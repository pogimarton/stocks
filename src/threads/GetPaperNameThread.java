package threads;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import client.StockManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class GetPaperNameThread {
	private Handler paperNameHandler;

	public GetPaperNameThread(Handler paperNameHandler) {
		
		this.paperNameHandler = paperNameHandler;

		Thread thread = new Thread(null, getPaperName, "getPaperThread");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

	private Runnable getPaperName = new Runnable() {

		public void run() {
			
			StockManager stockManager;			
			
			try {
				
				stockManager = new StockManager();
				Vector<String> paperNames = stockManager.getAllPaperNames();
				for (Iterator<String> it = paperNames.iterator(); it.hasNext();) {
					Message msgToGui = new Message();
					Bundle messageData = new Bundle();
					msgToGui.what = 0;
					messageData.putString("paperName", it.next());

					msgToGui.setData(messageData);
					//Log.e("KKKKKKKKKKKKKKKKKKKK", messageData.getString("paperName"));
					paperNameHandler.sendMessage(msgToGui);
				}

				Message msgToGui = new Message();
				Bundle messageData = new Bundle();
				msgToGui.what = 1;

				msgToGui.setData(messageData);

				paperNameHandler.sendMessage(msgToGui);
				
			} catch (UnknownHostException e) {
				Message msgToGui = new Message();

				Bundle messageData = new Bundle();
				msgToGui.what = 2;
				messageData.putString("error", "No connection");

				msgToGui.setData(messageData);
				
				paperNameHandler.sendMessage(msgToGui);
			} catch (IOException e) {
				Message msgToGui = new Message();

				Bundle messageData = new Bundle();
				msgToGui.what = 2;
				messageData.putString("error", "No connection");

				msgToGui.setData(messageData);
				
				paperNameHandler.sendMessage(msgToGui);
			}
			



		}

	};

}
