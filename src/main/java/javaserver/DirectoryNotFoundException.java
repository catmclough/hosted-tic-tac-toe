package javaserver;

public class DirectoryNotFoundException extends Exception {
	/**
    * Generated serial ID.
	 */
	private static final long serialVersionUID = -5356379314394533804L;

	public DirectoryNotFoundException(String directoryName) {
		super("The directory " + directoryName + " was not found.");
	}
}
