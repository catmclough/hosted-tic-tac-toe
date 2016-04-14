package javaserver.responders;

import java.util.Arrays;
import javaserver.Request;
import javaserver.Response;

public interface Responder {
	public Response getResponse(Request request);
	public String getStatusLine(Request request);

	default public boolean requestIsSupported(String[] supportedMethods, String method) {
		return Arrays.asList(supportedMethods).contains(method);
	}

  default Response createResponse(Request request) {
    return new Response.ResponseBuilder(getStatusLine(request))
      .build();
  }
}
