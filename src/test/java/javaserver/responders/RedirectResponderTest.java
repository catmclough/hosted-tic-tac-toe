package javaserver.responders;

import static org.junit.Assert.*;
import org.junit.Test;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.RequestParser;
import javaserver.Response;

public class RedirectResponderTest {
	private String redirectRoute = "/redirect";
	private String redirectHeader = "Location: http://localhost:5000/";
	private Request supportedRequest = RequestParser.createRequest("GET " + redirectRoute);

	private String[] supportedMethods = new String[] {"GET"};
	private RedirectResponder responder = new RedirectResponder(supportedMethods);

	@Test
	public void testRedirectResponderCreation() {
	    assertEquals(responder.getClass(), RedirectResponder.class);
	}

	@Test
	public void testRedirectRespondsWith302() {
		Response response = responder.getResponse(supportedRequest);
		assertEquals(response.getResponseCode(), HTTPStatusCode.THREE_OH_TWO.getStatusLine());
	}

	@Test
	public void testInvalidRedirectRespondsWith404() {
	    Request unsupportedRequest = RequestParser.createRequest("POST " + redirectRoute);
		Response response = responder.getResponse(unsupportedRequest);
		assertEquals(response.getResponseCode(), HTTPStatusCode.FOUR_OH_FOUR.getStatusLine());
	}

	@Test
	public void testRedirectHeader() {
		Response redirectResponse = responder.getResponse(supportedRequest);
		assertEquals(redirectResponse.getHeader(), redirectHeader);
	}
}
