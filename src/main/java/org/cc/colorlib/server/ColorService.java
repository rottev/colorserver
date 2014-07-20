package org.cc.colorlib.server;

public interface ColorService {
	boolean isColor(String txHash, int outindex);
	double getColorValue(String txHash, int outindex);
	String getColorValueEx(String colordef,String txHash, int outindex);
}
