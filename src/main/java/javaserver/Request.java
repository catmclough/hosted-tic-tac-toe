package javaserver;

import java.util.HashMap;

public class Request {
	private String method;
	private String uri;
	private HashMap<String, String> headers;
	private String data;

	public Request(String method, String uri, HashMap<String, String> headers, String data) {
		this.method = method;
		this.uri = uri;
		this.headers = headers;
		this.data = data;
	}

	public String getMethod() {
		return method;
	}

	public String getURI() {
		return uri;
	}

	public HashMap<String, String> getHeaders() {
	    return headers;
	}

	public String getData() {
		return data;
	}

	public boolean hasParams() {
		return uri.contains("?");
	}

	public String getURIWithoutParams() {
		String[] routeParts = uri.split("\\?", 2);
		return routeParts[0];
	}
}
