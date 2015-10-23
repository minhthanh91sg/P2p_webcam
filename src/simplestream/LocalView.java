package simplestream;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;

public class LocalView extends Thread {

	Viewer myViewer;
	JFrame frame;
	OpenIMAJGrabber grabber;
	int width;
	int height;
	boolean frameOpen = true;

	public LocalView(OpenIMAJGrabber grabber) {

		this.grabber = grabber;
	}

	public LocalView(OpenIMAJGrabber grabber, int width, int height) {
		this.grabber = grabber;
		this.width = width;
		this.height = height;
	}

	@Override
	public void run() {

		Viewer myViewer = new Viewer(width, height);
		final JFrame frame = new JFrame("Simple Stream Viewer");
		frame.setVisible(true);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// System.out.println("frame closing");
				frame.dispose();
				MultiView.localFrameOpen = false;
				System.exit(0);
				// frameOpen = false;

				// Thread.currentThread().interrupt();
				// grabber.stopSession();
				// return;
			}
		});
		frame.add(myViewer);

		while (frameOpen) {

			/* Get a frame from the webcam. */
			grabber.nextFrame();
			/* Get the raw bytes of the frame. */
			byte[] raw_image = grabber.getImage().getBytes(width * height * 3);
			/* print it out on my screen */
			myViewer.ViewerInput(raw_image);
			frame.repaint();
		}
		System.out.println("about to close grabber");
		grabber.stopSession();
	}

}