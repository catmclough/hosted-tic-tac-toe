package javaserver.responders;

import javaserver.Form;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.Response;

public class FormResponder implements Responder {
	private String[] supportedMethods;
	private Form form;

	public FormResponder(String[] supportedMethods, Form form) {
		this.supportedMethods = supportedMethods;
		this.form = form;
	}

	@Override
	public Response getResponse(Request request) {
		return new Response.ResponseBuilder(getStatusLine(request))
          .body(getData(request))
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

	private String getData(Request request) {
		if (requestChangesData(request)) {
			form = new Form(request.getData());
		}
		return form.getData();
	}

	private boolean requestChangesData(Request request) {
		return (request.getMethod().equals("POST") || request.getMethod().equals("PUT"))
				|| request.getMethod().equals("DELETE");
	}
}
