package simplestream;

import java.io.File;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class CommandLineValues {

	@Option(name = "-sport", required = false, usage = "server port")
	private int serverport = 6262;

	@Option(name = "-remote", required = false, usage = "remote connection hostnames")
	private String remotehosts = "";//"10.9.251.176";

	@Option(name = "-rport", required = false, usage = "remote connection ports number")
	private String remoteport = "";//"6262";

	@Option(name = "-width", required = false, usage = "image width sending")
	private int width = 640;

	@Option(name = "-height", required = false, usage = "image height sending")
	private int height = 480;

	@Option(name = "-rate", required = false, usage = "sleep time desired")
	private int rate = 100;

	public CommandLineValues(String... args) {
		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getRemotehosts() {
		return remotehosts;
	}

	public void setRemotehosts(String remotehosts) {
		this.remotehosts = remotehosts;
	}

	public String getRemoteport() {
		return remoteport;
	}

	public void setRemoteport(String remoteport) {
		this.remoteport = remoteport;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getServerport() {
		return serverport;
	}

	public void setServerport(int serverport) {
		this.serverport = serverport;
	}

}
