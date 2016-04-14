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

public class FileResponderTest {
    private String textFileRoute = "/file1";
	private static String textFileContents = "file1 contents";
	private static File mockPublicDirectory;
	private static File textFile;
	private static String[] supportedMethods = new String[] {"GET", "PATCH"};
	private static FileResponder responder;

	@BeforeClass
	public static void createTextFile() throws IOException {
	   mockPublicDirectory = new File("tempFolder");
	   mockPublicDirectory.mkdir(); 
	   
	   textFile = new File(mockPublicDirectory, "file1");

	   FileWriter writer = new FileWriter(textFile); 
	   writer.write(textFileContents); 
	   writer.flush();
	   writer.close();

       responder = new FileResponder(supportedMethods, mockPublicDirectory);
	}
	
	@AfterClass
	public static void deleteFiles() {
	    textFile.delete();
	    mockPublicDirectory.delete();
	}
	
	@Test
	public void testSupportedFileRequestResponseCode() {
	    Request getFile = RequestParser.createRequest("GET " + textFileRoute);
		Response fileResponse = responder.getResponse(getFile);
		assertEquals(fileResponse.getResponseCode(), HTTPStatusCode.TWO_HUNDRED.getStatusLine());
	}

	@Test
	public void testMethodNotAllowedResponseCode() {
	    Request postFile = RequestParser.createRequest("POST " + textFileRoute);
		Response unallowedRequestResponse = responder.getResponse(postFile);
		assertEquals(unallowedRequestResponse.getResponseCode(), HTTPStatusCode.FOUR_OH_FIVE.getStatusLine());
	}

	@Test
	public void testFileContentsInBody() {
	  Request getFile = RequestParser.createRequest("GET " + textFileRoute);
		Response existingFileResponse = responder.getResponse(getFile);
		assertTrue(existingFileResponse.getBody().contains(textFileContents));
	}

	@Test
	public void testCreatesPartialResponder() {
	  Request getPartial = RequestParser.createRequest("GET " + textFileRoute + "\nRange: bytes=0-4");
	  Response partialResponse = responder.getResponse(getPartial);
	  assertEquals(partialResponse.getResponseCode(), HTTPStatusCode.TWO_OH_SIX.getStatusLine());
	}

	@Test
	public void testCreatesAndRespondsWithPatchResponder() {
	  Request patchContent = RequestParser.createRequest("PATCH " + textFileRoute + "\nIf-Match: xyz");
	  Response patchResponse = responder.getResponse(patchContent);
	  assertEquals(patchResponse.getResponseCode(), HTTPStatusCode.TWO_OH_FOUR.getStatusLine());
	}
}
