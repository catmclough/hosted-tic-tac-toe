package javaserver;

import static org.junit.Assert.*;
import org.junit.Test;
import javaserver.responders.ErrorResponder;
import javaserver.responders.FormResponder;
import javaserver.responders.ParameterResponder;
import javaserver.responders.Responder;

public class RoutesTest {

    @Test
    public void testCreatesFormResponderForRootDirectory() {
        String rootDirectory = "/";
        Responder responder = Routes.getResponder(rootDirectory);
        assertEquals(responder.getClass(), FormResponder.class);
    }
}
