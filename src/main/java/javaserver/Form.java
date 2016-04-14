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
}
