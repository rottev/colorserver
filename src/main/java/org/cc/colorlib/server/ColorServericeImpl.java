package org.cc.colorlib.server;

import org.cc.colorlib.core.ColorWallet;

public class ColorServericeImpl implements ColorService{

	private static ColorWallet colorWallet = ColorWallet.getInstance();
	
	public boolean isColor(String txHash, int outindex) {
		return colorWallet.IsTxColor(txHash, outindex);
	}
	
	public double getColorValue(String txHash, int outindex) {
		return colorWallet.getColorValue(txHash, outindex);
	}
	public ColorServericeImpl()
	{
		
	}
	public String getColorValueEx(String colordef,String txHash, int outindex)
	{
		
		return colorWallet.getColorValueAndInfo( colordef, txHash,  outindex);
	}

}
