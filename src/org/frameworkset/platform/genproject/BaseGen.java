package org.frameworkset.platform.genproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.Charsets;
import org.frameworkset.runtime.CommonLauncher;
import org.frameworkset.util.io.AbstractResource;
import org.frameworkset.util.io.ResourceHandleListener;
import org.frameworkset.util.io.UrlResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.util.FileUtil;
import com.frameworkset.util.VelocityUtil;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

public abstract class BaseGen implements Gen {
	private static Logger log = LoggerFactory.getLogger(GenService.class);	
	protected File approotdir;
	protected String eclipseworkspace;
	protected String projectname;
	protected boolean initdb;
	protected boolean clearproject;
	protected String driverClassName;
	protected String url;
	protected String username;
	protected String password;
	protected String db_init_tool;
	protected String war;
//	protected boolean inited;
	protected File projectarchpath;
	protected String projecttype = "ant";

	protected File projectpath;
	protected File projecttemppath;
	
	protected File projectlibcompilepath;
	protected File projectlib;
	
	protected File projectdbinitpath;
	
	protected String validationQuery;
	public BaseGen() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void gen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clean()
	{
		String cleantemp = CommonLauncher.getProperty("cleantemp", "true");
		if (cleantemp.equals("true") && projecttemppath.exists())
		{
			try {
				log.info("remove temp files:"+projecttemppath.getCanonicalPath());
				FileUtil.removeFileOrDirectory(projecttemppath.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	static class ExceptionContainer{
		Exception war;
		Exception db;
	}
	protected String handleFilename(String filename)
	{
		if(filename == null || filename.trim().length() == 0)
			return filename;
		int idex = filename.indexOf("=");
		if(idex > 0 )
		{
			String[] filenames = filename.split("=");
			return filenames[1];
		}
		else
		{
			return filename;
		}
	}
	protected void handleArches() throws Exception
	{
//		Thread warthread = null;
//		Thread dbthread = null;
		final ExceptionContainer container = new ExceptionContainer();
		if(war == null || war.trim().length() == 0)
		{
			if(projectarchpath != null)
			{
				File files[] =projectarchpath.listFiles();
				if(files != null && files.length > 0)
				{
					for(File f:files)
					{
						if(f.getName().endsWith(".war"))
						{
							this.war = f.getCanonicalPath();
							break;
						}
					}
				}
				log.info("use war file "+war );
			}
		}
		else if(war.startsWith("http://"))
		{
			final UrlResource url = new UrlResource(war);
			try
			{
				Runnable run = new Runnable(){
	
					public void run() {
						try {
	//						System.out.println("download war file from "+war +" starting....");
							
							File tempwar = new File(projecttemppath,handleFilename(url.getFilename()));
						
							
							final TraceStatus traceStatus = new TraceStatus(url,tempwar);
						 
							 
							url.savetofile(tempwar,new ResourceHandleListener<AbstractResource>() {
								
								@Override
								public void startEvent(AbstractResource resource,File dest) {
									
									traceStatus.start(); 
									
								}
								
								@Override
								public void handleDataEvent(AbstractResource resource,File dest) {
									
									traceStatus.refreshprocess();
								}
								
								@Override
								public void endEvent(AbstractResource resource,File dest) {
									traceStatus.end();
									
								}
							});
							traceStatus.join();
	//						System.out.println("download war file from "+war+" sucessed.");
							war = tempwar.getCanonicalPath();
						} catch (Exception e) {
							
							container.war = e;
						}
						
					}
					
				};
				run.run();
			}
			finally
			{
				if(url != null)
					url.release();
			}
		
		}
		else
		{
			log.info("use war file "+war );
		}
		
		if(container.war != null)
			throw container.war ;
		if(db_init_tool == null || db_init_tool.trim().length() == 0)
		{
			if(projectarchpath != null)
			{
				File files[] =projectarchpath.listFiles();
				if(files != null && files.length > 0)
				{
					for(File f:files)
					{
						if(f.getName().endsWith(".zip"))
						{
							this.db_init_tool = f.getCanonicalPath();
							break;
						}
					}
				}
//				db_init_tool = new File(projectarchpath,"dbinit-system.zip").getCanonicalPath();
				log.info("use db_init_tool file "+db_init_tool );
			}
		}
		else if(db_init_tool.startsWith("http://"))
		{
			final 	UrlResource url = new UrlResource(db_init_tool);
			Runnable run = new Runnable(){
				public void run() {
					try {
//						System.out.println("download db_init_tool file from "+db_init_tool +" starting....");
					
						File tempzip = new File(projecttemppath,handleFilename(url.getFilename()));
 
						
						final TraceStatus traceStatus = new TraceStatus(url,tempzip);
					 
						 
						url.savetofile(tempzip,new ResourceHandleListener<AbstractResource>() {
							
							@Override
							public void startEvent(AbstractResource resource,File dest) {
								
								traceStatus.start(); 
								
							}
							
							@Override
							public void handleDataEvent(AbstractResource resource,File dest) {
								
								traceStatus.refreshprocess();
							}
							
							@Override
							public void endEvent(AbstractResource resource,File dest) {
								traceStatus.end();
								
							}
						});
						traceStatus.join();
//						System.out.println("download db_init_tool file from "+db_init_tool+" sucessed.");
						db_init_tool = tempzip.getCanonicalPath(); 
					} catch (Exception e) {
						
						container.db = e;
					}
					
				}
				
			};
			run.run();
			
		}
		else
		{
			log.info("use db_init_tool file "+db_init_tool );
		}
//		if(warthread != null)
//		{
//			try {
//				warthread.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		if(dbthread != null)
//			try {
//				dbthread.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		if(container.db != null)
		{
			
			throw container.db;
		}
		
	}
	
	private void setupdbinitpath(String startuppath,String startupfilename)
	{
		Writer writer = null;
		OutputStream out = null;
		try {
			// 生成ant构建属性文件
			Template antbuild = VelocityUtil.getTemplate(startuppath);
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			String dir = projectdbinitpath.getCanonicalPath();
			String bboss_version = CommonLauncher.getProperty("bboss_version", "5.0.1");
			context.put("bboss_version", bboss_version);
			if (CommonLauncher.isWindows())
			{
//				${dbinitdisk}
				context.put("dbinitdisk",dir.substring(0,dir.indexOf(':')+1));
				 
				context.put("dbinitpath", "cd "+dir);
				
			}
			else
			{
				log.info("cd "+dir);
				context.put("dbinitpath", "cd "+dir + ";");
			}
			
			out = new FileOutputStream(new File(projectdbinitpath, startupfilename));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);	
//			writer = new FileWriter(new File(projectdbinitpath, startupfilename));
			antbuild.merge(context, writer);
			writer.flush();
			
		} catch (Exception e) {
			log.error("设置数据库初始化环境信息失败：",e);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	protected void recoverdbinitpath(String startuppath,String startupfilename)
	{
		Writer writer = null;
		OutputStream out = null;
		try {
			// 生成ant构建属性文件
			Template antbuild = VelocityUtil.getTemplate(startuppath);
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			String bboss_version = CommonLauncher.getProperty("bboss_version", "5.0.1");
			context.put("bboss_version", bboss_version);
			if (CommonLauncher.isWindows())
			{
//				${dbinitdisk}
				context.put("dbinitdisk","");
			}
			 
			context.put("dbinitpath", "");
			out = new FileOutputStream(new File(projectdbinitpath, startupfilename));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);		
//			writer = new FileWriter(new File(projectdbinitpath, startupfilename));
			antbuild.merge(context, writer);
			writer.flush();
			
		} catch (Exception e) {
			log.error("还原数据库初始化环境信息失败：",e);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	protected void chmodx() throws IOException
	{
		Process proc = !CommonLauncher.isOSX()?
				Runtime.getRuntime().exec("chmod +x -R "+
				this.projectpath
						.getCanonicalPath()):
							
				Runtime.getRuntime().exec("chmod -R +x "+
									this.projectpath
											.getCanonicalPath())			;
		StreamGobbler error = new StreamGobbler( proc.getErrorStream(),"ERROR");
		
		StreamGobbler normal = new StreamGobbler( proc.getInputStream(),"NORMAL");
		error.start();
		normal.start();

		try {
			int exitVal = proc.waitFor();
		} catch (InterruptedException e) {
			log.error("授予执行数据库初始化指令执行权限失败：",e);
		}
	}
	protected void initDB() throws IOException {
		if (this.initdb)// 如果需要用执行数据库初始化，则执行以下数据库初始化功能
		{

			try {
				Process proc = null;
				if (CommonLauncher.isWindows()) {
					setupdbinitpath("dbinit/startup.bat","startup.bat");
					proc = Runtime.getRuntime().exec(
							new File(this.projectdbinitpath, "/startup.bat")
									.getCanonicalPath());
//					${dbinitpath}
					
				} 
				else
				{
					chmodx();
					setupdbinitpath("dbinit/startup.sh","startup.sh");
					proc = Runtime.getRuntime().exec("sh "+
							new File(this.projectdbinitpath, "/startup.sh")
									.getCanonicalPath());
				}
				StreamGobbler error = new StreamGobbler( proc.getErrorStream(),"INFO");
				
				StreamGobbler normal = new StreamGobbler( proc.getInputStream(),"NORMAL");
				error.start();
				normal.start();
//				InputStream stderr = proc.getErrorStream();
//				InputStreamReader isr = new InputStreamReader(stderr);
//				BufferedReader br = new BufferedReader(isr);
//				String line = null;
//				System.out.println(" ");
//				while ((line = br.readLine()) != null)
//					System.out.println(line);
//				System.out.println("");
				int exitVal = proc.waitFor();
				log.info("初始化数据库完毕! " );
			} catch (Throwable t) {
				log.error("执行数据库初始化指令失败：",t);
			}
			finally
			{
				if (CommonLauncher.isWindows()) {
//					${dbinitpath}
					recoverdbinitpath("dbinit/startup.bat","startup.bat");
					recoverdbinitpath("dbinit/startup.bat","startup.sh");
				
				} else
				{
					recoverdbinitpath("dbinit/startup.sh","startup.sh");
					recoverdbinitpath("dbinit/startup.bat","startup.bat");
					
				}
			}
		}
	}

	public File getApprootdir() {
		return approotdir;
	}

	public void setApprootdir(File approotdir) {
		this.approotdir = approotdir;
	}
	public static void main(String[] args) throws Exception
	{
//		String dir ="d:/aaa";
//		System.out.println(dir.substring(0,dir.indexOf(':')+1));
//		UrlResource url = new UrlResource("http://nj02.poms.baidupcs.com/file/79aeb2e91792629ca55304fe7356c143?bkt=p2-nj-384&fid=4245631570-250528-570552330597386&time=1438527441&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-zby2yWBB84wb%2B%2FgHnvKrJJHxWwM%3D&to=n2b&fm=Nan,B,G,nc&sta_dx=10&sta_cs=0&sta_ft=zip&sta_ct=0&fm2=Nanjing02,B,G,nc&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=0000c56d3a217528d115fadefe3cded71ec5&sl=82903119&expires=8h&rt=pr&r=636645352&mlogid=796086098&vuk=4245631570&vbdid=1671589608&fin=dbinit-system.zip&fn=dbinit-system.zip&slt=pm&uta=0&rtype=1&iv=0&isw=0");
		UrlResource url = new UrlResource("http://www.bbossgroups.com/tool/download.htm?fileName=bboss.war");
		
		url.savetofile(new File("d:/test.zip"));
	}

	public String getProjecttype() {
		return projecttype;
	}

	public void setProjecttype(String projecttype) {
		this.projecttype = projecttype;
	}
	protected void _init()  throws Exception{
		eclipseworkspace = CommonLauncher.getProperty("eclipseworkspace",
				"D:\\testwp");// elipse工程工作目录
		projectname = CommonLauncher.getProperty("projectname", "ptest");// 要生成的工程目录

		projectpath = new File(eclipseworkspace, projectname);
		projectdbinitpath = new File(projectpath, "dbinit-system");
		
		projecttemppath  = new File(projectpath, "temp");
	
		if(approotdir != null)
			this.projectarchpath = new File(this.approotdir,"arches");
		clearproject = Boolean.parseBoolean(CommonLauncher.getProperty(
				"clearproject", "true"));
		if (clearproject && projectpath.exists())
		{
			log.info("clean old project:"+ projectpath.getCanonicalPath());
			FileUtil.removeFileOrDirectory(projectpath.getCanonicalPath());
		}
		if (!projectpath.exists())
			projectpath.mkdirs();
		if (!projectdbinitpath.exists())
			projectdbinitpath.mkdirs();
		 
		
		if (!projecttemppath.exists())
			projecttemppath.mkdirs();
		 
		
		initdb = Boolean.parseBoolean(CommonLauncher.getProperty("initdb",
				"true"));

		driverClassName = CommonLauncher.getProperty("driverClassName");// 要生成的工程目录
		url = CommonLauncher.getProperty("url");// 要生成的工程目录
		username = CommonLauncher.getProperty("username");// 要生成的工程目录
		password = CommonLauncher.getProperty("password",false);// 要生成的工程目录
		validationQuery = CommonLauncher.getProperty("validationQuery",
				"");// 要生成的工程目录
//		war = CommonLauncher.getProperty("db_init_tool","D:\\temp\\genproject\\arches\\bboss-pdp-web-5.0.1.war");// 要生成的工程目录
//		 db_init_tool = CommonLauncher.getProperty("war","D:\\temp\\genproject\\arches\\dbinit-system-5.0.1.zip");// 要生成的工程目录
		db_init_tool = CommonLauncher.getProperty("db_init_tool");// 要生成的工程目录
		war = CommonLauncher.getProperty("war");// 要生成的工程目录
	}
	protected void init() throws Exception {
		_init();
		

	}

}
