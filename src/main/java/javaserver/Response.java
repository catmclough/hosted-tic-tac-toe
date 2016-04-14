package javaserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Response {

	private String statusLine;
	private String header;
	private String body;
	private byte[] bodyData;
    private String newLine = System.lineSeparator();


	private Response(ResponseBuilder builder) {
		this.statusLine = builder.statusLine;
		this.header = builder.header;
		this.body = builder.body;
		this.bodyData = builder.bodyData;
	}

	public String getResponseCode() {
		return statusLine;
	}

	public String getHeader() {
		return header;
	}

	public String getBody() {
		return body;
	}

	public byte[] getBodyData() {
	   return bodyData;
	}

	public byte[] formatResponse() {
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String output = statusLine + newLine;
		if (header != null)
            output += header + newLine;
		output += newLine;
		if (body != null)
            output += body + newLine;

		try {
            outputStream.write(output.getBytes());
            if (bodyData != null)
                outputStream.write(bodyData);
        } catch (IOException e) {
            System.out.println("Unable to write response body to ByteArrayOutputStream.");
            e.printStackTrace();
        }
		return outputStream.toByteArray();
	}

	public static class ResponseBuilder {
		protected String statusLine;
		protected String header;
		protected String body;
		protected byte[] bodyData;

		public ResponseBuilder(String statusLine) {
			this.statusLine = statusLine;
		}

		public ResponseBuilder statusLine(String statusLine) {
			this.statusLine = statusLine;
			return this;
		}

		public ResponseBuilder header(String header) {
			this.header = header;
			return this;
		}

		public ResponseBuilder body(String body) {
			this.body = body;
			return this;
		}

		public ResponseBuilder body(byte[] bodyData) {
			this.bodyData = bodyData;
			return this;
		}

		public Response build() {
			return new Response(this);
		}
	}
}
