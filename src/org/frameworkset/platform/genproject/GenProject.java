package org.frameworkset.platform.genproject;

import java.io.File;


public class GenProject {
	private static File appdir;
	public GenProject() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		GenService service = new GenService();
		service.setApprootdir(appdir);
		service.init();
		service.gen();
		service.clean();
		
	}
	
	public static void setAppdir(File appdir) {
		GenProject.appdir = appdir;
	}
	

}
