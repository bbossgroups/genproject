package org.frameworkset.platform.genproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.ZipException;

import org.apache.commons.io.Charsets;
import org.apache.log4j.Logger;
import org.frameworkset.runtime.CommonLauncher;
import org.frameworkset.util.io.ClassPathResource;

import com.frameworkset.util.FileUtil;
import com.frameworkset.util.VelocityUtil;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

public class GenGradleService  extends BaseGen{

	private static Logger log = Logger.getLogger(GenService.class);
	
	protected File project_common; 
	protected File project_service_interface;	
	protected File project_service_impl;
	protected File project_dubbo_interface;	
	protected File project_dubbo;
	protected File project_web;
	protected File project_web_webrootpath;
	protected File project_web_resources;
	
	protected File project_service_server_webrootpath;
	protected File project_service_server_resources;
	
	protected File project_service_server;
	
	/**
	 * gradle属性文件定义开始
	 */
	protected String PROJ_GROUP="com.wowo";
	protected String PROJ_VERSION="1.0.0";
	protected String jacksonversion="2.8.1";
	protected String bboss_version="5.0.1";
	protected String bboss_pdp_version="4.10.9";
	protected String bboss_wordpdf_version="4.10.9";
	protected String bbossplugin_version="4.10.9";
	protected String WF_VERSION="5.12";
	protected String mongodbversion="3.2.2";
	protected String bboss_rpc_version="4.10.9";
	
	protected String enabledubbo="false";
	protected String enablepinpoint="false";
	protected String traceagent="-javaagent:F:/6_environment/pinpoint-agent-1.6.1/pinpoint-bootstrap-1.6.1.jar -Dpinpoint.agentId=dubbo160_1 -Dpinpoint.applicationName=dubbo-test";
	 

	protected String skipTest="true";
	protected String PROJ_WEBSITEURL="http://www.bbossgroups.com";
	protected String PROJ_ISSUETRACKERURL="https://github.com/bbossgroups/bboss-cms/issues";
	protected String PROJ_VCSURL="svn://139.224.19.207/trunk/";
	protected String PROJ_DESCRIPTION="wowo base on bboss public development platform base on bboss.";
	
	protected String DEVELOPER_ID="yin-bp";
	protected String DEVELOPER_NAME="biaoping.yin";
	protected String DEVELOPER_EMAIL="yin-bp@163.com";
	protected String proxysonatype_repository_url="http://139.224.19.207:8881/repository/maven-public/";
	protected String proxysonatype_url="http://139.224.19.207:8881/repository/maven-releases/";
	protected String proxysonatype_username="admin";
	protected String proxysonatype_password="admin123";
	protected String scm_connection="scm:svn://139.224.19.207/trunk/";
	protected String scm_developerConnection="scm:svn://139.224.19.207/trunk/";
	protected String scm_url="svn://139.224.19.207/trunk/";
	protected String uploadArchivesToMavenCenter="false";
	protected String Implementation_Vendor="wowo";
	protected String SECURITY_PROJ_VERSION = "5.0.2.4";
	protected String projecttype;
	/**
	 * gradle属性文件定义结束
	 */
	public GenGradleService(String projecttype) {
		this.projecttype = projecttype;
		// TODO Auto-generated constructor stub
	}
	protected void initGradleProperties()
	{
		PROJ_GROUP=CommonLauncher.getProperty("PROJ_GROUP","com.wowo");
		PROJ_VERSION=CommonLauncher.getProperty("PROJ_VERSION","1.0.0");
		jacksonversion=CommonLauncher.getProperty("jacksonversion","2.8.5");
		bboss_version = CommonLauncher.getProperty("bboss_version","5.0.2.4");
		bboss_pdp_version = CommonLauncher.getProperty("bboss_pdp_version","5.0.1");
		bboss_wordpdf_version = CommonLauncher.getProperty("bboss_wordpdf_version","4.10.9");
		bbossplugin_version =  CommonLauncher.getProperty("bbossplugin_version","4.10.9");
		WF_VERSION = CommonLauncher.getProperty("WF_VERSION","5.12.1");
		mongodbversion = CommonLauncher.getProperty("mongodbversion","3.2.2");
		bboss_rpc_version = CommonLauncher.getProperty("bboss_rpc_version","4.10.9");
		
		enabledubbo = CommonLauncher.getProperty("enabledubbo","false");
		enablepinpoint = CommonLauncher.getProperty("enablepinpoint","false");
		traceagent = CommonLauncher.getProperty("traceagent");
		 

		skipTest = CommonLauncher.getProperty("skipTest","true");
		PROJ_WEBSITEURL = CommonLauncher.getProperty("PROJ_WEBSITEURL","http://www.bbossgroups.com");
		PROJ_ISSUETRACKERURL = CommonLauncher.getProperty("PROJ_ISSUETRACKERURL","https://github.com/bbossgroups/bboss-cms/issues");
		PROJ_VCSURL = CommonLauncher.getProperty("PROJ_VCSURL","svn://139.224.19.207/trunk/");
		PROJ_DESCRIPTION = CommonLauncher.getProperty("PROJ_DESCRIPTION","wowo base on bboss public development platform base on bboss.");
		
		DEVELOPER_ID = CommonLauncher.getProperty("DEVELOPER_ID","yin-bp");
		DEVELOPER_NAME = CommonLauncher.getProperty("DEVELOPER_NAME","biaoping.yin");
		DEVELOPER_EMAIL = CommonLauncher.getProperty("DEVELOPER_EMAIL","yin-bp@163.com");
		proxysonatype_repository_url = CommonLauncher.getProperty("proxysonatype_repository_url","http://139.224.19.207:8881/repository/maven-public/");
		proxysonatype_url = CommonLauncher.getProperty("proxysonatype_url","http://139.224.19.207:8881/repository/maven-releases/");
		proxysonatype_username = CommonLauncher.getProperty("proxysonatype_username","admin");
		proxysonatype_password = CommonLauncher.getProperty("proxysonatype_password","admin123");
		scm_connection = CommonLauncher.getProperty("scm_connection","scm:svn://139.224.19.207/trunk/");
		scm_developerConnection = CommonLauncher.getProperty("scm_developerConnection","scm:svn://139.224.19.207/trunk/");
		scm_url = CommonLauncher.getProperty("scm_url","svn://139.224.19.207/trunk/");
		uploadArchivesToMavenCenter = CommonLauncher.getProperty("uploadArchivesToMavenCenter","false");
		Implementation_Vendor = CommonLauncher.getProperty("Implementation_Vendor","wowo");
		
		SECURITY_PROJ_VERSION  = CommonLauncher.getProperty("SECURITY_PROJ_VERSION","5.0.2.4");
	}
	
	public void init() throws Exception {
		super.init();
		initGradleProperties();
		
		project_common = new File(projectpath, projectname+"-common");
		project_service_interface = new File(projectpath, projectname+"-api");	
		project_service_impl = new File(projectpath, projectname+"-service");
		project_dubbo_interface = new File(projectpath, projectname+"-dubbo-inf");	
		project_dubbo = new File(projectpath, projectname+"-dubbo");
		project_web = new File(projectpath, projectname+"-web");
		project_service_server = new File(projectpath, projectname+"-service-server");
		projectlibcompilepath = new File(projectpath, "lib-compile");
		if (!projectlibcompilepath.exists())
			projectlibcompilepath.mkdirs();
		projectlib = new File(projectpath, "lib");
		if (!projectlib.exists())
			projectlib.mkdirs();
		makeProjectStruct(project_common);
		makeProjectStruct(project_service_interface);
		if(this.enabledubbo.equals("true")){
			makeProjectStruct(project_dubbo_interface);			
			makeDubboProjectStruct(project_dubbo);
		}
		makeProjectStruct(project_service_impl);
		makeWebProjectStruct(project_web);
		makeServiceServerProjectStruct(project_service_server);
		

	}
	
	private void copyDubboFiles() throws Exception {
		log.info("copy "+new File("resources/templates/gradle2/build/project-dubbo/").getAbsolutePath() + " to "+project_dubbo.getAbsolutePath());
		
		FileUtil.copy(new File("resources/templates/gradle2/build/project-dubbo/"),
				project_dubbo.getAbsolutePath());
		log.info("copy "+new File("resources/templates/gradle2/build/project-dubbo-inf/").getAbsolutePath() + " to "+project_dubbo_interface.getAbsolutePath());
		
		FileUtil.copy(new File("resources/templates/gradle2/build/project-dubbo-inf/"),
				project_dubbo_interface.getAbsolutePath());
		
		
		log.info("copy "+new File("demo/project-dubbo/").getAbsolutePath() + " to "+project_dubbo.getAbsolutePath());
		
		FileUtil.copy(new File("demo/project-dubbo/"),
				project_dubbo.getAbsolutePath());
		log.info("copy "+new File("demo/project-dubbo-inf/").getAbsolutePath() + " to "+project_dubbo_interface.getAbsolutePath());
		
		FileUtil.copy(new File("demo/project-dubbo-inf/"),
				project_dubbo_interface.getAbsolutePath());
	}
	private void makeProjectStruct(File moduleProject)
	{
		if (!moduleProject.exists())
			moduleProject.mkdirs();
		File dir = new File(moduleProject,"src/main/java");
		dir.mkdirs();
		dir = new File(moduleProject,"src/main/resources");
		dir.mkdirs();
		
		dir = new File(moduleProject,"src/test/java");
		dir.mkdirs();
		dir = new File(moduleProject,"src/test/resources");
		dir.mkdirs();
		
				
	}
	
	private void makeDubboProjectStruct(File moduleProject)
	{
		if (!moduleProject.exists())
			moduleProject.mkdirs();
		File dir = new File(moduleProject,"src/main/java");
		dir.mkdirs();
		dir = new File(moduleProject,"src/main/resources");
		dir.mkdirs();
		
		dir = new File(moduleProject,"src/test/java");
		dir.mkdirs();
		dir = new File(moduleProject,"src/test/resources");
		dir.mkdirs();
		
		dir = new File(moduleProject,"runfiles");
		dir.mkdirs();
		
				
	}
	
	private void makeWebProjectStruct(File moduleProject)
	{
		if (!moduleProject.exists())
			moduleProject.mkdirs();
		File dir = new File(moduleProject,"src/main/java");
		dir.mkdirs();
		dir = new File(moduleProject,"src/main/resources");
		dir.mkdirs();
		project_web_resources = dir;
		
		dir = new File(moduleProject,"src/main/webapp");
		dir.mkdirs();
		project_web_webrootpath = dir;
		
		
		dir = new File(moduleProject,"src/test/java");
		dir.mkdirs();
		dir = new File(moduleProject,"src/test/resources");
		dir.mkdirs();
				
	}
	
	private void makeServiceServerProjectStruct(File moduleProject)
	{
		if (!moduleProject.exists())
			moduleProject.mkdirs();
		File dir = new File(moduleProject,"src/main/java");
		dir.mkdirs();
		dir = new File(moduleProject,"src/main/resources");
		dir.mkdirs();
		this.project_service_server_resources = dir;
		 
		dir = new File(moduleProject,"src/main/webapp");
		dir.mkdirs();
		project_service_server_webrootpath = dir;
		
		
		dir = new File(moduleProject,"src/test/java");
		dir.mkdirs();
		dir = new File(moduleProject,"src/test/resources");
		dir.mkdirs();
				
	}
	
	public void gen() {
		
		try {
			init();
			unzipArchs();
			copyresources();
			genProject();			
			initDB();
		} catch (ZipException e) {
			log.error("解压失败",e);
		} catch (Exception e) {
			log.error("生成工程失败",e);
		}
	}
	
	private void copydepenglibs() throws IOException
	{
		File[] compilejars = new File("resources/templates/"+projecttype+"/lib/").listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				if( name.endsWith(".jar"))
				{
					return true;
				}
				return false;
			}
			
		});
		for(int i = 0; i < compilejars.length; i ++)
		{
			FileUtil.fileCopy(compilejars[i], new File(this.projectlib, compilejars[i].getName()));
			
		}

		compilejars = new File("resources/templates/"+projecttype+"/lib-compile/").listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				if( name.endsWith(".jar"))
				{
					return true;
				}
				return false;
			}

		});
		for(int i = 0; i < compilejars.length; i ++)
		{
			FileUtil.fileCopy(compilejars[i], new File(this.projectlibcompilepath, compilejars[i].getName()));

		}
//		ClassPathResource resource = new ClassPathResource("templates/lib-compile/javaee.jar");
//		resource.savetofile(new File(this.projectlibcompilepath, "javaee.jar"));
//		resource = new ClassPathResource("templates/lib-compile/junit-4.6.jar");
//		resource.savetofile(new File(this.projectlibcompilepath, "junit-4.6.jar"));
	}
	

	private void copyresources() throws IOException {
		log.info("copy "+new File(project_web_webrootpath, "WEB-INF/classes").getAbsolutePath() + " to "+this.project_web_resources.getAbsolutePath());
		FileUtil.copy(new File(project_web_webrootpath, "WEB-INF/classes"),
				this.project_web_resources.getAbsolutePath());
		log.info("copy "+new File(project_web_webrootpath, "WEB-INF/classes").getAbsolutePath() + " to "+this.project_service_server_resources.getAbsolutePath());
		FileUtil.copy(new File(project_web_webrootpath, "WEB-INF/classes"),
				this.project_service_server_resources.getAbsolutePath());
		clearClasses(new File(project_web_webrootpath,"WEB-INF/classes"));
	}

	
	private void clearJars(File weblib)
	{
		File[] files = weblib.listFiles();
		for(int i = 0; files != null && i < files.length; i ++)
		{
			File f = files[i];
			if(f.getName().endsWith(".jar"))
				f.delete();
		}
	}
	
	private void clearDubbofiles(File dubboconf)
	{
		File[] files = dubboconf.listFiles();
		for(int i = 0; files != null && i < files.length; i ++)
		{
			File f = files[i];
			if(f.isFile() && f.getName().startsWith("dubbo-"))
				f.delete();
			if(f.isDirectory()){
				clearDubbofiles(f);
			}
			
		}
	}
	private void clearClasses(File classdir)
	{
		FileUtil.deleteFile(classdir);
	}
	private void clearResourcesOfServiceServer()
	{
		File[] deletefiles = project_service_server_webrootpath.listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				 return !name.equals("druid") 
						 && !name.equals("include") 
						 && !name.equals("monitor")
						 && !name.equals("WEB-INF")
						 && !name.equals("welcome.html");
			}
			
		});
		
		for(int i = 0; deletefiles != null && i < deletefiles.length; i ++)
		{
			FileUtil.removeFileOrDirectory(deletefiles[i]);
		}
		
		deletefiles = new File(project_service_server_webrootpath,"WEB-INF/conf").listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				 return !name.equals("bboss-mvc.xml");
			}
			
		});
		
		for(int i = 0; deletefiles != null && i < deletefiles.length; i ++)
		{
			FileUtil.removeFileOrDirectory(deletefiles[i]);
		}
		
		
		
		
	}
	private void unzipArchs() throws  Exception {
		handleArches();
		FileUtil.unzip(war, project_web_webrootpath.getAbsolutePath());
		
		File weblib = new File(project_web_webrootpath,"WEB-INF/lib");
		clearJars( weblib);
		if(!this.enabledubbo.equals("true"))
			clearDubbofiles(new File(project_web_webrootpath,"WEB-INF/conf"));
		
		FileUtil.unzip(war,project_service_server_webrootpath.getAbsolutePath());
		weblib = new File(project_service_server_webrootpath,"WEB-INF/lib");
		
		clearJars( weblib);
		clearClasses(new File(project_service_server_webrootpath,"WEB-INF/classes"));
		clearResourcesOfServiceServer();
		if(!this.enabledubbo.equals("true"))
			clearDubbofiles(new File(project_service_server_webrootpath,"WEB-INF/conf"));
		FileUtil.fileCopy(new File("resources/templates/"+projecttype+"/build/project-service-server/web.xml"), new File(project_service_server_webrootpath,"WEB-INF/web.xml"));
		FileUtil.unzip(this.db_init_tool,
				this.projectdbinitpath.getAbsolutePath());
	}
	

	private void gendbpoolfile() {

		Writer writer = null;
		OutputStream out = null;
		try {
			// 生成ant构建属性文件
			Template antbuild = VelocityUtil.getTemplate("resources/dbcp.xml");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)

			context.put("driverClassName", this.driverClassName);
			context.put("url", this.url);
			context.put("username", this.username);
			context.put("password", this.password);
			context.put("validationQuery", this.validationQuery);
			out = new FileOutputStream(new File(this.project_web_resources,
					"dbcp.xml"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);
			antbuild.merge(context, writer);
			writer.flush();
			ClassPathResource resource = new ClassPathResource(
					"templates/resources/poolman.xml");
			resource.savetofile(new File(this.project_web_resources,
					"poolman.xml"));
			FileUtil.copy(new File(this.project_web_resources, "dbcp.xml"),
					new File(this.projectdbinitpath, "resources")
							.getAbsolutePath());
			FileUtil.copy(new File(this.project_web_resources, "poolman.xml"),
					new File(this.projectdbinitpath, "resources")
							.getAbsolutePath());
			
			resource.savetofile(new File(this.project_service_server_resources,
					"poolman.xml"));
			FileUtil.copy(new File(this.project_service_server_resources, "dbcp.xml"),
					new File(this.projectdbinitpath, "resources")
							.getAbsolutePath());
			FileUtil.copy(new File(this.project_service_server_resources, "poolman.xml"),
					new File(this.projectdbinitpath, "resources")
							.getAbsolutePath());
		} catch (Exception e) {
			log.error("生成数据库连接池配置文件失败：",e);
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
	protected void setGradelpropertiesContext(VelocityContext context)
	{
		context.put("PROJ_GROUP",PROJ_GROUP);
		context.put("PROJ_VERSION",PROJ_VERSION);
		context.put("jacksonversion",jacksonversion);
		context.put("bboss_version",bboss_version);
		context.put("bboss_pdp_version",bboss_pdp_version);
		context.put("bboss_wordpdf_version",bboss_wordpdf_version);
		context.put("bbossplugin_version",bbossplugin_version);		
		context.put("WF_VERSION",WF_VERSION);
		context.put("mongodbversion",mongodbversion);
		context.put("bboss_rpc_version",bboss_rpc_version);

		
		context.put("enabledubbo",enabledubbo);
		context.put("enablepinpoint",enablepinpoint);
		context.put("traceagent",traceagent);
		 
		
		context.put("skipTest",skipTest);
		context.put("PROJ_WEBSITEURL",PROJ_WEBSITEURL);
		context.put("PROJ_ISSUETRACKERURL",PROJ_ISSUETRACKERURL);
		context.put("PROJ_VCSURL",PROJ_VCSURL);
		context.put("PROJ_DESCRIPTION",PROJ_DESCRIPTION);
		
		context.put("DEVELOPER_ID",DEVELOPER_ID);
		context.put("DEVELOPER_NAME",DEVELOPER_NAME);
		context.put("DEVELOPER_EMAIL",DEVELOPER_EMAIL);
		
		context.put("proxysonatype_repository_url",proxysonatype_repository_url);
		context.put("proxysonatype_url",proxysonatype_url);
		context.put("proxysonatype_username",proxysonatype_username);
		context.put("proxysonatype_password",proxysonatype_password);
		context.put("scm_connection",scm_connection);
		context.put("scm_developerConnection",scm_developerConnection);
		context.put("scm_url",scm_url);
		context.put("uploadArchivesToMavenCenter",uploadArchivesToMavenCenter);
		
		context.put("Implementation_Vendor",Implementation_Vendor);
		context.put("SECURITY_PROJ_VERSION",SECURITY_PROJ_VERSION);
		
	}
	
	protected void setGradelbuildContext(VelocityContext context)
	{
		context.put("enabledubbo",enabledubbo);
		context.put("enablepinpoint",enablepinpoint);
		context.put("traceagent",traceagent);
		
		context.put("projectname",projectname);
		
	}
	
	protected void setGradelModuleBuildContext(VelocityContext context)
	{
		context.put("enabledubbo",enabledubbo);
		context.put("enablepinpoint",enablepinpoint);
		context.put("traceagent",traceagent);	
		context.put("projectname",projectname);
		context.put("PROJ_GROUP",PROJ_GROUP);
		
	}
	private void genantProjectBuildfile() {
		Writer writer = null;
		OutputStream out = null;
		try {
			// 生成ant构建属性文件
			Template gradleproperties = VelocityUtil
					.getTemplate(projecttype+"/build/gradle.properties");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelpropertiesContext( context);
			out = new FileOutputStream(new File(this.projectpath,
					"gradle.properties"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);
//			writer = new FileWriter(new File(this.projectpath,
//					"build.properties"));
			gradleproperties.merge(context, writer);
			writer.flush();
			writer.close();
			
			Template gradlebuild = VelocityUtil
					.getTemplate(projecttype+"/build/build.gradle");
			context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelbuildContext(  context);
			out = new FileOutputStream(new File(this.projectpath,
					"build.gradle"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);
//			writer = new FileWriter(new File(this.projectpath,
//					"build.properties"));
			gradlebuild.merge(context, writer);
			writer.flush();
			writer.close();
			

			Template gradlesetting = VelocityUtil
					.getTemplate(projecttype+"/build/settings.gradle");
			context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelbuildContext(  context);
			out = new FileOutputStream(new File(this.projectpath,
					"settings.gradle"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);
//			writer = new FileWriter(new File(this.projectpath,
//					"build.properties"));
			gradlesetting.merge(context, writer);
			writer.flush();
			writer.close();
			
			
		} catch (Exception e) {
			log.error("生成gradle构建文件失败：",e);
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
	
	private void genantCommonModuleProjectBuildfile() {
		genModuleProjectBuildfile(projecttype+"/build/project-common/build.gradle",project_common ) ;
		
	}
	
	private void genModuleProjectBuildfile(String templatepath,File buildfile ) {
		Writer writer = null;
		OutputStream out = null;
		try {
			// 生成ant构建属性文件
			Template gradle = VelocityUtil
					.getTemplate(templatepath);
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelModuleBuildContext(  context);
			out = new FileOutputStream(new File(buildfile,
					"build.gradle"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);
//			writer = new FileWriter(new File(this.projectpath,
//					"build.properties"));
			gradle.merge(context, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("生成gradle构建文件失败：",e);
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
	
	private void genDubboRuntimeFiles( ) {
		Writer writer = null;
		OutputStream out = null;
		String templatepath = projecttype+"/build/project-dubbo/";
		File buildfile = this.project_dubbo;
		try {
			
			// 生成ant构建属性文件
			Template gradle = VelocityUtil
					.getTemplate(templatepath+"runfiles/setup.sh");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelModuleBuildContext(  context);
			out = new FileOutputStream(new File(buildfile,
					"runfiles/setup.sh"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);
//			writer = new FileWriter(new File(this.projectpath,
//					"build.properties"));
			gradle.merge(context, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("生成gradle构建文件失败：",e);
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
		
		try {
			// 生成ant构建属性文件
			Template gradle = VelocityUtil
					.getTemplate(templatepath+"runfiles/setup.bat");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelModuleBuildContext(  context);
			out = new FileOutputStream(new File(buildfile,
					"runfiles/setup.bat"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);
//			writer = new FileWriter(new File(this.projectpath,
//					"build.properties"));
			gradle.merge(context, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("生成gradle构建文件失败：",e);
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
		
		try {
			// 生成ant构建属性文件
			Template gradle = VelocityUtil
					.getTemplate(templatepath+"runfiles/config.properties");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelModuleBuildContext(  context);
			out = new FileOutputStream(new File(buildfile,
					"runfiles/config.properties"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);
//			writer = new FileWriter(new File(this.projectpath,
//					"build.properties"));
			gradle.merge(context, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("生成gradle构建文件失败：",e);
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
	private void genantAPIModuleProjectBuildfile() {
		genModuleProjectBuildfile(projecttype+"/build/project-api/build.gradle",project_service_interface );
		
	}
	private void gendubboAPIModuleProjectBuildfile() {
		genModuleProjectBuildfile(projecttype+"/build/project-dubbo-inf/build.gradle",project_dubbo_interface );
		
	}
	private void gendubboModuleProjectBuildfile() {
		genModuleProjectBuildfile(projecttype+"/build/project-dubbo/build.gradle",project_dubbo );
		
	} 
	 
	private void genantServiceServerModuleProjectBuildfile() {
		genModuleProjectBuildfile(projecttype+"/build/project-service-server/build.gradle",this.project_service_server );
		
	}
	
	private void genantServiceModuleProjectBuildfile() {
		genModuleProjectBuildfile(projecttype+"/build/project-service/build.gradle",this.project_service_impl );
		
	}
	private void genantWebModuleProjectBuildfile() {
		genModuleProjectBuildfile(projecttype+"/build/project-web/build.gradle",this.project_web);
		
	}
	private void genGradleBuildFiles()
	{
		genantProjectBuildfile();
		genantAPIModuleProjectBuildfile();
		genantCommonModuleProjectBuildfile();
		genantServiceModuleProjectBuildfile() ;
		genantServiceServerModuleProjectBuildfile();
		genantWebModuleProjectBuildfile();
		if(this.enabledubbo.equals("true")){
			gendubboAPIModuleProjectBuildfile();
			gendubboModuleProjectBuildfile();
		}
	}
	private void genProject() throws Exception {
		if(this.enabledubbo.equals("true")){
			this.copyDubboFiles();
			genDubboRuntimeFiles();
		}
		genGradleBuildFiles();
		
		copydepenglibs();
		gendbpoolfile();

	}
}
