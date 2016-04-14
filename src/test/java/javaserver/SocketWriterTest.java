package javaserver;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class SocketWriterTest {
	private static SocketWriter testWriter;
	private static String standardResponse = "200 A-OKAY";

	@Before
	public void setUp() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		testWriter = new SocketWriter();
		testWriter.writingMechanism = dataOutputStream;
	}

	@Test
	public void testWritesByteResponse() {
	  String exception = null;
	  byte[] responseBytes = standardResponse.getBytes();
	  try {
          testWriter.respond(responseBytes);
	  } catch (Exception e) {
	      exception = e.getMessage();
	  }
	  assertNull(exception);
	}

	@Test
	public void testClosesOutputStream() throws IOException {
		testWriter.closeOutputStream();
		assertFalse(testWriter.isOutputStreamOpen);
	}
}
