package org.frameworkset.platform.genproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.Charsets;
import org.apache.log4j.Logger;
import org.frameworkset.runtime.CommonLauncher;
import org.frameworkset.util.io.ClassPathResource;

import com.frameworkset.util.FileUtil;
import com.frameworkset.util.VelocityUtil;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

public class GenDubberGradleService extends GenGradleService {

	private static Logger log = Logger.getLogger(GenService.class);

	protected File project_dubbo_consumer;
	protected File project_dubbo_resources;

	/**
	 * gradle属性文件定义结束
	 */
	public GenDubberGradleService(String projecttype) {
		super(projecttype);
		// TODO Auto-generated constructor stub
	}
	protected void _init()  throws Exception{
		eclipseworkspace = CommonLauncher.getProperty("eclipseworkspace",
				"D:\\testwp");// elipse工程工作目录
		projectname = CommonLauncher.getProperty("projectname", "ptest");// 要生成的工程目录

		projectpath = new File(eclipseworkspace, projectname);
	 
		
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
	public void init() throws Exception {
		_init();
		initGradleProperties();
		
		project_dubbo_interface = new File(projectpath, projectname + "-dubbo-inf");
		project_dubbo = new File(projectpath, projectname + "-dubbo");
		project_dubbo_consumer = new File(projectpath, projectname + "-dubbo-consumer");
		project_dubbo_resources = new File(project_dubbo, "src/test/resources");

		makeProjectStruct(project_dubbo_interface);
		makeProjectStruct(project_dubbo_consumer);
		makeDubboProjectStruct(project_dubbo);

	}

	private void copyDubboFiles() throws Exception {
		log.info("copy " + new File("resources/templates/dubbo/build/project-dubbo/").getAbsolutePath() + " to "
				+ project_dubbo.getAbsolutePath());

		FileUtil.copy(new File("resources/templates/dubbo/build/project-dubbo/"), project_dubbo.getAbsolutePath());
		log.info("copy " + new File("resources/templates/dubbo/build/project-dubbo-inf/").getAbsolutePath() + " to "
				+ project_dubbo_interface.getAbsolutePath());

		FileUtil.copy(new File("resources/templates/dubbo/build/project-dubbo-inf/"),
				project_dubbo_interface.getAbsolutePath());

		log.info(
				"copy " + new File("demo/project-dubbo/").getAbsolutePath() + " to " + project_dubbo.getAbsolutePath());

		FileUtil.copy(new File("demo/project-dubbo/"), project_dubbo.getAbsolutePath());
		log.info("copy " + new File("demo/project-dubbo-inf/").getAbsolutePath() + " to "
				+ project_dubbo_interface.getAbsolutePath());

		FileUtil.copy(new File("demo/project-dubbo-inf/"), project_dubbo_interface.getAbsolutePath());

		log.info("copy " + new File("demo/project-dubbo-consumer/").getAbsolutePath() + " to "
				+ project_dubbo_interface.getAbsolutePath());

		FileUtil.copy(new File("demo/project-dubbo-consumer/"), project_dubbo_consumer.getAbsolutePath());
	}

	private void makeProjectStruct(File moduleProject) {
		if (!moduleProject.exists())
			moduleProject.mkdirs();
		File dir = new File(moduleProject, "src/main/java");
		dir.mkdirs();
		dir = new File(moduleProject, "src/main/resources");
		dir.mkdirs();

		dir = new File(moduleProject, "src/test/java");
		dir.mkdirs();
		dir = new File(moduleProject, "src/test/resources");
		dir.mkdirs();

	}

	private void makeDubboProjectStruct(File moduleProject) {
		if (!moduleProject.exists())
			moduleProject.mkdirs();
		File dir = new File(moduleProject, "src/main/java");
		dir.mkdirs();
		dir = new File(moduleProject, "src/main/resources");
		dir.mkdirs();

		dir = new File(moduleProject, "src/test/java");
		dir.mkdirs();
		dir = new File(moduleProject, "src/test/resources");
		dir.mkdirs();

		dir = new File(moduleProject, "runfiles");
		dir.mkdirs();

	}

	public void gen() {

		try {
			init();

			genProject();

		} catch (Exception e) {
			log.error("生成工程失败", e);
		}
	}

	private void copydepenglibs() throws IOException {
		File dir = new File("resources/templates/" + projecttype + "/lib/");
		if(dir.exists()){
			File[] compilejars = new File("resources/templates/" + projecttype + "/lib/").listFiles(new FilenameFilter() {
	
				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					if (name.endsWith(".jar")) {
						return true;
					}
					return false;
				}
	
			});
			for (int i = 0; compilejars != null && i < compilejars.length; i++) {
				FileUtil.fileCopy(compilejars[i], new File(this.projectlib, compilejars[i].getName()));
	
			}
		}

		dir = new File("resources/templates/" + projecttype + "/lib-compile/");
		if(dir.exists()){
			File[] compilejars = new File("resources/templates/" + projecttype + "/lib-compile/").listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					if (name.endsWith(".jar")) {
						return true;
					}
					return false;
				}
	
			});
			for (int i = 0; compilejars != null && i < compilejars.length; i++) {
				FileUtil.fileCopy(compilejars[i], new File(this.projectlibcompilepath, compilejars[i].getName()));
	
			}
		}
		// ClassPathResource resource = new
		// ClassPathResource("templates/lib-compile/javaee.jar");
		// resource.savetofile(new File(this.projectlibcompilepath,
		// "javaee.jar"));
		// resource = new
		// ClassPathResource("templates/lib-compile/junit-4.6.jar");
		// resource.savetofile(new File(this.projectlibcompilepath,
		// "junit-4.6.jar"));
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
			out = new FileOutputStream(new File(this.project_dubbo_resources, "dbcp.xml"));
			writer = new OutputStreamWriter(out, Charsets.UTF_8);
			antbuild.merge(context, writer);
			writer.flush();
			ClassPathResource resource = new ClassPathResource("templates/resources/poolman.xml");
			resource.savetofile(new File(this.project_dubbo_resources, "poolman.xml"));

		} catch (Exception e) {
			log.error("生成数据库连接池配置文件失败：", e);
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

	private void genantProjectBuildfile() {
		Writer writer = null;
		OutputStream out = null;
		try {
			// 生成ant构建属性文件
			Template gradleproperties = VelocityUtil.getTemplate(projecttype + "/build/gradle.properties");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelpropertiesContext(context);
			out = new FileOutputStream(new File(this.projectpath, "gradle.properties"));
			writer = new OutputStreamWriter(out, Charsets.UTF_8);
			// writer = new FileWriter(new File(this.projectpath,
			// "build.properties"));
			gradleproperties.merge(context, writer);
			writer.flush();
			writer.close();

			Template gradlebuild = VelocityUtil.getTemplate(projecttype + "/build/build.gradle");
			context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelbuildContext(context);
			out = new FileOutputStream(new File(this.projectpath, "build.gradle"));
			writer = new OutputStreamWriter(out, Charsets.UTF_8);
			// writer = new FileWriter(new File(this.projectpath,
			// "build.properties"));
			gradlebuild.merge(context, writer);
			writer.flush();
			writer.close();

			Template gradlesetting = VelocityUtil.getTemplate(projecttype + "/build/settings.gradle");
			context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelbuildContext(context);
			out = new FileOutputStream(new File(this.projectpath, "settings.gradle"));
			writer = new OutputStreamWriter(out, Charsets.UTF_8);
			// writer = new FileWriter(new File(this.projectpath,
			// "build.properties"));
			gradlesetting.merge(context, writer);
			writer.flush();
			writer.close();

		} catch (Exception e) {
			log.error("生成gradle构建文件失败：", e);
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
		genModuleProjectBuildfile(projecttype + "/build/project-common/build.gradle", project_common);

	}

	private void genModuleProjectBuildfile(String templatepath, File buildfile) {
		Writer writer = null;
		OutputStream out = null;
		try {
			// 生成ant构建属性文件
			Template gradle = VelocityUtil.getTemplate(templatepath);
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelModuleBuildContext(context);
			out = new FileOutputStream(new File(buildfile, "build.gradle"));
			writer = new OutputStreamWriter(out, Charsets.UTF_8);
			// writer = new FileWriter(new File(this.projectpath,
			// "build.properties"));
			gradle.merge(context, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("生成gradle构建文件失败：", e);
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

	private void genDubboRuntimeFiles() {
		Writer writer = null;
		OutputStream out = null;
		String templatepath = projecttype + "/build/project-dubbo/";
		File buildfile = this.project_dubbo;
		try {

			// 生成ant构建属性文件
			Template gradle = VelocityUtil.getTemplate(templatepath + "runfiles/setup.sh");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelModuleBuildContext(context);
			out = new FileOutputStream(new File(buildfile, "runfiles/setup.sh"));
			writer = new OutputStreamWriter(out, Charsets.UTF_8);
			// writer = new FileWriter(new File(this.projectpath,
			// "build.properties"));
			gradle.merge(context, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("生成gradle构建文件失败：", e);
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
			Template gradle = VelocityUtil.getTemplate(templatepath + "runfiles/setup.bat");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelModuleBuildContext(context);
			out = new FileOutputStream(new File(buildfile, "runfiles/setup.bat"));
			writer = new OutputStreamWriter(out, Charsets.UTF_8);
			// writer = new FileWriter(new File(this.projectpath,
			// "build.properties"));
			gradle.merge(context, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("生成gradle构建文件失败：", e);
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
			Template gradle = VelocityUtil.getTemplate(templatepath + "runfiles/config.properties");
			VelocityContext context = new VelocityContext();// VelocityUtil.buildVelocityContext(context)
			setGradelModuleBuildContext(context);
			out = new FileOutputStream(new File(buildfile, "runfiles/config.properties"));
			writer = new OutputStreamWriter(out, Charsets.UTF_8);
			// writer = new FileWriter(new File(this.projectpath,
			// "build.properties"));
			gradle.merge(context, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("生成gradle构建文件失败：", e);
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

	private void gendubboAPIModuleProjectBuildfile() {
		genModuleProjectBuildfile(projecttype + "/build/project-dubbo-inf/build.gradle", project_dubbo_interface);

	}

	private void gendubboModuleProjectBuildfile() {
		genModuleProjectBuildfile(projecttype + "/build/project-dubbo/build.gradle", project_dubbo);

	}

	private void gendubboConsumerModuleProjectBuildfile() {
		genModuleProjectBuildfile(projecttype + "/build/project-dubbo-consumer/build.gradle", project_dubbo_consumer);

	}

	private void genGradleBuildFiles() {
		genantProjectBuildfile();
		gendubboAPIModuleProjectBuildfile();
		gendubboModuleProjectBuildfile();
		gendubboConsumerModuleProjectBuildfile();
	}

	private void genProject() throws Exception {

		this.copyDubboFiles();
		genDubboRuntimeFiles();

		genGradleBuildFiles();
		try{
			copydepenglibs();
		}
		catch(Exception e){
			
		}
		gendbpoolfile();

	}
}
