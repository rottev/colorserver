package org.cc.colorlib.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.cc.colorlib.core.ColorWallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcServer;
import com.googlecode.jsonrpc4j.StreamServer;

public class ServerStart {

    private static ColorServericeImpl colorService;
    public static int SERVICE_PORT = 9091;
    
	public static void main(String[] args) throws Exception {		
		ColorWallet.start();
		ServletEngine engine = new ServletEngine();
		engine.startup();
	}

}
