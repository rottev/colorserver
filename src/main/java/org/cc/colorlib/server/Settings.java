package org.cc.colorlib.server;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.cc.colorlib.core.ColorWallet;
import org.cc.colorlib.utils.ApplicationDataDirectoryLocator;

import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.params.TestNet3Params;

public class Settings {
	public static String CONFIGURATION_FILE = "cclibsrv.properties";
	public static String DEFAULT_WALLET_FILENAME = "cclibcolor.wallet";
	public static String CONFIGURATION_TESTNET = "testnet";
	public static String CONFIGURATION_MAINNET = "mainnet";
	
	private PropertiesConfiguration config;
	
	private static Settings instance = null;
	
	
	public static Settings getInstance() {
		 if(instance == null) {
		     synchronized(Settings.class) {
		       if(instance == null) {
		    	   instance = new Settings();
		       }
		    }
		  }
		  return instance;
	   }
	
	private Settings() {
		try {
			ApplicationDataDirectoryLocator path = new ApplicationDataDirectoryLocator();
			File settingsfile = new File(path.getApplicationDataDirectory() + "/" +  CONFIGURATION_FILE);
			if(!settingsfile.exists())
				settingsfile.createNewFile();
			config = new PropertiesConfiguration(settingsfile );
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			config = null;
		} catch(IOException e) {
			e.printStackTrace();
			config = null;
		}
	}
	
	public String getWalletFilename() throws ConfigurationException {
		if(!config.containsKey("walletName")) {
			config.setProperty("walletName", DEFAULT_WALLET_FILENAME);
			config.save();
			return DEFAULT_WALLET_FILENAME;
		}
		else
			return config.getString("walletName");
	}
	
	public String getWalletFilenameFullPath() throws ConfigurationException
	{
		ApplicationDataDirectoryLocator path = new ApplicationDataDirectoryLocator();
		return path.getApplicationDataDirectory() + "/" + getWalletFilename();
	}
	
	public String getNetworkSettings() throws ConfigurationException {
		if(!config.containsKey("network")) {
			config.setProperty("network", CONFIGURATION_TESTNET);
			config.save();
			return CONFIGURATION_TESTNET;
		}
		else
			return config.getString("network");
	}
	
	public boolean isTestNet() throws ConfigurationException {
		if(config == null) {
			return true;
		}
		else {
			if( getNetworkSettings().equals(CONFIGURATION_TESTNET))
				return true;
			else if( getNetworkSettings().equals(CONFIGURATION_MAINNET))
				return false;
		}
		return true;
	}
	
	 public NetworkParameters getNetowrkParams() throws ConfigurationException
	 {
		 return isTestNet() ? TestNet3Params.get() : MainNetParams.get();
	 }
	 
	 public String getIssuerServerUrl()
	 {
		 if(config == null) 
			 return null;
		 else 
			 return config.getString("serverUrl");
	 }
	
}
