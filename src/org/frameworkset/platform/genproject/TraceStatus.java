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
	boolean ended = false;
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
					builder.append(newsize).append("/").append(url.getTotalsize()).append(",Percent:").append(formater.format(rate));
					oldput = builder.toString();
					System.out.print("download file "+this.dest.getName()+" from "+url.getURL() + " to "+dest.getParent()+"\r\nDownload Size(Bytes):"+oldput);
					
					 
					first = false;
				}
				else
				{
					
					 
					if(oldsavesize !=  newsize)
					{
						oldsavesize  = newsize;			 
						StringBuilder builder = new StringBuilder();
						int oldlen = oldput.length();
						for(int i = 0 ; i < oldlen; i ++)
							System.out.print("\b");
						float rate = (float) (newsize*1.0 / url.getTotalsize());
						
						builder.append(newsize).append("/").append(url.getTotalsize()).append(",Percent:").append(formater.format(rate));
						int div = oldlen - builder.length();
						if(div >  0)
						{
							for(int i = 0; i < div; i ++)
								builder.append(" ");
						}
						oldput = builder.toString();
						System.out.print(oldput);		
						
					 
						
					}
				}
				
				if(complete || ended)
				{
					System.out.println();
					break;
				}
				else
				{
					Thread.sleep(1000);
				}
				
			} catch (IOException e) {							
				log.error("下载文件失败：",e);
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
		
		ended = true;
		synchronized(this)
		{
			this.notifyAll();
		}
	}
	
}
