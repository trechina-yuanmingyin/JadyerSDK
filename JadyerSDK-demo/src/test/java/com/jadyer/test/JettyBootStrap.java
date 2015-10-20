package com.jadyer.test;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyBootStrap {
	private int port = 80;
	private String appenv_active = "dev";
	private String context_path = "/";
	private String webapp_path = "F:/Tool/Code/JavaSE/MyJettyDemo/WebRoot";
	
	public static void main(String[] args) {
		//new JettyBootStrap(8080, "/", "dev").run();
		new JettyBootStrap().run();
	}


	public JettyBootStrap() {
		String webappPath = getClass().getClassLoader().getResource(".").getFile();
		this.webapp_path = webappPath.substring(0, webappPath.indexOf("target")) + "src/main/webapp";
	}


	public JettyBootStrap(int port, String context_path, String env) {
		String webappPath = getClass().getClassLoader().getResource(".").getFile();
		this.webapp_path = webappPath.substring(0, webappPath.indexOf("target")) + "src/main/webapp";
		this.appenv_active = env;
		this.port = port;
		this.context_path = context_path;
	}
	
	
	private void log(long time) {
		System.err.println();
		System.out.println("*****************************************************************");
		System.err.println("[INFO] Server running in " + time + "ms at http://127.0.0.1" + (80==this.port?"":":"+this.port) + this.context_path);
		System.out.println("*****************************************************************");
	}


	private void run(){
		long beginTime = System.currentTimeMillis();
		System.setProperty("appenv.active", this.appenv_active);
		Server server = createServer(this.port, this.context_path, this.webapp_path);
		try {
			server.start();
			this.log(System.currentTimeMillis() - beginTime);
		} catch (Exception e) {
			System.err.println("Jetty启动失败,堆栈轨迹如下");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	

	/**
	 * 创建用于开发运行调试的JettyServer
	 * @param port        访问服务器的端口
	 * @param contextPath 访问服务器的地址
	 * @param webAppPath  Web应用的目录(需指向到WebRoot目录下)
	 */
	private static Server createServer(int port, String contextPath, String webAppPath){
		Server server = new Server();
		server.setStopAtShutdown(true);
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		connector.setReuseAddress(false);
		server.setConnectors(new Connector[]{connector});
		//为了设置reuseAddress=false所以创建Connector,否则直接new Server(port)即可,通过查看Server源码发现,二者是等效的
		//不过使用Connector的好处是可以让Jetty监听多个端口,此时创建多个绑定不同端口的Connector即可,最后一起setConnectors
		//Server server = new Server(port);
		//server.setStopAtShutdown(true);
		WebAppContext context = new WebAppContext(webAppPath, contextPath);
		server.setHandler(context);
		return server;
	}
}