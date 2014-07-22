package org.cc.colorlib.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;



public class ServletEngine {

	public static final int PORT = 9091;

	Server server;

	public void startup() throws Exception {
		server = new Server(PORT);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		context.addServlet(ColorServiceServlet.class, "/color");
		server.start();
	}

	public void stop() throws Exception {
		server.stop();
	}

}
