package javaserver;

import static org.junit.Assert.*;
import org.junit.Test;

public class RequestParserTest {
	private String rawRequest = "POST /users/123 HTTP/1.1";
	private String requestMethod = "POST";
	private String requestURI = "/users/123";
	private String partialData = "bytes=0-4";
	private String requestWithRange = "GET /partial_content.txt HTTP/1.1" + System.lineSeparator() + "Range: bytes=0-4";
	private String etag = "xyz";
	private String patchedContent = "new content";
	private String requestWithEtagAndData = "PATCH /path-content.txt HTTP/1.1" + System.lineSeparator() + "If-Match: xyz\n"
	        + System.lineSeparator() + System.lineSeparator() + patchedContent;
	private String uriWithParams = "GET /parameters?foo=bar";
	private String uriWithoutParams = "GET /parameters";

	@Test
	public void testParsesMethod() {
		assertEquals(RequestParser.getRequestMethod(rawRequest), requestMethod);
	}

	@Test
	public void testParsesURI() {
		assertEquals(RequestParser.getRequestURI(rawRequest), requestURI);
	}

	@Test
	public void testURIWithoutParams() {
		assertEquals(RequestParser.getURIWithoutParams(uriWithParams), uriWithoutParams);
	}

	@Test
	public void testParsesRangeHeader() {
		assertEquals(RequestParser.getRequestHeaders(requestWithRange).get("Range"), partialData);
	}

	@Test
	public void testParsesEtag() {
	    assertEquals(RequestParser.getRequestHeaders(requestWithEtagAndData).get("If-Match"), etag);
	}

	@Test
	public void testParsesData() {
	    assertEquals(RequestParser.getRequestData(requestWithEtagAndData), patchedContent);
	}
}
