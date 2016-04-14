package javaserver;

public class HTMLContent {

	public static String openHTMLAndBody(String pageTitle) {
		return  "<!DOCTYPE html>\n<html>\n<head>\n<title>" + pageTitle + "</title>\n</head>\n<body>\n";
	}

	public static String listOfLinks(String[] list) {
		String unorderedList = "<ul>" + System.lineSeparator();
		for (String listItem : list) {
			unorderedList += "<li><a href=\"/" + listItem + "\">" + listItem + "</a></li>" + System.lineSeparator();
		}
		unorderedList += "</ul>" + System.lineSeparator();
		return unorderedList;
	}

	public static String closeBodyAndHTML() {
		return "</body>\n</html>";
	}
}
