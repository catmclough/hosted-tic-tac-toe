package javaserver.responders;

import static org.junit.Assert.*;
import org.junit.Test;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.RequestParser;
import javaserver.Response;

public class ErrorResponderTest {
	private String unknownRoute = "/foobar";
	private String unknownRouteWithParams = "/foobar?var1=xyz";
	private String unknownFileWithPartial = "/foobar\nRange: bytes=0-10";
	private Request unsupportedRequest = RequestParser.createRequest("GET " + unknownRoute);

	private String fourOhFour = HTTPStatusCode.FOUR_OH_FOUR.getStatusLine();

	private ErrorResponder responder = new ErrorResponder();

	@Test
	public void test404ResponseCode() {
		Response unsupportedRequestResponse = responder.getResponse(unsupportedRequest);
		assertEquals(unsupportedRequestResponse.getResponseCode(), fourOhFour);
	}

	@Test
	public void testRespondsToRequestiWithoutURI() {
		Response response = responder.getResponse(RequestParser.createRequest("GET"));
		assertEquals(response.getResponseCode(), fourOhFour);
	}

	@Test
	public void testRespondsToBlankRequest() {
		Response response = responder.getResponse(RequestParser.createRequest(""));
		assertEquals(response.getResponseCode(), fourOhFour);
	}

	@Test
	public void testRespondsToCrazyRequest() {
		Response response = responder.getResponse(RequestParser.createRequest(""));
		assertEquals(response.getResponseCode(), fourOhFour);
	}

	@Test
	public void testRespondsToInvalidCodedParams() {
		Response unknownRouteResponse = responder.getResponse(RequestParser.createRequest(unknownRouteWithParams));
		assertEquals(unknownRouteResponse.getResponseCode(), fourOhFour);
	}

	@Test
	public void testInvalidiFileRequestWithPartial() {
		Response invalidRangeResponse = responder.getResponse(RequestParser.createRequest(unknownFileWithPartial));
		assertEquals(invalidRangeResponse.getResponseCode(), fourOhFour);
	}
}

