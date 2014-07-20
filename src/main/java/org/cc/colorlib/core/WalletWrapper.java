package org.cc.colorlib.core;

import java.io.File;

import com.google.bitcoin.core.Wallet;

public class WalletWrapper {
	private Wallet wallet = null;
	private File walletFile = null;
	
	public WalletWrapper(File wFile, Wallet wallet) {
		// TODO Auto-generated constructor stub
		this.wallet = wallet;
		this.walletFile = wFile;
	}
	
	public Wallet getWallet()
	{
		return wallet;
	}
	
	public File getWalletFile()
	{
		return walletFile;
	}

}
