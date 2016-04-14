package javaserver.responders;

import static org.junit.Assert.*;
import org.junit.Test;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.RequestParser;
import javaserver.Response;

public class OptionResponderTest {
	private String optionRoute = "/method_options";
	private String[] supportedOptionMethods = new String[] {"GET", "HEAD", "POST", "OPTIONS", "PUT"};
	private String methodOptionsHeader = "Allow: " + String.join(",", supportedOptionMethods);
	private Request methodOptionsRequest = RequestParser.createRequest("OPTIONS " + optionRoute);
	private Request getRequest = RequestParser.createRequest("GET " + optionRoute);

	private OptionResponder responder = new OptionResponder(supportedOptionMethods);

	private Response optionsResponse = responder.getResponse(methodOptionsRequest);
	private Response getResponse = responder.getResponse(getRequest);

	@Test
	public void testOptionsResponderCreation() {
	    assertEquals(responder.getClass(), OptionResponder.class);
	}

	@Test
	public void testValidOptionResponseCodes() {
		assertEquals(optionsResponse.getResponseCode(), HTTPStatusCode.TWO_HUNDRED.getStatusLine());
	}

	@Test
	public void testInvalidOptionResponseCode() {
	    Request invalidMethodOptionsRequest = RequestParser.createRequest("PATCH " + optionRoute);
	    Response invalidMethodResponse = responder.getResponse(invalidMethodOptionsRequest);
		assertEquals(invalidMethodResponse.getResponseCode(), HTTPStatusCode.FOUR_OH_FOUR.getStatusLine());
	}

	@Test
	public void testOptionsHeader() {
		assertTrue(optionsResponse.getHeader().contains(methodOptionsHeader));
	}

	@Test
	public void testOtherSupportedMethodResponseCode() {
		assertEquals(getResponse.getResponseCode(), HTTPStatusCode.TWO_HUNDRED.getStatusLine());
	}

	@Test
	public void testOtherSupportedMethodHeader() {
		assertTrue(getResponse.getHeader().isEmpty());
	}
}
