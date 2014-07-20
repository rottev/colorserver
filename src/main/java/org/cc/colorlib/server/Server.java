package org.cc.colorlib.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcServer;
import com.googlecode.jsonrpc4j.StreamServer;

public class Server {

    private static ColorServericeImpl colorService;
    public static int SERVICE_PORT = 9091;
    
	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		colorService = new ColorServericeImpl();
		JsonRpcServer server = new JsonRpcServer(new ObjectMapper(), colorService);
		StreamServer streamServer = new StreamServer(server, 50, SERVICE_PORT,
		        50, InetAddress.getByName("127.0.0.1"));
		streamServer.start();
	}

}
