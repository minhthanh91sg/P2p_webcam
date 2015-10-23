package simplestream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JFrame;

import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;

public class AsClient extends Thread {
	Socket socket = null;
	// Viewer myViewer;
	// JFrame frame;
	OpenIMAJGrabber grabber;
	String remotehosts;
	String remoteport;
	int width;
	int height;
	int rate;

	// ArrayList<String> remoteHostList;
	// ArrayList<Integer> remotePortList;

	public AsClient(OpenIMAJGrabber grabber, String remotehosts,
			String remoteport, int width, int height, int rate) {
		super();
		this.grabber = grabber;
		this.remotehosts = remotehosts;
		this.remoteport = remoteport;
		this.width = width;
		this.height = height;
		this.rate = rate;
	}

	@Override
	public void run() {
		try {
			StringTokenizer stkHosts = new StringTokenizer(remotehosts, " ,");
			StringTokenizer stkPorts = new StringTokenizer(remoteport, " ,");
			int port;
			String host;
			while (stkHosts.hasMoreTokens()) {
				host = stkHosts.nextToken();
				port = Integer.parseInt(stkPorts.nextToken());
				
				socket = new Socket(host, port);
				Thread connectionToServer = new Thread(new ConnectionToServer(
						socket, grabber,width,height,rate));
				connectionToServer.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
