package org.frameworkset.platform.genproject;

import java.io.File;

import org.frameworkset.runtime.CommonLauncher;


public class GenProject {
	private static File appdir;
	public GenProject() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Gen service = null;
		String projecttype = CommonLauncher.getProperty("projecttype", "gradle2");
		if(projecttype.equals("ant"))
			service = new GenService();
		else if(projecttype.equals("gradle") || projecttype.equals("gradle2"))
			service = new GenGradleService(projecttype);
		else  if(projecttype.equals("dubbo") )
			service = new GenDubberGradleService(projecttype);
		service.setApprootdir(appdir);
		
		service.setProjecttype(projecttype);
		service.gen();
		service.clean();
		
	}
	
	public static void setAppdir(File appdir) {
		GenProject.appdir = appdir;
	}
	

}
