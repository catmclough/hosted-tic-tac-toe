package javaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	protected ServerSocket serverSocket;
	protected ExecutorService threadPool;
	protected boolean isOn = true;
	protected ClientWorker clientWorker;

	Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		this.threadPool = Executors.newFixedThreadPool(8);
	}

	public void run() throws IOException {
		while (isOn()) {
			Socket clientSocket = serverSocket.accept();
			clientWorker = new ClientWorker(clientSocket);
			threadPool.execute(clientWorker);
		}
		shutDown();
	}

	private boolean isOn() {
		return isOn;
	}

	public void shutDown() throws IOException {
		threadPool.shutdown();
		serverSocket.close();
		this.isOn = false;
	}
}
