package threads;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import client.StockManager;

public class GetInfobarThread {
	private Handler getInfobarHandler;
	
	private Vector<String> favPaperNames;

	public GetInfobarThread(Handler getInfobarHandler, Vector<String> favPaperNames) {
		// TODO Auto-generated constructor stub
		this.getInfobarHandler = getInfobarHandler;
		this.favPaperNames = favPaperNames;

		Thread thread = new Thread(null, getInfobarName, "getInfoBarThread");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

	private Runnable getInfobarName = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			StockManager stockManager = new StockManager();
			TreeMap<String, Integer> infoBar = stockManager.getLastPapernamesAndPrice(favPaperNames);
			String tmpPaperName;
			for (Iterator<String> it = infoBar.keySet().iterator(); it.hasNext();) {
				Message msgToGui = new Message();
				Bundle messageData = new Bundle();
				tmpPaperName = it.next();
				msgToGui.what = 0;
				messageData.putString("paperName", tmpPaperName);
				messageData.putString("price", infoBar.get(tmpPaperName).toString());

				msgToGui.setData(messageData);
				//Log.e("KKKKKKKKKKKKKKKKKKKK", messageData.getString("paperName")+"  "+messageData.getString("price"));
				getInfobarHandler.sendMessage(msgToGui);
			}

			Message msgToGui = new Message();
			Bundle messageData = new Bundle();
			msgToGui.what = 1;

			msgToGui.setData(messageData);

			getInfobarHandler.sendMessage(msgToGui);

		}

	};

}