package simplestream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;

public class AsServer extends Thread {
	
	ServerSocket serversocket = null;
	Socket socket = null;
	//Viewer myViewer;
	//JFrame frame;
	OpenIMAJGrabber grabber;
	int serverport;
	int width;
	int height;
	int rate;
	

	public AsServer(OpenIMAJGrabber grabber) {
		this.grabber = grabber;
	}


	public AsServer(OpenIMAJGrabber grabber, int serverport,int width,int height,int rate) {
		this.grabber = grabber;
		this.serverport = serverport;
		this.width = width;
		this.height = height;
		this.rate = rate;
	}


	@Override
	public void run() {
		/* multi-Threading for possible incoming client connection here */
		try {
			int port = serverport;
			serversocket = new ServerSocket(port);
			System.out.println("Server's listening...");
			while (true) {
				socket = serversocket.accept();
				System.out.println("Connected.");
				Thread incomingClient = new Thread(new IncomingCLient(grabber,socket,width,height,rate));
				incomingClient.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null)
				try {
					socket.close();
					serversocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
	}
	
	

}
