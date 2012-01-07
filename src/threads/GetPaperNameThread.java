package threads;

import java.util.Iterator;
import java.util.Vector;

import client.StockManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GetPaperNameThread {
	private Handler paperNameHandler;

	public GetPaperNameThread(Handler paperNameHandler) {
		// TODO Auto-generated constructor stub
		this.paperNameHandler = paperNameHandler;

		Thread thread = new Thread(null, getPaperName, "getPaperThread");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

	private Runnable getPaperName = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			StockManager stockManager = new StockManager();
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

		}

	};

}
