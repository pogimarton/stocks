package threads;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import shared.PriceAndVolume;
import shared.StockDate;
import shared.StockTime;
import client.StockManager;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class GetDataThread {

	Handler stockDataHandler;
	Date fromInterval;
	Date toInterval;
	Context context;
	String paperName;
	
	
	public GetDataThread(Handler stockDataHandler, String paperName,Date fromInterval, Date toInterval, Context context) {
		

		this.stockDataHandler = stockDataHandler;
		this.fromInterval = fromInterval;
		this.toInterval = toInterval;
		this.context = context;
		this.paperName = paperName;

		Thread thread = new Thread(null, stockDataSaveAndDownload, "Background");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	private Runnable stockDataSaveAndDownload = new Runnable() {

		public void run() {







			Date[] newestTrade = getNewestData(paperName);
			Date newestTradeDateDate = null;
			Date newestTradeTimeDate = null;
			if (newestTrade != null) {
				newestTradeDateDate = newestTrade[0];
				newestTradeTimeDate = newestTrade[1];
			}
			// get oldest saved data

			Date[] oldestTrade = getOldestData(paperName);

			Date oldestTradeDateDate = null;
			

			if (oldestTrade != null) {
				oldestTradeDateDate = oldestTrade[0];
				
			}

			Calendar fromC = Calendar.getInstance();
			fromC.setTime(fromInterval);
			Calendar toC = Calendar.getInstance();
			toC.setTime(toInterval);

			Vector<Date> tradeDate = new Vector<Date>();

			int count1 = (int) ((toInterval.getTime() - fromInterval.getTime()) % 86399999);
			for (int i = 0; i <= count1; i++) {

				
				tradeDate.add(fromC.getTime());
				fromC.add(Calendar.DATE, 1);

			}

			if (oldestTradeDateDate != null && newestTradeDateDate != null) {

				

				Vector<Date> downloaded = new Vector<Date>();

				Calendar oldC = Calendar.getInstance();
				oldC.setTime(oldestTradeDateDate);
				Calendar newC = Calendar.getInstance();
				newC.setTime(newestTradeDateDate);

				int count2 = (int) ((newestTradeDateDate.getTime() - oldestTradeDateDate.getTime()) % 86399999);
				for (int i = 0; i <= count2; i++) {

				
					downloaded.add(oldC.getTime());
					oldC.add(Calendar.DATE, 1);

				}

				if (tradeDate.remove(newestTradeDateDate)) {
					saveTradesByTime(paperName, newestTradeDateDate, newestTradeTimeDate);
				}
				tradeDate.removeAll(downloaded);
			}

			
			
			saveTradesByDates(paperName, tradeDate);
			getData();

		}

	};

	private void getData() {

		
		getDataByDay(paperName, fromInterval, toInterval);


	}

	private void saveTradesByDates(String paperName, Vector<Date> tradeDateDates) {

		if (tradeDateDates.size() > 0) {
			SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
			SimpleDateFormat mounthFormat = new SimpleDateFormat("MM");
			SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

			TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>> stocksWithoutName;
			HashMap<String, TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>> stocksWithName = new HashMap<String, TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>>();

			DatabaseManager databaseManager = new DatabaseManager(context);

			Date tradeDateDate;
			StockDate stockDate;

			for (Iterator<Date> it = tradeDateDates.iterator(); it.hasNext();) {

				tradeDateDate = it.next();
		
				int year = Integer.parseInt(yearFormat.format(tradeDateDate));
				int mounth = Integer.parseInt(mounthFormat.format(tradeDateDate));
				int day = Integer.parseInt(dayFormat.format(tradeDateDate));

				stockDate = new StockDate(year, mounth, day);
				StockManager stockManager;
				try {
					stockManager = new StockManager();
					stocksWithoutName = stockManager.getDayTradesOfPaper(paperName, stockDate);

					stocksWithName.put(paperName, stocksWithoutName);
		
					databaseManager.insertStockData(stocksWithName);
				} catch (UnknownHostException e) {
					Message msgToGui = new Message();

					Bundle messageData = new Bundle();
					msgToGui.what = 2;
					messageData.putString("error", "No connection");

					msgToGui.setData(messageData);
					
					stockDataHandler.sendMessage(msgToGui);
					
				} catch (IOException e) {
					Message msgToGui = new Message();

					Bundle messageData = new Bundle();
					msgToGui.what = 2;
					messageData.putString("error", "No connection");

					msgToGui.setData(messageData);
					
					stockDataHandler.sendMessage(msgToGui);
				}


			}

			databaseManager.close();
		}
	}

	private void saveTradesByTime(String paperName, Date tradeDateDate, Date tradeTimeDate) {
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		SimpleDateFormat mounthFormat = new SimpleDateFormat("MM");
		SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

		SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
		SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
		SimpleDateFormat secondFormat = new SimpleDateFormat("ss");

		TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>> stocksWithoutName;
		HashMap<String, TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>> stocksWithName = new HashMap<String, TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>>();

		DatabaseManager databaseManager = new DatabaseManager(context);

		int year = Integer.parseInt(yearFormat.format(tradeDateDate));
		int mounth = Integer.parseInt(mounthFormat.format(tradeDateDate));
		int day = Integer.parseInt(dayFormat.format(tradeDateDate));

		int hour = Integer.parseInt(hourFormat.format(tradeTimeDate));

		int minute = Integer.parseInt(minuteFormat.format(tradeTimeDate));

		int second = Integer.parseInt(secondFormat.format(tradeTimeDate));

		StockDate stockDate = new StockDate(year, mounth, day);

		StockTime stockTime = new StockTime(hour, minute, second);
		StockManager stockManager;
		try {
			
			stockManager = new StockManager();
			stocksWithoutName = stockManager.getFromTimeTradesOfPaper(paperName, stockDate, stockTime);

			stocksWithName.put(paperName, stocksWithoutName);
		

			databaseManager.insertStockData(stocksWithName);

			databaseManager.close();
			
			
			
		} catch (UnknownHostException e) {
			Message msgToGui = new Message();

			Bundle messageData = new Bundle();
			msgToGui.what = 2;
			messageData.putString("error", "No connection");

			msgToGui.setData(messageData);
			
			stockDataHandler.sendMessage(msgToGui);
		} catch (IOException e) {
			Message msgToGui = new Message();

			Bundle messageData = new Bundle();
			msgToGui.what = 2;
			messageData.putString("error", "No connection");

			msgToGui.setData(messageData);
			
			stockDataHandler.sendMessage(msgToGui);
		}
		
	}

	
	private void getDataByDay(String paperName, Date fromInterval, Date toInterval) {
		DatabaseManager databaseManager = new DatabaseManager(context);


		Date act = fromInterval;




		Cursor c;
		Calendar actC = Calendar.getInstance();
		actC.setTime(act);
		Calendar toC = Calendar.getInstance();
		toC.setTime(toInterval);

		Integer count = (int) ((toInterval.getTime() - act.getTime()) % 86399999);

		for (int i = 0; i <= count; i++) {


			c = databaseManager.getStockDatabyDay(paperName, dateToStockDate(act));

			actC.add(Calendar.DATE, 1);
			act = actC.getTime();


			if (c.moveToFirst() && c.getCount() > 0) {
				do {

					Message msgToGui = new Message();

					Bundle messageData = new Bundle();
					msgToGui.what = 0;
					messageData.putString("paperName", paperName);
					messageData.putString("date", c.getString(c.getColumnIndex(DatabaseManager.tradeDateColumnName)));
					messageData.putString("time", c.getString(c.getColumnIndex(DatabaseManager.tradeTimeColumnName)));
					messageData.putString("price", c.getString(c.getColumnIndex(DatabaseManager.priceColumnName)));
					messageData.putString("volume", c.getString(c.getColumnIndex(DatabaseManager.volumeColumnName)));

					msgToGui.setData(messageData);

					stockDataHandler.sendMessage(msgToGui);


				} while (c.moveToNext());
				Message msgToGui = new Message();

				Bundle messageData = new Bundle();
				msgToGui.what = 1;


				msgToGui.setData(messageData);
				// Log.i("stockMain", messageData.getString("1"));
				stockDataHandler.sendMessage(msgToGui);

			}
			else
			{
				Message msgToGui = new Message();

				Bundle messageData = new Bundle();
				msgToGui.what = 2;
				messageData.putString("error", "No Data");

				msgToGui.setData(messageData);
				
				stockDataHandler.sendMessage(msgToGui);
			}

		}

		databaseManager.close();
	}

	private Date[] getNewestData(String paperName) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		DatabaseManager databaseManager = new DatabaseManager(context);

		Cursor c = databaseManager.getNewestData(paperName);

		Date res[] = null;
		if (c.moveToFirst()) {
			String date = c.getString(c.getColumnIndex(DatabaseManager.tradeDateColumnName));
			String time = c.getString(c.getColumnIndex(DatabaseManager.tradeTimeColumnName));

			Date dateD = null;
			Date timeD = null;
			try {
				dateD = dateFormat.parse(date);
				timeD = timeFormat.parse(time);
			} catch (ParseException e) {

				e.printStackTrace();
			}

			res = new Date[] { dateD, timeD };

		}
		databaseManager.close();
		return res;
	}

	private Date[] getOldestData(String paperName) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		DatabaseManager databaseManager = new DatabaseManager(context);

		Cursor c = databaseManager.getOldestData(paperName);


		Date res[] = null;
		if (c.moveToFirst()) {

			String date = c.getString(c.getColumnIndex(DatabaseManager.tradeDateColumnName));
			String time = c.getString(c.getColumnIndex(DatabaseManager.tradeTimeColumnName));

			Date dateD = null;
			Date timeD = null;
			try {
				dateD = dateFormat.parse(date);
				timeD = timeFormat.parse(time);
			} catch (ParseException e) {

				e.printStackTrace();
			}

			res = new Date[] { dateD, timeD };
		}
		databaseManager.close();
		return res;
	}

	/*
	 * private void deletDataByDate(String paperName, Date tradeDate) {
	 * DatabaseManager databaseManager = new DatabaseManager(context); StockDate
	 * stockDate = dateToStockDate(tradeDate);
	 * databaseManager.deleteStockData(paperName, stockDate);
	 * 
	 * }
	 */

	private StockDate dateToStockDate(Date date) {

		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		SimpleDateFormat mounthFormat = new SimpleDateFormat("MM");
		SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

		int year = Integer.parseInt(yearFormat.format(date));
		int mounth = Integer.parseInt(mounthFormat.format(date));
		int day = Integer.parseInt(dayFormat.format(date));

		StockDate stockDate = new StockDate(year, mounth, day);

		return stockDate;

	}



}
