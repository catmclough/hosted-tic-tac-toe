package javaserver;

import java.util.HashMap;

public class RequestParser {

	public static Request createRequest(String rawRequest) {
		String requestMethod = getRequestMethod(rawRequest);
		String requestURI = getRequestURI(rawRequest);
		HashMap<String, String> requestHeaders = getRequestHeaders(rawRequest);
		String requestData = getRequestData(rawRequest);
		return new Request(requestMethod, requestURI, requestHeaders, requestData);
	}

	public static String getRequestMethod(String rawRequest) {
		return rawRequest.split("\\s")[0];
	}

	public static String getRequestURI(String rawRequest) {
		try {
			return rawRequest.split("\\s")[1].trim();
		} catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}

	public static HashMap<String, String> getRequestHeaders(String rawRequest) {
	   String[] requestLines = rawRequest.split(System.lineSeparator());
	   HashMap<String, String> headers = new HashMap<String, String>();
	   for (int i = 1; i < requestLines.length; i++) {
	      String[] headerParts = requestLines[i].split(Headers.SEPARATOR);
	      if (headerParts != null) {
	          if (Headers.KNOWN_HEADERS.contains(headerParts[0]))
                 headers.put(headerParts[0].trim(), headerParts[1].trim());
	      }
	   }
	   return headers;
	}

	public static String getRequestData(String rawRequest) {
	   String[] requestLines = rawRequest.split(System.lineSeparator());
	   String data = "";
	   for (int i = 1; i < requestLines.length; i++) {
	       boolean lineIsHeader = false;
	       for (String header : Headers.KNOWN_HEADERS) {
	           if (requestLines[i].startsWith(header))
	                lineIsHeader = true;
	       }
	       if (!lineIsHeader)
	           data += requestLines[i].trim();
	   }
	   return data;
	}

	public static String getCodedCredentials(Request request) {
	    return request.getHeaders().get("Authorization").split("Basic")[1].trim();
	}

	public static String getURIWithoutParams(String uri) {
		if (requestHasParams(uri)) {
			String[] routeParts = uri.split("\\?", 2);
			return routeParts[0];
		} else {
			return uri;
		}
	}

	private static boolean requestHasParams(String uri) {
		return uri.contains("?");
	}

	public static String getImageFormat(Request request) {
	    try {
           return request.getURI().split("\\.")[1];
	    } catch (ArrayIndexOutOfBoundsException e) {
	       System.out.println("Request has no image format extension.");
	       return "";
	    }
	}
}
