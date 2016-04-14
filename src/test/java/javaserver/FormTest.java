package javaserver;

import static org.junit.Assert.*;

import org.junit.Test;

public class FormTest {

    @Test
    public void testFormWithStringData() {
        String data = "some string";
        Form stringDataForm = new Form(data);
        assertEquals(stringDataForm.getData(), data);
    }

}
