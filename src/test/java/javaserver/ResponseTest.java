package javaserver;

import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;

public class ResponseTest {

    private Response mockResponse() {
        String twoHundred = HTTPStatusCode.TWO_HUNDRED.getStatusLine();
        return new Response.ResponseBuilder(twoHundred)
                .header("Some header.")
                .body("Some content")
                .build();
    }

    private String formatResponseProperly(String statusLine, String headers, String body) {
       return statusLine + System.lineSeparator() + headers + System.lineSeparator() + System.lineSeparator() + body + System.lineSeparator();
    }

    @Test
    public void testResponseFormat() {
        String properlyFormattedResponse = formatResponseProperly(mockResponse().getResponseCode(), mockResponse().getHeader(), mockResponse().getBody());
        assertTrue(Arrays.equals(mockResponse().formatResponse(), properlyFormattedResponse.getBytes()));
    }
}
