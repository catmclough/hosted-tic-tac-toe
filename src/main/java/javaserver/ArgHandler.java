package javaserver;

import java.util.Arrays;

public class ArgHandler {
	private static final String PORT_FLAG = "-P";
	private static final String DIRECTORY_FLAG = "-D";

	public static int getPort(String[] args, int defaultPort) {
		int portFlagIndex = Arrays.asList(args).indexOf(PORT_FLAG);
		if (portFlagIndex >= 0)
			try {
				return Integer.parseInt(args[portFlagIndex + 1]);
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				return defaultPort;
			}
		else {
			return defaultPort;
		}
	}

	public static String getDirectory(String[] args, String defaultDirectory) {
	    if (ArgHandler.argsHaveValidDirectoryChoice(args)) {
	        return ArgHandler.getChosenDirectory(args);
	    } else {
	        return defaultDirectory;
	    }
	}

	private static boolean argsHaveValidDirectoryChoice(String[] args) {
		int publicDirectoryFlagIndex = Arrays.asList(args).indexOf(DIRECTORY_FLAG);
		if (publicDirectoryFlagIndex >= 0) {
			try {
			    return args[publicDirectoryFlagIndex + 1] != null;
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	private static String getChosenDirectory(String[] args) {
		int publicDirectoryFlagIndex = Arrays.asList(args).indexOf(DIRECTORY_FLAG);
	    return args[publicDirectoryFlagIndex + 1];
	}

}
