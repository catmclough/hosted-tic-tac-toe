package javaserver.responders;

import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.Response;

public class ErrorResponder implements Responder {

	@Override
	public Response getResponse(Request request) {
		return new Response.ResponseBuilder(getStatusLine(request))
          .build();
	}

	@Override
	public String getStatusLine(Request request) {
		return HTTPStatusCode.FOUR_OH_FOUR.getStatusLine();
	}
}
