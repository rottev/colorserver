package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import org.cc.colorlib.server.ColorServericeImpl;
import org.cc.colorlib.server.ColorService;
import org.cc.colorlib.server.ServerStart;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcClient;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.JsonRpcServer;
import com.googlecode.jsonrpc4j.ProxyUtil;
import com.googlecode.jsonrpc4j.StreamServer;


public class ServerTest {
	
	private static JsonRpcServer server;
	private static JsonRpcHttpClient  client;
	private static StreamServer streamServer;
	private static JsonRpcClient  jsonRpcClient;
	
	/*
	@Before
	public void setup() {
		ColorService colorService = new ColorServericeImpl();
		server = new JsonRpcServer(new ObjectMapper(), colorService);
		try {
			streamServer = new StreamServer(server, 50, ServerStart.SERVICE_PORT,
			        50, InetAddress.getByName("127.0.0.1"));
			streamServer.start();
		
			jsonRpcClient = new JsonRpcClient();
			
		//	client = new JsonRpcHttpClient(new URL("http://127.0.0.1:" + ServerStart.SERVICE_PORT + "/ColorService.json"));
			
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	*/
	
	@BeforeClass
	public static void setUpClass() {
	    //executed only once, before the first test
		ColorService colorService = new ColorServericeImpl();
		server = new JsonRpcServer(new ObjectMapper(), colorService);
		try {
			streamServer = new StreamServer(server, 50, ServerStart.SERVICE_PORT,
			        50, InetAddress.getByName("127.0.0.1"));
			streamServer.start();
		
			jsonRpcClient = new JsonRpcClient();
			
		//	client = new JsonRpcHttpClient(new URL("http://127.0.0.1:" + ServerStart.SERVICE_PORT + "/ColorService.json"));
			
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCheckInvalidColor() throws UnknownHostException, IOException {
		
		Socket socket = new Socket( InetAddress.getByName("127.0.0.1"), ServerStart.SERVICE_PORT);
		
		ColorService userService = ProxyUtil.createClientProxy(
				this.getClass().getClassLoader(), ColorService.class,
				jsonRpcClient, socket);
		
		try
		{
			boolean isColor = userService.isColor("notacolor", 0);
			assertEquals(isColor, false);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Test
	public void testCheckValidColor() throws UnknownHostException, IOException {
		
		Socket socket = new Socket( InetAddress.getByName("127.0.0.1"), ServerStart.SERVICE_PORT);
		
		ColorService userService = ProxyUtil.createClientProxy(
				this.getClass().getClassLoader(), ColorService.class,
				jsonRpcClient, socket);
		
		try
		{
			boolean isColor = userService.isColor("6e154972db38e359ccf51738386711d5e29618208765dee1dfef69022c809341", 0);
			assertEquals(isColor, true);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Test
	public void testCheckColorValue() throws UnknownHostException, IOException {
		
		Socket socket = new Socket( InetAddress.getByName("127.0.0.1"), ServerStart.SERVICE_PORT);
		
		ColorService userService = ProxyUtil.createClientProxy(
				this.getClass().getClassLoader(), ColorService.class,
				jsonRpcClient, socket);
		
		try
		{
			double value = userService.getColorValue("6e154972db38e359ccf51738386711d5e29618208765dee1dfef69022c809341", 0);
			assertEquals(value, 2000, 0.0001);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	
	@Test
	public void testCheckColorValueEx() throws UnknownHostException, IOException {
		
		Socket socket = new Socket( InetAddress.getByName("127.0.0.1"), ServerStart.SERVICE_PORT);
		
		ColorService userService = ProxyUtil.createClientProxy(
				this.getClass().getClassLoader(), ColorService.class,
				jsonRpcClient, socket);
		
		try
		{
			String value = userService.getColorValueEx(null,"6e154972db38e359ccf51738386711d5e29618208765dee1dfef69022c809341", 0);
			System.out.println(value);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@AfterClass
	public static void afterClass() {
		try {
			streamServer.stop();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
