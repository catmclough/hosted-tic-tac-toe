package javaserver.responders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.Response;

public class FileResponder implements Responder {
	protected String[] supportedMethods;
	protected File directory;

	public FileResponder(String[] supportedMethods, File publicDir) {
		this.supportedMethods = supportedMethods;
		this.directory = publicDir;
	}

	@Override
	public Response getResponse(Request request) {
	    if (requestIsSupported(supportedMethods, request.getMethod())) {
            Responder responder = createResponder(request);
            return responder.createResponse(request);
	    } else {
            return new Response.ResponseBuilder(HTTPStatusCode.FOUR_OH_FIVE.getStatusLine())
              .build();
	    }
	}

	private Responder createResponder(Request request) {
		if (request.getHeaders().containsKey("Range")) {
			return new PartialResponder(supportedMethods, directory);
		} else if (request.getMethod().equals("PATCH")) {
		    return new PatchResponder(supportedMethods, directory);
		} else {
		    return this;
		}
	}

	public Response createResponse(Request request) {
        return new Response.ResponseBuilder(getStatusLine(request))
          .body(getBody(request))
          .build();
	}

	@Override
	public String getStatusLine(Request request) {
        return HTTPStatusCode.TWO_HUNDRED.getStatusLine();
	}

	protected String getBody(Request request) {
        File thisFile = new File(directory + request.getURI());
        int fileLength = (int) thisFile.length();
        byte[] fileContents = new byte[fileLength];

        try {
            FileInputStream fileInput = new FileInputStream(thisFile);
            fileInput.read(fileContents);
            fileInput.close();
        } catch (IOException e) {
            System.err.println("Unable to read from file with input stream");
            e.printStackTrace();
        }
        return new String(fileContents);
    }
}
