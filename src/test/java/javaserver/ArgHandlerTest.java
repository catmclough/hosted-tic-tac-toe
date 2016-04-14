package javaserver;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArgHandlerTest {
   private int defaultPort = 5623;
   private String defaultPubDirectory = "cool_directory/";

   @Test
   public void testGetsChosenPort() {
      String[] args = new String[] {"-D", "zoobeezoo", "-P", "1234"};
      assertEquals(ArgHandler.getPort(args, defaultPort), 1234);
   }

   @Test
   public void testReturnsDefaultPortWithNoArgs() {
      String[] args = new String[] {};
      assertEquals(ArgHandler.getPort(args, defaultPort), defaultPort);
   }

   @Test
   public void testReturnsDefaultPortWithInvalidArgs() {
      String[] args = new String[] {"-P", "zoobeezoo"};
      assertEquals(ArgHandler.getPort(args, defaultPort), defaultPort);
   }

   @Test
   public void testGetsChosenDirectory() {
      String[] args = new String[] {"-D", "zoobeezoo/", "-P", "1234"};
      assertEquals(ArgHandler.getDirectory(args, defaultPubDirectory), "zoobeezoo/");
   }

   @Test
   public void testReturnsDefaultDirectoryWithNoArgs() {
      String[] args = new String[] {};
      assertEquals(ArgHandler.getDirectory(args, defaultPubDirectory), defaultPubDirectory);
   }

   @Test
   public void testReturnsDefaultDirectoryWithInvalidArgs() {
      String[] args = new String[] {"-D"};
      assertEquals(ArgHandler.getDirectory(args, defaultPubDirectory), defaultPubDirectory);
   }
}
