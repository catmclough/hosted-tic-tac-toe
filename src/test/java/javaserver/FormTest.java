package javaserver;

import static org.junit.Assert.*;

import org.junit.Test;

public class FormTest {

    @Test
    public void testFormWithStringData() {
        String data = "some string";
        Form form = new Form(data);
        assertEquals(form.getData(), data);
    }

    @Test
    public void testFormWithStringCollectionData() {
        String[] data = new String[] {"some string", "some other string", "this is data"};
        Form form = new Form(data);
        assertEquals(form.getDataCollection(), data);
    }
}
