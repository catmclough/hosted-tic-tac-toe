package javaserver.responders;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javaserver.Request;
import javaserver.HTMLContent;
import javaserver.HTTPStatusCode;
import javaserver.RequestParser;
import javaserver.Response;

public class DirectoryResponderTest {
	private String directoryRoute = "/";
	private String directoryContentType = "Content-Type: text/html;";
	private static String[] supportedDirectoryMethods = new String[] {"GET"};
	private static File file1;
	private static File file2;

	private Request supportedRequest = RequestParser.createRequest("GET " + directoryRoute);
	private static File mockPublicDirectory;
	private static Responder responder;

	@BeforeClass
	public static void setupPublicDirectory() throws IOException {
	   mockPublicDirectory = new File("tempFolder");
	   mockPublicDirectory.mkdir();
	   file1 = File.createTempFile("file", "1", mockPublicDirectory);
	   file2 = File.createTempFile("file", "2", mockPublicDirectory);
	   responder = new DirectoryResponder(supportedDirectoryMethods, mockPublicDirectory);
	}

	@AfterClass
	public static void deleteMockDirectory() {
	   file1.delete();
	   file2.delete(); 
	   mockPublicDirectory.delete();
	}

	@Test
	public void testDirectoryResponderCreation() {
	    assertEquals(responder.getClass(), DirectoryResponder.class);
	}

	@Test
	public void testRespondsWith200() {
		Response response = responder.getResponse(supportedRequest);
		assertEquals(response.getResponseCode(), HTTPStatusCode.TWO_HUNDRED.getStatusLine());
	}

	@Test
	public void testRespondsWith404() {
	    Request unsupportedRequest = RequestParser.createRequest("POST " + directoryRoute);
		Response unsupportedRequestResponse = responder.getResponse(unsupportedRequest);
		assertEquals(unsupportedRequestResponse.getResponseCode(), HTTPStatusCode.FOUR_OH_FOUR.getStatusLine());
	}

	@Test
	public void testRespondsWithContentTypeHeader() {
		Response response = responder.getResponse(supportedRequest);
		assertEquals(response.getHeader(), directoryContentType);
	}

	@Test
	public void testDirectoryLinksBody() {
		Response response = responder.getResponse(supportedRequest);
		String responseBody = response.getBody();

		String listOfDirectoryLinks = HTMLContent.listOfLinks(mockPublicDirectory.list());
		assertTrue(responseBody.contains(listOfDirectoryLinks));
		assertTrue(responseBody.contains(file1.getName()));
		assertTrue(responseBody.contains(file2.getName()));
	}
}
