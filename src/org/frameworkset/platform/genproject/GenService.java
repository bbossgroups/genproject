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
import org.frameworkset.util.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.util.FileUtil;
import com.frameworkset.util.VelocityUtil;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

public class GenService extends BaseGen{
	private static Logger log = LoggerFactory.getLogger(GenService.class);
	protected File projectresourcepath;
	protected File projectwebrootpath;
	protected File projectwebrootlibpath;
	protected File projectsrcpath;
	protected File projectsrctestpath;
	public GenService() {
		// TODO Auto-generated constructor stub
	}

	public void init() throws Exception {
		super.init();
		projectwebrootpath = new File(projectpath, "WebRoot");
		projectwebrootlibpath = new File(projectpath, "WebRoot/WEB-INF/lib");
		projectresourcepath = new File(projectpath, "resources");
		projectlibcompilepath = new File(projectpath, "lib-compile");
		projectsrcpath  = new File(projectpath, "src");
		projectsrctestpath  = new File(projectpath, "src-test");
		
		
		if (!projectwebrootpath.exists())
			projectwebrootpath.mkdirs();
		if (!projectresourcepath.exists())
			projectresourcepath.mkdirs();
		if (!projectlibcompilepath.exists())
			projectlibcompilepath.mkdirs();
		if (!projectsrcpath.exists())
			projectsrcpath.mkdirs();
		if (!projectsrctestpath.exists())
			projectsrctestpath.mkdirs();
		
		

	}
	
	public void gen() {
		
		try {
			init();
			unzipArchs();
			copyresources();
			genProject();
			gentomcatdeploy();
			
			initDB();
		} catch (ZipException e) {
			log.error("解压失败",e);
		} catch (Exception e) {
			log.error("生成工程失败",e);
		}
	}
	private void gentomcatdeploy()
	{
		Writer writer = null;
		OutputStream out = null;
		try {
			// 生成ant构建属性文件
			Template antbuild = VelocityUtil.getTemplate("ant/tomcat-deploy.xml");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			context.put("projectname", this.projectname);
			context.put("projectwebrootpath", this.projectwebrootpath);
			out = new FileOutputStream(new File(this.projectpath, this.projectname+".xml"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);	
//			writer = new FileWriter(new File(this.projectpath, this.projectname+".xml"));
			antbuild.merge(context, writer);
			writer.flush();
			
		} catch (Exception e) {
			log.error("生成tomcat部署文件失败",e);
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
	
	private void copydepenglibs(final StringBuilder classpath) throws IOException
	{
		File[] compilejars = new File("resources/templates/lib-compile/").listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				if( name.endsWith(".jar"))
				{
					classpath.append("	<classpathentry kind=\"lib\" path=\"lib-compile/").append(name).append("\"/>\r\n");
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
		FileUtil.copy(new File(projectwebrootpath, "WEB-INF/classes"),
				this.projectresourcepath.getAbsolutePath());
	}

	
	
	private void unzipArchs() throws  Exception {
		handleArches();
		FileUtil.unzip(war, projectwebrootpath.getAbsolutePath());
		FileUtil.unzip(this.db_init_tool,
				this.projectdbinitpath.getAbsolutePath());
	}
	

	private void getClasspathInfo(final StringBuilder classpath )
	{
		
		this.projectwebrootlibpath.listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				
				if( name.endsWith(".jar"))
					classpath.append("	<classpathentry kind=\"lib\" path=\"WebRoot/WEB-INF/lib/").append(name).append("\"/>\r\n");
				return false;
			}
			
		});
		classpath.append("	<classpathentry kind=\"output\" path=\"WebRoot/WEB-INF/classes\"/>\r\n");
		
	}
	private void genjavaprojectfile() {

		Writer writer = null;
		OutputStream out = null;
		try {
			// 生成ant构建属性文件
			Template antbuild = VelocityUtil.getTemplate("project/.project");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			context.put("project", this.projectname);
			out = new FileOutputStream(new File(this.projectpath, ".project"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);			 
			antbuild.merge(context, writer);
			writer.flush();
//			ClassPathResource resource = new ClassPathResource(
//					"templates/project/.classpath");
//			resource.savetofile(new File(this.projectpath, ".classpath"));
			StringBuilder classpath = new StringBuilder();	
			classpath.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n")
			.append("<classpath>\r\n")
			.append("	<classpathentry kind=\"src\" path=\"src\"/>\r\n")
			.append("	<classpathentry kind=\"src\" path=\"src-test\"/>\r\n")
			.append("	<classpathentry kind=\"src\" path=\"resources\"/>\r\n")
			.append("	<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/>\r\n");
			copydepenglibs(classpath);
			getClasspathInfo( classpath );
			classpath.append("</classpath>");
			FileUtil.writeFile(new File(this.projectpath, ".classpath"), classpath.toString(),"UTF-8");

		} catch (Exception e) {
			log.error("生成java工程文件失败：",e);
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
			out = new FileOutputStream(new File(this.projectresourcepath,
					"dbcp.xml"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);
			antbuild.merge(context, writer);
			writer.flush();
			ClassPathResource resource = new ClassPathResource(
					"templates/resources/poolman.xml");
			resource.savetofile(new File(this.projectresourcepath,
					"poolman.xml"));
			FileUtil.copy(new File(this.projectresourcepath, "dbcp.xml"),
					new File(this.projectdbinitpath, "resources")
							.getAbsolutePath());
			FileUtil.copy(new File(this.projectresourcepath, "poolman.xml"),
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

	private void genantbuildfile() {
		Writer writer = null;
		OutputStream out = null;
		try {
			// 生成ant构建属性文件
			Template antbuild = VelocityUtil
					.getTemplate("ant/build.properties");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			context.put("projectname", this.projectname);
			out = new FileOutputStream(new File(this.projectpath,
					"build.properties"));
			writer = new OutputStreamWriter(out,Charsets.UTF_8);
//			writer = new FileWriter(new File(this.projectpath,
//					"build.properties"));
			antbuild.merge(context, writer);
			writer.flush();
			ClassPathResource resource = new ClassPathResource(
					"templates/ant/build.bat");
			resource.savetofile(new File(this.projectpath, "build.bat"));
			resource = new ClassPathResource(
					"templates/ant/build.sh");
			resource.savetofile(new File(this.projectpath, "build.sh"));
			resource = new ClassPathResource("templates/ant/build.xml");
			resource.savetofile(new File(this.projectpath, "build.xml"));
			resource = new ClassPathResource("templates/ant/buildjar.bat");
			resource.savetofile(new File(this.projectpath, "buildjar.bat"));
		} catch (Exception e) {
			log.error("生成ant构建文件失败：",e);
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

	private void genProject() throws IOException {
		genantbuildfile();
		genjavaprojectfile();
		gendbpoolfile();

	}

	
}
