package javaserver;

import static org.junit.Assert.*;
import org.junit.Test;
import javaserver.responders.ErrorResponder;
import javaserver.responders.FormResponder;
import javaserver.responders.ParameterResponder;
import javaserver.responders.Responder;

public class RoutesTest {

    @Test
    public void testCreatesErrorResponderForUnknownRoute() {
        String unknownRoute = "/foobar";
        Responder responder = Routes.getResponder(unknownRoute);
        assertEquals(responder.getClass(), ErrorResponder.class);
    }
    
    @Test
    public void testCreatesErrorResponderForUnknownRouteWithParams() {
        String unknownRouteWithParams = "/foobar?var1=xyz";
        Responder responder = Routes.getResponder(unknownRouteWithParams);
        assertEquals(responder.getClass(), ErrorResponder.class);
    }

    @Test
    public void testCreatesErrorResponderForUnknownRouteWithPartialRange() {
        String unknownFileWithPartial = "/foobar\nRange: bytes=0-10";
        Responder responder = Routes.getResponder(unknownFileWithPartial);
        assertEquals(responder.getClass(), ErrorResponder.class);
    }

	@Test
	public void testFormResponderCreation() {
	    String formRoute = "/form";
	    Responder responder = Routes.getResponder(formRoute);
	    assertEquals(responder.getClass(), FormResponder.class);
	}

	@Test
	public void testCreatesFormWithEmptyDataLine() {
	    String formRoute = "/form";
	    Responder responder = Routes.getResponder(formRoute);
	    Request getForm = RequestParser.createRequest("GET " + formRoute);
	    assertTrue(responder.getResponse(getForm).getBody().isEmpty());
	}

	@Test
	public void testCreatesParameterResponder() {
	   String simpleParamRoute = "/parameters"; 
	   Responder responder = Routes.getResponder(simpleParamRoute);
	   assertEquals(responder.getClass(), ParameterResponder.class);
	}

}
