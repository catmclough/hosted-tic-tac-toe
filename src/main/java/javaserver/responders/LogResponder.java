package javaserver.responders;

import java.util.Base64;

import javaserver.App;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.RequestLog;
import javaserver.RequestParser;
import javaserver.Response;

public class LogResponder implements Responder {
    private RequestLog requestLog;
    private String[] supportedMethods;
    private String realm = "Cob Spec Logs";
    private String username = "admin";
    private String password = "hunter2";

	public LogResponder(String[] supportedMethods) {
	    this.supportedMethods = supportedMethods;
	    this.requestLog = App.log;
	}

	@Override
	public Response getResponse(Request request) {
		return new Response.ResponseBuilder(getStatusLine(request))
		    .header(getBasicAuthHeader())
		    .body(getLog(request))
            .build();
	}

	@Override
	public String getStatusLine(Request request) {
	    if (requestIsSupported(supportedMethods, request.getMethod())  && isAuthorized(request)) {
	        return HTTPStatusCode.TWO_HUNDRED.getStatusLine();
	    } else {
            return HTTPStatusCode.FOUR_OH_ONE.getStatusLine();
	    }
	}

	private boolean isAuthorized(Request request) {
	    return request.getHeaders().containsKey("Authorization") && hasValidCredentials(RequestParser.getCodedCredentials(request)); 
	}

	private String getBasicAuthHeader() {
	   return "WWW-Authenticate: Basic realm=\"" + realm + "\"";
	}

	private String getLog(Request request) {
	    String body = "";
	    if (requestIsSupported(supportedMethods, request.getMethod()) && isAuthorized(request)) {
	        body += requestLog.getLogContents();
	    }
	    return body;
	}

	private boolean hasValidCredentials(String codedCredentials) {
	    byte[] decodedCredentials = Base64.getMimeDecoder().decode(codedCredentials.getBytes());
	    String credentials = new String(decodedCredentials);
	    return credentials.equals(username + ":" + password);
	}
}
