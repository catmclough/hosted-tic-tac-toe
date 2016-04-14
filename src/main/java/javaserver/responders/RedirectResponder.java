package javaserver.responders;

import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.Response;

public class RedirectResponder implements Responder {
	private String[] supportedMethods;
	private String defaultRedirectLocation = "http://localhost:5000/";
	private String redirectHeader = "Location: ";

	public RedirectResponder(String[] supportedMethods) {
		this.supportedMethods = supportedMethods;
	}

	@Override
	public Response getResponse(Request request) {
      return new Response.ResponseBuilder(getStatusLine(request))
        .header(getResponseHeader(request))
        .build();
	}

	@Override
	public String getStatusLine(Request request) {
		if (requestIsSupported(supportedMethods, request.getMethod())) {
			return HTTPStatusCode.THREE_OH_TWO.getStatusLine();
		} else {
			return HTTPStatusCode.FOUR_OH_FOUR.getStatusLine();
		}
	}

	private String getResponseHeader(Request request) {
		String header = new String();
		if (requestIsSupported(supportedMethods, request.getMethod())) {
		  header += redirectHeader + defaultRedirectLocation;
		}
		return header;
	}
}
