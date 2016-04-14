package javaserver.responders;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.RequestParser;
import javaserver.Response;

public class ImageResponder extends FileResponder {
    private String[] acceptedImageFormats = new String[] {"jpeg", "gif", "png"};
    private boolean imageWasFound = true;

    public ImageResponder(String[] supportedMethods, File publicDirectory) {
        super(supportedMethods, publicDirectory);
    }

    @Override
    public Response getResponse(Request request) {
        return new Response.ResponseBuilder(getStatusLine(request))
          .header(getImageHeaders(request))
          .body(getImageData(request))
          .statusLine(getStatusLine(request))
          .build();
    }

    @Override
    public String getStatusLine(Request request) {
        if (imageWasFound) {
            return HTTPStatusCode.TWO_HUNDRED.getStatusLine();
        } else {
            return HTTPStatusCode.FOUR_OH_FOUR.getStatusLine();
        }
    }

    private String getImageHeaders(Request request) {
       String header = "Content-Length: " + getImageData(request).length + System.lineSeparator()
       + "Content-Type: image/" + getImageFormat(request);
       return header;
    }

    private String getImageFormat(Request request) {
        String format = RequestParser.getImageFormat(request);
        if (!Arrays.asList(acceptedImageFormats).contains(format)) {
            System.err.println("Unsupported image format.");
            this.imageWasFound = false;
        }
        return format;
    }

	protected byte[] getImageData(Request request) {
		File thisFile = new File(directory + request.getURI());
		byte[] fileContents = new byte[0];

		if (requestIsSupported(supportedMethods, request.getMethod())) {
            try {
                fileContents = Files.readAllBytes(thisFile.toPath());
                return fileContents;
            } catch (IOException e1) {
                this.imageWasFound = false;
                System.err.println("Unable to read from file.");
            }
		}
        return fileContents;
	}
}