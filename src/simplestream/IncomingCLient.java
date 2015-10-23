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

public class IncomingCLient extends Thread {

	// Viewer myViewer;
	// JFrame frame;
	OpenIMAJGrabber grabber;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	int width;
	int height;
	int rate;
	boolean frameOpen = true;

	Message msgIn;
	Message msgOut;
	String msgInStr;
	String msgOutJS;
	MessageFactory msgInFact = new MessageFactory();

	public IncomingCLient(OpenIMAJGrabber grabber, Socket socket, int width,
			int height, int rate) {
		this.grabber = grabber;
		this.socket = socket;
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

	/*
	 * public IncomingCLient(Socket socket,OpenIMAJGrabber grabber) {
	 * this.socket = socket; this.grabber = grabber; try { in = new
	 * BufferedReader(new InputStreamReader( this.socket.getInputStream())); out
	 * = new PrintWriter(this.socket.getOutputStream(), true); } catch
	 * (IOException e) { e.printStackTrace(); } }
	 */

	@Override
	public void run() {

		try {

			msgInStr = in.readLine();
			System.out.println(msgInStr);
			msgIn = msgInFact.FromJSON(msgInStr);
			System.out.println(msgIn);

			if (msgIn.Type().equals("startstream")) {

				final Thread sendingImage = new Thread(new SendingImage(grabber, out,
						width, height, rate));
				sendingImage.start();

				int clientWidth = ((StartStreamMessage) msgIn).getWidth();
				int clientHeight = ((StartStreamMessage) msgIn).getHeight();

				Viewer clientViewer = new Viewer(clientWidth, clientHeight);
				final JFrame clientFrame = new JFrame("Client Viewer");
				clientFrame.setVisible(true);
				clientFrame.setSize(clientWidth, clientHeight);
				clientFrame
						.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				clientFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {

						// upon close of a frame , send the corresponding
						// party a end stream message and close the socket

						msgOut = new StopStreamMessage();
						msgOutJS = msgOut.ToJSON();
						out.println(msgOutJS);
						clientFrame.dispose();
						try {
							socket.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						frameOpen = false;
						sendingImage.interrupt();
						//return;
						
					}
				});
				clientFrame.add(clientViewer);

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
						clientViewer.ViewerInput(decompressed_image);
						clientFrame.repaint();
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
				System.out.println("Disconneted from a client.");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
