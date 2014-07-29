package org.cc.colorlib.core;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.configuration.ConfigurationException;
import org.cc.colorlib.core.BaseTrading.Asset;
import org.cc.colorlib.core.BaseTrading.Issuance;
import org.cc.colorlib.server.ColorDownloadListner;
import org.cc.colorlib.server.Settings;

import com.google.bitcoin.core.BlockChain;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.net.discovery.DnsDiscovery;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.params.TestNet3Params;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.MemoryBlockStore;
import com.google.bitcoin.store.UnreadableWalletException;
import com.google.bitcoin.utils.Threading;




public class ColorWallet {

	private static ColorWallet instance = null;
	private WalletWrapper wallet = null;
	private Map<String, Issuance> IssueanceMap = null;
    private Map<String, Integer> TrackingMap = new HashMap<String, Integer>();
	private List<Asset> AssetList = null;
	
	public static void start()
	{
		getInstance();
	}
	
	public static ColorWallet getInstance() {
		 if(instance == null) {
		     synchronized(ColorWallet.class) {
		       if(instance == null) {
		    	   instance = new ColorWallet();
		       }
		    }
		  }
		  return instance;
	   }
	
	private ColorWallet()
	{
		try {
			loadWallet();
			trackCololors();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BlockStoreException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void loadWallet() throws ConfigurationException, IOException {
		File WalletFile = new File(Settings.getInstance().getWalletFilename());
		Wallet w = null;
		if(!WalletFile.exists()) 
			WalletFile.createNewFile();
		try
		{
			w = Wallet.loadFromFile(WalletFile);
		}
		catch(UnreadableWalletException e) {
			w = new Wallet(Settings.getInstance().getNetowrkParams());
			w.saveToFile(WalletFile);
		}
		
		wallet= new WalletWrapper(WalletFile, w);
		
	}
	
	private void trackCololors() throws BlockStoreException, ConfigurationException, ParseException, InterruptedException, ExecutionException {
		System.out.println("Mode: " +  (Settings.getInstance().isTestNet() ? "Tesntnet" : "Mainnet"));
		IssueanceMap = BaseTrading.getInstance().getIssuane();
		AssetList = BaseTrading.getInstance().getAssetList();
		
		for(Asset asset : AssetList)
			System.out.println("found asset " +asset);
		
		final NetworkParameters params = Settings.getInstance().getNetowrkParams();
        BlockStore blockStore = new MemoryBlockStore(params);
        BlockChain chain = new BlockChain(params, wallet.getWallet(), blockStore);
        
        
        Date fastCatcupDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // set fast catchup to oldest color
        for( Entry<String, Issuance> i : IssueanceMap.entrySet()) {
        	if(sdf.parse(i.getValue().date).compareTo(fastCatcupDate) < 0)
        		fastCatcupDate = sdf.parse(i.getValue().date);
        	TrackingMap.put(i.getValue().geneisistransaction, i.getValue().outputindex);
        	System.out.println("found issuance " + i.getValue().geneisistransaction +" for asset " + getAssetById(i.getValue().asssetId));
        }
        
        // set fast catchup to correct date
        if(wallet.getWallet().getLastBlockSeenTime().after(fastCatcupDate))
        	fastCatcupDate = wallet.getWallet().getLastBlockSeenTime();
     
        final PeerGroup peerGroup = new PeerGroup(params, chain);
        peerGroup.setFastCatchupTimeSecs(fastCatcupDate.getTime() / 1000);
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
      
        peerGroup.start();
        peerGroup.startBlockChainDownload(new ColorDownloadListner(TrackingMap, wallet, true));

        /*
        Executors.newSingleThreadExecutor().execute(new Runnable() {
			public void run() {
				  peerGroup.addEventListener(new ColorDownloadListner(TrackingMap, wallet, true), Threading.USER_THREAD);
				peerGroup.downloadBlockChain();
				//peerGroup.startBlockChainDownload(new ColoredPeerEventListener(TrackingMap, wallet));
			  };
		});
		*/
		
	}
	
	private String getAssetById(String id) {
		for(Asset asset : AssetList)
		{
			if (asset.id.equals(id))
				return asset.toString();
		}
		return "Unknown";
	}
	public boolean IsTxColor(String txhash, int voutindex) {
		return BaseTrading.getInstance().GetColorTransactionSearchHistory(wallet.getWallet(), txhash, voutindex) != null;
	}
	
	public double getColorValue(String txhash, int voutindex) {
		return BaseTrading.getInstance().GetColorValue(wallet.getWallet(), txhash, voutindex).doubleValue();
	}

	public String getColorValueAndInfo(String colordef, String txHash, int outindex) {
		return BaseTrading.getInstance().getColorValueAndInfo(wallet.getWallet(),  colordef,  txHash, outindex);
	}
}
