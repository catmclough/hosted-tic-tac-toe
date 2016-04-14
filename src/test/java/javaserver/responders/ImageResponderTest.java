package javaserver.responders;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.junit.BeforeClass;
import org.junit.Test;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.RequestParser;
import javaserver.Response;

public class ImageResponderTest {
	private static String jpegRoute = "/image.jpeg";
	private String pngRoute = "/image.png";
	private String gifRoute = "/image.gif";
	private static File mockPublicDirectory;
	private static ImageResponder responder;
	private static String[] supportedFileMethods = new String[] {"GET"};

	@BeforeClass
	public static void loadTestResources() throws IOException {
	   mockPublicDirectory = new File("public/testResources");
       responder = new ImageResponder(supportedFileMethods, mockPublicDirectory);
	}

    private Request jpegRequest = RequestParser.createRequest("GET " + jpegRoute);

    @Test
    public void testImageResponderCreation() {
        assertEquals(responder.getClass(), ImageResponder.class);
    }

    @Test
    public void testPublicImageStatusLine() {
        Response imageResponse = responder.getResponse(jpegRequest);
        assertEquals(imageResponse.getResponseCode(), HTTPStatusCode.TWO_HUNDRED.getStatusLine());
    }

    @Test
    public void testUnknownImageStatusLine() {
        Request bogusRequest = RequestParser.createRequest("GET /bogus.png");
        Response imageResponse = responder.getResponse(bogusRequest);
        assertEquals(imageResponse.getResponseCode(), HTTPStatusCode.FOUR_OH_FOUR.getStatusLine());
    }

    @Test
    public void testContentLengthHeaderExists() {
       Response imageResponse = responder.getResponse(jpegRequest);
       assertTrue(imageResponse.getHeader().contains("Content-Length: "));
    }

    @Test
    public void testJPEGHeader() {
       Response imageResponse = responder.getResponse(jpegRequest);
       assertTrue(imageResponse.getHeader().contains("Content-Type: image/jpeg"));
    }

    @Test
    public void testPNGHeader() {
       Request pngRequest = RequestParser.createRequest("GET " + pngRoute);
       Response imageResponse = responder.getResponse(pngRequest);
       assertTrue(imageResponse.getHeader().contains("Content-Type: image/png"));
    }

    @Test
    public void testGIFHeader() {
       Request pngRequest = RequestParser.createRequest("GET " + gifRoute);
       Response imageResponse = responder.getResponse(pngRequest);
       assertTrue(imageResponse.getHeader().contains("Content-Type: image/gif"));
    }

    @Test
    public void testImageResponseBody() {
       Response imageResponse = responder.getResponse(jpegRequest);
       assertTrue(Arrays.equals(imageResponse.getBodyData(), responder.getImageData(jpegRequest)));
    }
}
