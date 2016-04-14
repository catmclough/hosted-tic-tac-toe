package javaserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SocketWriter {
	protected OutputStream writingMechanism;
	protected boolean isOutputStreamOpen = true;

	public void openWriter(Socket clientSocket) {
		try {
			this.writingMechanism = clientSocket.getOutputStream();;
		} catch (IOException e) {
			System.out.println("Unable to open output stream.");
			e.printStackTrace();
		}
	}

	public void respond(byte[] response) {
		try {
			writingMechanism.write(response);
		} catch (IOException e) {
			System.out.println("Unable to write response to OutputStream.");
			e.printStackTrace();
		}
		closeOutputStream();
	}

	public void closeOutputStream() {
		try {
			writingMechanism.close();
		} catch (IOException e) {
			System.out.println("Unable to close output stream: ");
			e.printStackTrace();
		}
		this.isOutputStreamOpen = false;
	}
}
