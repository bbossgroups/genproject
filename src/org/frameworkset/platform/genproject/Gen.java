package org.frameworkset.platform.genproject;

import java.io.File;

public interface Gen {
	public void gen() ;
	public void clean();
	public void setApprootdir(File approotdir) ;
	public void setProjecttype(String projecttype) ;
		

}
