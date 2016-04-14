package javaserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadPoolExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

public class ServerTest extends TestCase {
	private Server server;
	private Socket mockedClientSocket;
	int defaultPort = 6000;

	@Before
	public void setUp() throws Exception {
		ServerSocket mockedServerSocket = new MockServerSocket(defaultPort);
		server = new Server(mockedServerSocket);
	}

	@After
	public void tearDown() throws IOException {
		server.serverSocket.close();
	}

	@Test
	public void testAcceptsClient() throws IOException {
		server.run();
		assertEquals(mockedClientSocket.getChannel(), server.serverSocket.getChannel());
	}

	@Test
	public void testUsesThreadPoolExecutor() throws IOException, InterruptedException {
		server.run();
		Thread.sleep(1000);
		assertEquals(server.threadPool.getClass(), ThreadPoolExecutor.class);
	}

	@Test
	public void testExecutesThreadPool() throws IOException {
		server.run();
		int openThreads = ((ThreadPoolExecutor) server.threadPool).getActiveCount();
		assertEquals(1, openThreads);
	}

	@Test
	public void testShutsDownThreadPool() throws IOException {
		server.shutDown();
		assertTrue(server.threadPool.isShutdown());
	}

	@Test
	public void testClosesServerSocket() throws IOException {
		server.shutDown();
		assertTrue(server.serverSocket.isClosed());
	}

	@Test
	public void serverCanBeShutDown() {
		boolean caughtError = false;
		try {
			server.shutDown();
		} catch (IOException e) {
			caughtError = true;
		}
		assertFalse("Server's ServerSocket failed to close", caughtError);
	}

	class MockSocket extends Socket {
		MockSocket(String hostName, int port) throws UnknownHostException, IOException {
			super(hostName, port);
		}

	 	@Override
	 	public OutputStream getOutputStream() {
	 		ByteArrayOutputStream out = new ByteArrayOutputStream();
	 		return new DataOutputStream(out);
	 	}

	 	@Override
	 	public InputStream getInputStream() {
	 		return new ByteArrayInputStream("GET /foo".getBytes());
	 	}
	}

	class MockServerSocket extends ServerSocket {
		private int port;

		MockServerSocket(int port) throws IOException {
		  super(port);
		  this.port = port;
		}

		@Override
		public Socket accept() throws IOException {
		  mockedClientSocket = new MockSocket("localhost", port);
		  server.isOn = false;
		  return mockedClientSocket;
		}

	}

	class MockSocketWriter extends SocketWriter {
		public String latestResponse;

		MockSocketWriter(Socket clientSocket) {
		 	super();
	 	}

	 	@Override
	 	public void respond(byte[] response) {
	 		this.latestResponse = response.toString();
	 	}
	}
}
