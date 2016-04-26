package javaserver;

public class Form {
	private String data ;
	private String[] dataCollection ;

	public Form(String initialData) {
		this.data = initialData;
	}

	public Form(String[] initialData) {
		this.dataCollection = initialData;
	}

	public String getData() {
		return this.data;
	}

	public String[] getDataCollection() {
		return this.dataCollection;
	}

  public String getHTML() {
    String formHTML = HTMLContent.openHTMLAndBody("Game Board");
    formHTML += "hi!";
    formHTML += HTMLContent.closeBodyAndHTML();
    System.out.println("HTML:\n" + formHTML);
    return formHTML;
  }
}
