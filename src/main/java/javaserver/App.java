package javaserver;

import java.io.IOException;
import java.net.ServerSocket;

public class App {
	public static String name = "Cat's Java Server";
	public static final int DEFAULT_PORT = 5000;
	public static final String DEFAULT_PUBLIC_DIRECTORY = "public/";
	protected static int port;
	protected static Server server;
	public static RequestLog log = initializeLog();

	public static void main(String[] args) throws IOException {
		setUpServer(args);
		runServer(server);
	}
	
	private static RequestLog initializeLog() {
	    return new RequestLog();
	}

	protected static void setUpServer(String[] args) throws IOException {
	    handleArgs(args);
		server = new Server(new ServerSocket(port));
	}

	private static void handleArgs(String[] args) {
		port = ArgHandler.getPort(args, DEFAULT_PORT);

		String directoryName = ArgHandler.getDirectory(args, DEFAULT_PUBLIC_DIRECTORY);
		try {
            DirectoryHandler.createPublicDirectory(directoryName);
		} catch (DirectoryNotFoundException e) {
			e.getMessage();
			System.exit(0);
		}
	}

	protected static void runServer(Server server) throws IOException {
		System.out.println("Server running on port " + port);
		System.out.println("Public Directory: " + DirectoryHandler.getPublicDirectoryPath());
		server.run();
	}
}
