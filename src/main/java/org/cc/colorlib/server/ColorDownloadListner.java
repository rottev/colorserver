package org.cc.colorlib.server;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cc.colorlib.core.WalletWrapper;

import com.google.bitcoin.core.Block;
import com.google.bitcoin.core.DownloadListener;
import com.google.bitcoin.core.GetDataMessage;
import com.google.bitcoin.core.Message;
import com.google.bitcoin.core.Peer;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.TransactionInput;
import com.google.bitcoin.core.TransactionOutput;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.wallet.WalletTransaction;
import com.google.bitcoin.wallet.WalletTransaction.Pool;

public class ColorDownloadListner extends DownloadListener {

	private final Map<String, Integer> colorMap;
	private boolean deepSearch = false;
	private WalletWrapper wallet = null;
	private Wallet workingWallet;
	private boolean saveWhenDone = false;
	
	
	public ColorDownloadListner(Map<String, Integer> map, WalletWrapper w)
	{
		super();
		colorMap = map;
		wallet = w;
		workingWallet = w.getWallet();
	}
	
	public ColorDownloadListner(Map<String, Integer> map, WalletWrapper w, boolean saveWhenDone)
	{
		this(map, w);
		this.saveWhenDone = saveWhenDone;
	}
	
	@Override
	public void onChainDownloadStarted(Peer peer, int blocksLeft) {
		// TODO Auto-generated method stub
		super.onChainDownloadStarted(peer, blocksLeft);
		System.out.println("Sync starting");
	}

	@Override
	public void onBlocksDownloaded(Peer peer, Block block, int blocksLeft) {
		// TODO Auto-generated method stub
		super.onBlocksDownloaded(peer, block, blocksLeft);
		boolean printinfo = true;
		//System.out.println("ColoredPeerEventListener - onBlocksDownloaded got blocks");
		try{
			for(Transaction t : block.getTransactions())
	        {	
				if(printinfo) {
					printinfo = false;
					System.out.println("Working, blocks remaining: " + blocksLeft);
				}
				if(colorMap.containsKey(t.getHashAsString())){
					System.out.println("some transaction we will add to wallet: " + t.getHashAsString());
					// all outputs
					if(colorMap.get(t.getHashAsString()).intValue() == -1){
						System.out.println("all outputs");
						for(TransactionOutput outs : t.getOutputs())
						{
							if(outs.getSpentBy() != null){
								System.out.println("spent");
								colorMap.put(outs.getSpentBy().getOutpoint().getHash().toString(), -1);
							}
						}
					}
					else if(t.getOutput(colorMap.get(t.getHashAsString()).intValue()).getSpentBy() != null)
					{
						System.out.println("output index: " + colorMap.get(t.getHashAsString()).intValue());
						String addhash = t.getOutput(colorMap.get(t.getHashAsString()).intValue()).getSpentBy().getOutpoint().getHash().toString();
						System.out.println("add hash: " + addhash);
						
						colorMap.put(addhash, -1);
					}
					if(workingWallet.getTransaction(t.getHash()) == null) {
						System.err.println("AddingColorTx: " + t);
						workingWallet.addWalletTransaction(new WalletTransaction(Pool.PENDING, t));
					}
					deepSearch = true;
				}
				if(deepSearch){
					int insIndex = 0;
					boolean addToWallet = false;
					for( TransactionInput ins : t.getInputs()){
						
						if(colorMap.containsKey(ins.getOutpoint().getHash().toString()))
						{
	
						//	if(colorMap.get(ins.getOutpoint().getHash().toString()).intValue() == ins.getOutpoint().getIndex()){
								System.out.println("connecting transaction:\n" + t +"\nto:\n" + ins.getOutpoint().getHash().toString());
								colorMap.put(t.getHashAsString(), insIndex);
								addToWallet = true;
						//	}
						}							
						insIndex++;
					}
					if(addToWallet) {
						if(workingWallet.getTransaction(t.getHash()) == null) {
							System.err.println("AddingColorTx: " + t);
							workingWallet.addWalletTransaction(new WalletTransaction(Pool.PENDING, t));
							
						}
					}
				}
				
	        }
		}
		catch(Exception ex) {
			System.out.println("ColoredPeerEventListener - onBlocksDownloaded exception:\n");
			ex.printStackTrace();
		}
	}

	@Override
	protected void progress(double pct, int blocksSoFar, Date date) {
		// TODO Auto-generated method stub
		super.progress(pct, blocksSoFar, date);
		System.out.println("Sync percent: " + (int)pct + "%");
	}

	@Override
	protected void startDownload(int blocks) {
		// TODO Auto-generated method stub
		super.startDownload(blocks);
	}

	@Override
	protected void doneDownload() {
		// TODO Auto-generated method stub
		super.doneDownload();
		if(saveWhenDone)
		{
			try {
				System.out.println("saving wallet...");
				wallet.getWallet().saveToFile(wallet.getWalletFile());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Sync complete");
	}

	@Override
	public void await() throws InterruptedException {
		// TODO Auto-generated method stub
		super.await();
	}

	@Override
	public void onPeerConnected(Peer peer, int peerCount) {
		// TODO Auto-generated method stub
		super.onPeerConnected(peer, peerCount);
	}

	@Override
	public void onPeerDisconnected(Peer peer, int peerCount) {
		// TODO Auto-generated method stub
		super.onPeerDisconnected(peer, peerCount);
	}

	@Override
	public Message onPreMessageReceived(Peer peer, Message m) {
		// TODO Auto-generated method stub
		return super.onPreMessageReceived(peer, m);
	}

	@Override
	public void onTransaction(Peer peer, Transaction t) {
		// TODO Auto-generated method stub
		super.onTransaction(peer, t);
	}

	@Override
	public List<Message> getData(Peer peer, GetDataMessage m) {
		// TODO Auto-generated method stub
		return super.getData(peer, m);
	}



}
