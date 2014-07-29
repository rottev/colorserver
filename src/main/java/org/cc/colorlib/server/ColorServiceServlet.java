package org.cc.colorlib.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.jsonrpc4j.JsonRpcServer;


public class ColorServiceServlet  extends HttpServlet {

    private ColorService colorService;
    private JsonRpcServer jsonRpcServer;

	public void init() {
		jsonRpcServer = new JsonRpcServer(new ColorServericeImpl());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		jsonRpcServer.handle(req, resp);
	}

}
