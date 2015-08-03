package org.frameworkset.platform.genproject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.frameworkset.util.io.UrlResource;
 

public class TraceStatus extends Thread{
	private static Logger log = Logger.getLogger(TraceStatus.class);
	boolean first = true;
	UrlResource url;
	File dest; 
	long oldsavesize;
	String oldput ;
	private DecimalFormat formater = new DecimalFormat("#.##%");
	public TraceStatus(UrlResource url,File dest)
	{
		this.url= url; 
		this.dest = dest;
	}
 
	public void run() {
		do
		{
			
			try {
				long newsize = url.getSavesize();
				boolean complete = newsize == url.getTotalsize(); 
				if(first)
				{
					float rate = (float) (newsize*1.0 / url.getTotalsize());
					StringBuilder builder = new StringBuilder();
					builder.append(newsize).append("/").append(url.getTotalsize()).append(",").append(formater.format(rate));
					oldput = builder.toString();
					System.out.print("download file "+this.dest.getCanonicalPath()+" from "+url.getURL() + ",process status:"+oldput);
					
					 
					first = false;
				}
				else
				{
					
					 
					if(oldsavesize !=  newsize)
					{
						oldsavesize  = newsize;			 
						StringBuilder builder = new StringBuilder();
						for(int i = 0 ; oldput != null && i < oldput.length(); i ++)
							System.out.print("\b");
						float rate = (float) (newsize*1.0 / url.getTotalsize());
						
						builder.append(newsize).append("/").append(url.getTotalsize()).append(",").append(formater.format(rate)).append("   ");
						oldput = builder.toString();
						System.out.print(oldput);
						
						
					 
						
					}
				}
				
				if(complete )
				{
					System.out.println();
					break;
				}
				else
				{
					Thread.sleep(900);
				}
				
			} catch (IOException e) {							
				
				break;
			} catch (InterruptedException e) {				
					break;
			}
		}while(true);
			
		
	}
	public void refreshprocess()
	{
//		synchronized(this)
//		{
//			this.notifyAll();
//		}
	}
	public void end()
	{
		
		
		synchronized(this)
		{
			this.notifyAll();
		}
	}
	
}
