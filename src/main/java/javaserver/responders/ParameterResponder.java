package javaserver.responders;

import java.io.UnsupportedEncodingException;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.Response;

public class ParameterResponder implements Responder {
	private String[] supportedMethods;

	public ParameterResponder(String[] supportedMethods) {
		this.supportedMethods = supportedMethods;
	}

	@Override
	public Response getResponse(Request request) {
		return new Response.ResponseBuilder(getStatusLine(request))
          .body(decodedParameterBody(request))
          .build();
	}

	@Override
	public String getStatusLine(Request request) {
		if (requestIsSupported(supportedMethods, request.getMethod())) {
			return HTTPStatusCode.TWO_HUNDRED.getStatusLine();
		} else {
			return HTTPStatusCode.FOUR_OH_FOUR.getStatusLine();
		}
	}

	private String decodedParameterBody(Request request) {
		String body = "";
		for (String parameterVar : splitParameters(request.getURI())) {
			body += decodeParameter(parameterVar) + System.lineSeparator();
		}
		return body;
	}

	public String[] splitParameters(String uri) {
		String query = getParams(uri);
		query = query.replace("=", " = ");
		return query.split("&");
	}

	public String decodeParameter(String parameterLine) {
		try {
			String encoding = "UTF-8";
			parameterLine = java.net.URLDecoder.decode(parameterLine, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("ParameterHandler could not decode one or more of the request's parameters");
		}
		return parameterLine;
	}

	private String getParams(String uri) {
		String[] routeParts = uri.split("\\?", 2);
		return routeParts[1];
	}
}
