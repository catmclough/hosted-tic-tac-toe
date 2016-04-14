package javaserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader {
	protected BufferedReader readingMechanism;
	private char lastCharOfRequest = (char) -1;

	public void openReader(Socket clientSocket) {
		InputStreamReader input;
		try {
			input = new InputStreamReader(clientSocket.getInputStream());
			this.readingMechanism = new BufferedReader(input);
		} catch (IOException e) {
			System.out.println("Reader was unable to get input stream" + e.getStackTrace());
		}
	}

	public String readFromSocket() throws IOException {
		String fullRequest = "";
		fullRequest += getRequestLine();
		if (readingMechanism.ready()) {
			fullRequest += System.lineSeparator();
			fullRequest += getData();
		}
		return fullRequest;
	}

	private String getRequestLine() throws IOException {
		return readingMechanism.readLine();
	}

	private String getData() throws IOException {
		String data = "";
		while (readingMechanism.ready()) {
			char nextChar = read();
			if (nextChar != lastCharOfRequest) {
				data += nextChar;
			} else {
				break;
			}
		}
		return data;
	}

	private char read() throws IOException {
		return (char) readingMechanism.read();
	}
}
