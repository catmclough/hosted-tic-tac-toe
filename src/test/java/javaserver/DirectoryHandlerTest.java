package javaserver;

import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.Test;

public class DirectoryHandlerTest {

    @Test
    public void testRecognizesExistingDirectory() throws IOException {
        try {
            DirectoryHandler.createPublicDirectory(App.DEFAULT_PUBLIC_DIRECTORY);
        } catch (DirectoryNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(DirectoryHandler.getPublicDirectoryPath(), App.DEFAULT_PUBLIC_DIRECTORY);
    }

    @Test
    public void testRecognizesExistingDirectoryWithFormatIssue() {
        boolean errorCaught = false;
        try {
            DirectoryHandler.createPublicDirectory("/public");
        } catch (DirectoryNotFoundException e) {
            errorCaught = true;
        }
        assertFalse(errorCaught);
        assertEquals(DirectoryHandler.getPublicDirectoryPath(), "public/");
    }

    @Test
    public void testInvalidDirectoryThrowsDirectoryNotFound() throws IOException {
        boolean errorCaught = false;
        try {
            DirectoryHandler.createPublicDirectory("non-existant-directory");
        } catch (DirectoryNotFoundException e) {
            errorCaught = true;
        }
        assertTrue(errorCaught);
    }
}
