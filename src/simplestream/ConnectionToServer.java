package simplestream;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

import org.apache.commons.codec.binary.Base64;

import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;

public class ConnectionToServer extends Thread {

	Socket socket;
	OpenIMAJGrabber grabber;
	int width;
	int height;
	int rate;
	boolean frameOpen = true;

	Message msgIn;
	Message msgOut;
	MessageFactory msgInFact = new MessageFactory();
	String msgInStr;
	String msgOutJS;

	BufferedReader in;
	PrintWriter out;

	public ConnectionToServer(Socket socket, OpenIMAJGrabber grabber,
			int width, int height, int rate) {
		this.socket = socket;
		this.grabber = grabber;
		this.width = width;
		this.height = height;
		this.rate = rate;
		try {
			in = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream()));
			out = new PrintWriter(this.socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		final Thread sendingImage = new Thread(new SendingImage(grabber, out,
				width, height, rate));
		sendingImage.start();

		// String line = null;
		System.out.println("Connected to a server");

		try {

			msgInStr = in.readLine();
			System.out.println(msgInStr);
			msgIn = msgInFact.FromJSON(msgInStr);

			if (msgIn.Type().equals("startstream")) {

				int clientWidth = ((StartStreamMessage) msgIn).getWidth();
				int clientHeight = ((StartStreamMessage) msgIn).getHeight();

				Viewer serverViewer = new Viewer(clientWidth, clientHeight);
				final JFrame serverFrame = new JFrame("Server Viewer");
				serverFrame.setVisible(true);
				serverFrame.setSize(clientWidth, clientHeight);
				serverFrame
						.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				serverFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {

						// upon close of a frame , send the corresponding
						// party a end stream message and close the socket

						msgOut = new StopStreamMessage();
						msgOutJS = msgOut.ToJSON();
						out.println(msgOutJS);
						serverFrame.dispose();
						try {
							socket.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						frameOpen = false;
						sendingImage.interrupt();
						// return;
					}
				});
				serverFrame.add(serverViewer);

				while (frameOpen) {
					msgInStr = in.readLine();
					System.out.println(msgInStr);
					msgIn = msgInFact.FromJSON(msgInStr);

					if (msgIn.Type().equals("image")) {

						/*
						 * Assume we received some image data. Remove the text
						 * friendly encoding.
						 */
						byte[] nobase64_image = Base64
								.decodeBase64(((ImageMessage) msgIn)
										.getBase64_image());
						/* Decompress the image */
						byte[] decompressed_image = Compressor
								.decompress(nobase64_image);
						/* Give the raw image bytes to the viewer. */
						serverViewer.ViewerInput(decompressed_image);
						serverFrame.repaint();
						// System.out.println(msgInStr);

					}

					/*
					 * if received a stop stream message, send back a stop
					 * stream message and stop displaying image
					 */
					if (msgIn.Type().equals("stopstream")) {
						msgOut = new StopStreamMessage();
						msgOutJS = msgOut.ToJSON();
						out.println(msgOutJS);
						sendingImage.interrupt();
						break;
					}
					
					if (MultiView.localFrameOpen = false) {

						msgOut = new StopStreamMessage();
						msgOutJS = msgOut.ToJSON();
						out.println(msgOutJS);
						sendingImage.interrupt();
						break;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				System.out.println("Disconneted from a server.");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
