package org.frameworkset.platform.genproject;


public class GenProject {

	public GenProject() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		GenService service = new GenService();
		service.init();
		service.gen();
		
	}
	

}
