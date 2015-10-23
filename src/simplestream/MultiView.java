package simplestream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.commons.codec.binary.Base64;
import org.bridj.Pointer;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.github.sarxos.webcam.ds.buildin.natives.Device;
import com.github.sarxos.webcam.ds.buildin.natives.DeviceList;
import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;

public class MultiView {

	static boolean localFrameOpen = true;

	public static void main(String[] args) {

		CommandLineValues values = new CommandLineValues(args);
		CmdLineParser parser = new CmdLineParser(values);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.exit(1);
		}

		int serverport = values.getServerport();
		String remotehosts = values.getRemotehosts();
		String remoteport = values.getRemoteport();
		int width = values.getWidth();
		int height = values.getHeight();
		int rate = values.getRate();
		
		boolean localFrameOpen = true;

		// Viewer myViewer = new Viewer();
		// JFrame frame = new JFrame("Simple Stream Viewer");
		// frame.setVisible(true);
		// frame.setSize(160, 120);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.add(myViewer);

		//rrayList<String> remoteHostList = new ArrayList<String>();
		//ArrayList<Integer> remotePortList = new ArrayList<Integer>();

		/**
		 * This example show how to use native OpenIMAJ API to capture raw bytes
		 * data as byte[] array. It also calculates current FPS???????.
		 */

		OpenIMAJGrabber grabber = new OpenIMAJGrabber();

		Device device = null;
		Pointer<DeviceList> devices = grabber.getVideoDevices();
		for (Device d : devices.get().asArrayList()) {
			device = d;
			break;
		}

		boolean started = grabber.startSession(width, height, 60,
				Pointer.pointerTo(device));

		if (!started) {
			throw new RuntimeException("Not able to start native grabber!");
		}

		/*
		 * Start a local view thread first ???? should i wait for customer
		 * specification ????
		 */
		Thread localview = new Thread(new LocalView(grabber, width, height));
		localview.start();
		//Thread localview2 = new Thread(new LocalView(grabber, width, height));
		//localview2.start();

		/*
		 * start a server thread that is listening, so any incoming client will
		 * be handled
		 */
		Thread asServer = new Thread(new AsServer(grabber, serverport, width,
				height, rate));
		asServer.start();

		/*
		 * check if there is any connection request at hand,i.e. if command line
		 * option pass any argument for connection.If requests exist, start a
		 * client thread to handle the connection request.
		 */
		if (!remotehosts.equals("")) {
			Thread asClient = new Thread(new AsClient(grabber, remotehosts,
					remoteport,width,height,rate));
			asClient.start();
		}
		
		//while(true){
		//	System.out.println(Thread.activeCount());
		//}
	}
}
