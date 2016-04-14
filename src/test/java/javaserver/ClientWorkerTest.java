package javaserver;

import java.net.Socket;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

public class ClientWorkerTest extends TestCase {
	private ClientWorker clientWorker;
	private MockClientSocket mockSocket;
	private MockReader mockReader;
	private MockSocketWriter mockWriter;
    private String getRequest = "GET /foo";

	@Before
	public void setUp() throws Exception {
		mockSocket = new MockClientSocket();
		clientWorker = new ClientWorker(mockSocket);

		mockReader = new MockReader(getRequest);
		mockWriter = new MockSocketWriter();
		clientWorker.reader = mockReader;
		clientWorker.writer = mockWriter;
		clientWorker.run();
	}

	public void testSetsUpLog() {
	    assertNotNull(clientWorker.requestLog);
	}

	@Test
	public void testOpensReader() {
		assertTrue(mockReader.opened);
	}

	@Test
	public void testGetsAndLogsRequest() {
	    assertTrue(clientWorker.requestLog.getLogContents().contains(getRequest));
	}

	@Test
	public void testOpensWriter() {
		assertTrue(mockWriter.opened);
	}

	@Test
	public void testSendsFormattedResponseToWriter() {
		String fourOhFour = HTTPStatusCode.FOUR_OH_FOUR.getStatusLine() + "\n\n";
		assertTrue(Arrays.equals(fourOhFour.getBytes(), mockWriter.latestResponse));
	}

	@Test
	public void testClosesClientSocket() {
	  clientWorker.run();
	  assertTrue(mockSocket.isClosed());
	}

	class MockClientSocket extends Socket {
		MockClientSocket() {
			super();
		}
	}

	class MockReader extends Reader {
		private String request;
		public boolean opened = false;

		MockReader(String request) {
			this.request = request;
		}

		@Override
		public void openReader(Socket socket) {
			this.opened = true;
		}

		@Override
		public String readFromSocket() {
			return request;
		}
	}

	class MockSocketWriter extends SocketWriter {
	  public byte[] latestResponse;
		public boolean opened = false;
		public boolean closed = false;

		@Override
		public void openWriter(Socket socket) {
			this.opened = true;
		}

		@Override
		public void respond(byte[] response) {
		    this.latestResponse = response;
		}

		@Override
		public void closeOutputStream() {
			this.closed = true;
		}
	}
}
