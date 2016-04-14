package javaserver.responders;

import java.io.File;
import javaserver.App;
import javaserver.HTMLContent;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.Response;

public class DirectoryResponder implements Responder {
	private String[] supportedMethods;
	private File directory;
	private String htmlHeader = "Content-Type: text/html;";

	public DirectoryResponder(String[] supportedMethods, File publicDir) {
		this.supportedMethods = supportedMethods;
		this.directory = publicDir;
	}

	@Override
	public Response getResponse(Request request) {
        return new Response.ResponseBuilder(getStatusLine(request))
          .header(htmlHeader)
          .body(getDirectoryLinksHTML())
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

	private String getDirectoryLinksHTML() {
		return HTMLContent.openHTMLAndBody(App.name) + HTMLContent.listOfLinks(directory.list()) + HTMLContent.closeBodyAndHTML();
	}
}
