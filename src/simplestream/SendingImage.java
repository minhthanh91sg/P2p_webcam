package simplestream;

import java.io.PrintWriter;

import org.apache.commons.codec.binary.Base64;

import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;

public class SendingImage extends Thread {

	OpenIMAJGrabber grabber;
	PrintWriter out;
	int width;
	int height;
	int rate;
	Message msgOut;

	public SendingImage(OpenIMAJGrabber grabber, PrintWriter out, int width,
			int height, int rate) {
		super();
		this.grabber = grabber;
		this.out = out;
		this.width = width;
		this.height = height;
		this.rate = rate;
	}

	@Override
	public void run() {
		// send a start stream message first
		Message msgOut;
		String msgOutJS;

		msgOut = new StartStreamMessage(width, height);
		msgOutJS = msgOut.ToJSON();
		out.println(msgOutJS);

		while (true) {
			/* start sending the local image to the client */
			/* Get a frame from the webcam. */
			grabber.nextFrame();
			/* Get the raw bytes of the frame. */
			byte[] raw_image = grabber.getImage().getBytes(width * height * 3);
			/* Apply a crude kind of image compression. */
			byte[] compressed_image = Compressor.compress(raw_image);
			/*
			 * Prepare the data to be sent in a text friendly format.
			 */
			byte[] base64_image = Base64.encodeBase64(compressed_image);
			/* transform the data to JSON string */
			msgOut = new ImageMessage(base64_image);
			msgOutJS = msgOut.ToJSON();
			/*
			 * The image data can be sent to connected clients.
			 */
			out.println(msgOutJS);
			
			
			try {
				Thread.sleep(rate);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

}
