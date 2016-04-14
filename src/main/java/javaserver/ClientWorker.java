package javaserver;

import java.io.IOException;
import java.net.Socket;
import javaserver.responders.Responder;

public class ClientWorker implements Runnable {
	private Socket clientSocket;
	protected Reader reader;
	protected SocketWriter writer;
	protected RequestLog requestLog;

	public ClientWorker(Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.reader = new Reader();
		this.writer = new SocketWriter();
		this.requestLog = App.log;
	}

	public void run() {
		reader.openReader(clientSocket);
		String rawRequest = getRequest(reader);
		requestLog.addRequest(rawRequest);
		Request request = RequestParser.createRequest(rawRequest);
		Responder responder = Routes.getResponder(RequestParser.getURIWithoutParams(request.getURI()));
		Response response = responder.getResponse(request);

		writer.openWriter(clientSocket);
		writer.respond(response.formatResponse());
		try {
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Unable to close client socket ");
			e.printStackTrace();
		}
	}

	private String getRequest(Reader reader) {
		String request = "";
		try {
			request = reader.readFromSocket();
		} catch (IOException | NullPointerException e) {
			System.out.println("Did not get any request from socket.");
		}
		return request;
	}
}
