package javaserver;

public enum HTTPStatusCode {
	TWO_HUNDRED ("HTTP/1.1 200 OK"),
	TWO_OH_FOUR ("HTTP/1.1 204 No Content"),
	TWO_OH_SIX ("HTTP/1.1 206 Partial Content"),
	THREE_OH_TWO ("HTTP/1.1 302 Found"),
	FOUR_OH_ONE ("HTTP/1.1 401 Unauthorized"),
	FOUR_OH_FOUR ("HTTP/1.1 404 Not Found"),
	FOUR_OH_FIVE ("HTTP/1.1 405 Method Not Allowed");

	private final String statusLine;

	private HTTPStatusCode(String statusLine) {
		this.statusLine = statusLine;
	}

	public String getStatusLine() {
		return this.statusLine;
	}
}
