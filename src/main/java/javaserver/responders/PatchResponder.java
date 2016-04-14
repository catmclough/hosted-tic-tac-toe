package javaserver.responders;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import javaserver.HTTPStatusCode;
import javaserver.Request;
import javaserver.Response;

public class PatchResponder extends FileResponder {

	public PatchResponder(String[] supportedMethods, File publicDir) {
		super(supportedMethods, publicDir);
	}

    public Response createResponse(Request request) {
        updateFileContents(request);
        return new Response.ResponseBuilder(getStatusLine(request))
          .body(getBody(request))
          .build();
    }

    @Override
    public String getStatusLine(Request request) {
        return HTTPStatusCode.TWO_OH_FOUR.getStatusLine();
    }

    private void updateFileContents(Request request) {
       if (etagMatchesFileContent(request))
           patchContentWrittenToFile(request);
    }

    protected boolean etagMatchesFileContent(Request request) {
        String encodedSHA1FileContent = encode(getBody(request).getBytes());
        String etag = request.getHeaders().get("If-Match");
        if (etag != null) {
            return (etag.equals(encodedSHA1FileContent));
        } else {
            return false;
        }
    }

    private String encode(byte[] content) {
        String sha1 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(content);
            sha1 = getHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Could not encode file contents to SHA-1");
        }
        return sha1;
      }

    private String getHex(byte[] hash) {
       Formatter formatter = new Formatter();
       for (byte b : hash) {
          formatter.format("%02x", b);
       }
       String hex = formatter.toString();
       formatter.close();
       return hex;
    }

    private String patchContentWrittenToFile(Request request) {
        File file = new File(directory + request.getURI());
        try {
            Files.write(file.toPath(), request.getData().getBytes());
        } catch (IOException e) {
            System.out.println("Could not write patched content to file.");
            e.printStackTrace();
        }
        return request.getData();
    }
}
