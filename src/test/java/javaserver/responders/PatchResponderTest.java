package javaserver.responders;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.RequestParser;
import javaserver.Response;

public class PatchResponderTest {
	private String textFileRoute = "/file.txt";
	private static String defaultContent = "default content";
	private String patchedContent = "new content";
	private String defaultContentMatchHeader = "If-Match: dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec";
	private String nonMatchingEtagHeader = "If-Match: x";

	private static File mockPublicDirectory;
	private static File textFile;
	private static PatchResponder responder;
	private static String[] supportedFileMethods = new String[] {"GET", "PATCH"};

	@BeforeClass
	public static void createTextFile() {
	   mockPublicDirectory = new File("tempFolder");
	   mockPublicDirectory.mkdir(); 
	   textFile = new File(mockPublicDirectory, "file.txt");
	   writeDefaultContentsToFile();
       responder = new PatchResponder(supportedFileMethods, mockPublicDirectory);
	}
	
	@After
	public void resetFile() {
	    writeDefaultContentsToFile();
	}

	@AfterClass
	public static void deleteFiles() {
	    textFile.delete();
	    mockPublicDirectory.delete();
	}
	
	private static void writeDefaultContentsToFile() {
        try {
            FileWriter writer = new FileWriter(textFile);
            writer.write(defaultContent); 
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
	}

	private String patchRequest(String ifMatchHeader) {
	    return "PATCH " + textFileRoute + System.lineSeparator() + ifMatchHeader
	        + System.lineSeparator() + System.lineSeparator() + patchedContent;
	}

	private Response getFile() {
	    Responder fileResponder = new FileResponder(supportedFileMethods, mockPublicDirectory);
	    Request request = RequestParser.createRequest("GET " + textFileRoute);
	    Response fileResponse = fileResponder.getResponse(request);
	    return fileResponse;
	}


	private String twoHundred = HTTPStatusCode.TWO_HUNDRED.getStatusLine();
	private String twoOhFour = HTTPStatusCode.TWO_OH_FOUR.getStatusLine();


	private Request validPatchRequest = RequestParser.createRequest(patchRequest(defaultContentMatchHeader));

	@Test
	public void testGetResponse() {
	   assertEquals(getFile().getResponseCode(), twoHundred);
	   assertEquals(getFile().getBody(), defaultContent);
	}

	@Test
	public void testNonMatchingEtagDoesNotChangeFileContents() {
	    Request invalidPatchRequest = RequestParser.createRequest(patchRequest(nonMatchingEtagHeader));
	    responder.getResponse(invalidPatchRequest);
	    assertEquals(getFile().getBody(), defaultContent);
	}

	@Test
	public void testPatchResponseCode() {
	    Response validPatchResponse = responder.getResponse(validPatchRequest);
	    assertEquals(validPatchResponse.getResponseCode(), twoOhFour);
	}

	@Test
	public void TestRecognizesEtagMatch() {
	   assertTrue(responder.etagMatchesFileContent(validPatchRequest));
	}

	@Test
	public void writesDataWhenEtagMatches() {
	    assertEquals(getFile().getBody(), defaultContent);
	    responder.getResponse(validPatchRequest);
	    assertEquals(getFile().getBody(), patchedContent);
	}
}
