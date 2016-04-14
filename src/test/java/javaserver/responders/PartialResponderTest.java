package javaserver.responders;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.RequestParser;
import javaserver.Response;

public class PartialResponderTest {
	private String textFileRoute = "/file.txt";
	private static String textFileContents = "default content";
	private static File mockPublicDirectory;
	private static File textFile;
	private static PartialResponder responder;
	private static String[] supportedFileMethods = new String[] {"GET", "PATCH"};

	@BeforeClass
	public static void createTextFile() throws IOException {
	   mockPublicDirectory = new File("tempFolder");
	   mockPublicDirectory.mkdir(); 
	   textFile = new File(mockPublicDirectory, "file.txt");
	   FileWriter writer = new FileWriter(textFile); 
	   writer.write(textFileContents); 
	   writer.flush();
	   writer.close();

       responder = new PartialResponder(supportedFileMethods, mockPublicDirectory);
	}

	@AfterClass
	public static void deleteFiles() {
	    textFile.delete();
	    mockPublicDirectory.delete();
	}

	private String twoOhSix = HTTPStatusCode.TWO_OH_SIX.getStatusLine();

    Request fullRangeRequest = RequestParser.createRequest("GET " + textFileRoute + "\nRange: bytes=0-4");

	@Test
	public void testFullRangePartialRequestResponseCode() {
		Response partial = responder.getResponse(fullRangeRequest);
		assertEquals(partial.getResponseCode(), twoOhSix);
	}

	@Test
	public void testInvalidPartialRequestResponseCode() {
	    Request invalidRequest = RequestParser.createRequest("POST " + textFileRoute + "\nRange: bytes=0-4");
		Response partial = responder.getResponse(invalidRequest);
		assertEquals(partial.getResponseCode(), HTTPStatusCode.FOUR_OH_FIVE.getStatusLine());
	}

	@Test
	public void testHalfOfRangeRequestResponseCode() {
	    Request noBeginningPartial = RequestParser.createRequest("GET " + textFileRoute + "\nRange: bytes=-4");
	    Request noEndPointPartial = RequestParser.createRequest("GET " + textFileRoute + "\nRange: bytes=4-");
		Response noBeginningPartialResponse = responder.getResponse(noBeginningPartial);
		assertEquals(noBeginningPartialResponse.getResponseCode(), twoOhSix);

		Response noEndPartialResponse = responder.getResponse(noEndPointPartial);
		assertEquals(noEndPartialResponse.getResponseCode(), twoOhSix);
	}

	@Test
	public void testPartialContentWithFullRange() {
		String firstFiveBytes = textFileContents.substring(0, 5);
		Response partialResponse = responder.getResponse(fullRangeRequest);
		assertEquals(partialResponse.getBody(), firstFiveBytes);
	}

	@Test
	public void testPartialContentWithEndOfRange() {
		Request request = RequestParser.createRequest("GET " + textFileRoute + "\nRange: bytes=-6");
		String lastSixBytes = textFileContents.substring(textFileContents.length() - 6);
		Response partialResponse = responder.getResponse(request);
		assertEquals(partialResponse.getBody(), lastSixBytes);
	}

	@Test
	public void testPartialContentWithStartOfRange() {
	    Request startOfRangeRequest = RequestParser.createRequest("GET " + textFileRoute + "\nRange: bytes=4-");
	    String bytesFrom4 = textFileContents.substring(4);
	    Response response = responder.getResponse(startOfRangeRequest);
		assertEquals(response.getBody(), bytesFrom4);
	}
}
