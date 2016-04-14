package javaserver;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.junit.Test;
import java.io.ByteArrayInputStream;

public class ReaderTest {
	private Reader testReader;
	private BufferedReader testBufferedReader;
	private String simpleRequestLine = "GET /";
	private String requestWithData = "POST /form HTTP/1.1\n\n\"Data\"=\"My Info\"";

	@Test
	public void testReaderReadsSimpleRequestLine() throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(simpleRequestLine.getBytes());
		testBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		testReader = new Reader();
		testReader.readingMechanism = testBufferedReader;

		assertEquals("Simple request line was not properly read", testReader.readFromSocket(), simpleRequestLine);
	}

	@Test
	public void testReaderReadsMessageWithData() throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(requestWithData.getBytes());
		testBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		testReader = new Reader();
		testReader.readingMechanism = testBufferedReader;

		assertEquals("Request with data was not properly read", testReader.readFromSocket(), requestWithData);
	}
}
