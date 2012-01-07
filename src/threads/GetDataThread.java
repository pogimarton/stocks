package threads;

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
import android.util.Log;

public class GetDataThread {

	Handler stockDataHandler;
	Date fromInterval;
	Date toInterval;
	Context context;
	String paperName;
	
	
	public GetDataThread(Handler stockDataHandler, String paperName,Date fromInterval, Date toInterval, Context context) {
		// TODO Auto-generated constructor stub

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

			// TODO get intervall
			// TODO get act day

			// /params begin
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

			// Date fromInterval = null;
			// Date toInterval = null;
			//
			// try {
			// fromInterval = dateFormat.parse("2008-02-22");
			// toInterval = dateFormat.parse("2008-02-26");
			//
			// } catch (ParseException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			

			// /params end

			// get newest saved data

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
			Date oldestTradeTimeDate = null;

			if (oldestTrade != null) {
				oldestTradeDateDate = oldestTrade[0];
				oldestTradeTimeDate = oldestTrade[1];
			}
			// Calendar fromC = Calendar.getInstance();
			// fromC.setTime(fromInterval);
			// toInterval.getTime() - fromInterval.getTime()) % 86399999
			// fromC.getTime()
			// fromC.add(Calendar.DATE, 1);
			Calendar fromC = Calendar.getInstance();
			fromC.setTime(fromInterval);
			Calendar toC = Calendar.getInstance();
			toC.setTime(toInterval);

			Vector<Date> tradeDate = new Vector<Date>();

			int count1 = (int) ((toInterval.getTime() - fromInterval.getTime()) % 86399999);
			for (int i = 0; i <= count1; i++) {

				Log.e("stockMainActivity.handlStockData.ujak", dateFormat.format(fromC.getTime()));
				tradeDate.add(fromC.getTime());
				fromC.add(Calendar.DATE, 1);

			}

			if (oldestTradeDateDate != null && newestTradeDateDate != null) {

				Log.e("stockMainActivity.handlStockData.NEW", dateFormat.format(newestTradeDateDate));
				Log.e("stockMainActivity.handlStockData.OLD", dateFormat.format(oldestTradeDateDate));

				Vector<Date> downloaded = new Vector<Date>();

				Calendar oldC = Calendar.getInstance();
				oldC.setTime(oldestTradeDateDate);
				Calendar newC = Calendar.getInstance();
				newC.setTime(newestTradeDateDate);

				int count2 = (int) ((newestTradeDateDate.getTime() - oldestTradeDateDate.getTime()) % 86399999);
				for (int i = 0; i <= count2; i++) {

					Log.e("stockMainActivity.handlStockData.old", dateFormat.format(oldC.getTime()));
					downloaded.add(oldC.getTime());
					oldC.add(Calendar.DATE, 1);

				}

				if (tradeDate.remove(newestTradeDateDate)) {
					saveTradesByTime(paperName, newestTradeDateDate, newestTradeTimeDate);
				}
				tradeDate.removeAll(downloaded);
			}
			String out = "asdasd";
			for (int i = 0; i < tradeDate.size(); i++) {
				out += dateFormat.format(tradeDate.get(i)) + " ";
			}
			Log.e("stockMainActivity.handlStockData.tradedate", out);
			saveTradesByDates(paperName, tradeDate);
			getData();

		}

	};

	private void getData() {

		// TODO handleren keresztul adatok visszakuldese
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		
		/*
		 * Date fromInterval = null; Date toInterval = null; try { fromInterval
		 * = dateFormat.parse("2008-02-22"); toInterval =
		 * dateFormat.parse("2008-02-26"); } catch (ParseException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		PriceAndVolume PAV;
		HashMap<String, TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>> stocks;

		// stocks =
		getDataByDay(paperName, fromInterval, toInterval);

		/*
		 * for (Iterator<StockDate> itSD =
		 * stocks.get(paperName).keySet().iterator(); itSD.hasNext();) {
		 * StockDate dateKey = itSD.next();
		 * 
		 * 
		 * 
		 * for (Iterator<StockTime> itST =
		 * stocks.get(paperName).get(dateKey).keySet().iterator();
		 * itST.hasNext();) { StockTime timeKey = itST.next(); PAV =
		 * stocks.get(paperName).get(dateKey).get(timeKey);
		 * 
		 * Message msgToGui = new Message();
		 * 
		 * 
		 * Bundle messageData = new Bundle(); msgToGui.what = 0;
		 * messageData.putString("paperName", paperName);
		 * messageData.putString("date", dateKey.toString());
		 * messageData.putString("time", timeKey.toString());
		 * messageData.putString("price", PAV.getPrice()+"");
		 * messageData.putString("volume", PAV.getVolume()+"");
		 * 
		 * msgToGui.setData(messageData); //Log.i("stockMain",
		 * messageData.getString("1")); stockDataHandler.sendMessage(msgToGui);
		 * 
		 * }
		 * 
		 * }
		 */
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
				Log.e("saveTradesByDates", tradeDateDate.toGMTString());
				int year = Integer.parseInt(yearFormat.format(tradeDateDate));
				int mounth = Integer.parseInt(mounthFormat.format(tradeDateDate));
				int day = Integer.parseInt(dayFormat.format(tradeDateDate));

				stockDate = new StockDate(year, mounth, day);

				stocksWithoutName = StockManager.getDayTradesOfPaper(paperName, stockDate);

				stocksWithName.put(paperName, stocksWithoutName);
				// databaseManager.deleteStockData(paperName, stockDate);
				databaseManager.insertStockData(stocksWithName);

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

		stocksWithoutName = StockManager.getFromTimeTradesOfPaper(paperName, stockDate, stockTime);

		stocksWithName.put(paperName, stocksWithoutName);
		Log.e("savabytime saved data", stocksWithName.toString());

		databaseManager.insertStockData(stocksWithName);

		databaseManager.close();
	}

	// private HashMap<String, TreeMap<StockDate, TreeMap<StockTime,
	// PriceAndVolume>>> getDataByDay(String paperName, Date fromInterval, Date
	// toInterval) {
	private void getDataByDay(String paperName, Date fromInterval, Date toInterval) {
		DatabaseManager databaseManager = new DatabaseManager(context);

		// Date tmpDateDate = null;
		// Date tmpTimeDate = null;
		// StockTime tmpStockTime;
		// StockDate tmpStockDate;
		Date act = fromInterval;
		// PriceAndVolume tmpPriceAndVolume;
		// TreeMap<StockTime, PriceAndVolume> tmpStockTimeTree;
		// TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>
		// tmpStockDateTree;
		// HashMap<String, TreeMap<StockDate, TreeMap<StockTime,
		// PriceAndVolume>>> stocks = new HashMap<String, TreeMap<StockDate,
		// TreeMap<StockTime, PriceAndVolume>>>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		// TODO if c null
		Cursor c;
		Calendar actC = Calendar.getInstance();
		actC.setTime(act);
		Calendar toC = Calendar.getInstance();
		toC.setTime(toInterval);

		Integer count = (int) ((toInterval.getTime() - act.getTime()) % 86399999);
		// tmpStockDateTree = new TreeMap<StockDate, TreeMap<StockTime,
		// PriceAndVolume>>();
		for (int i = 0; i <= count; i++) {

			Log.e("stockMainActivity.getDataByDay.Day", dateFormat.format(act));
			c = databaseManager.getStockDatabyDay(paperName, dateToStockDate(act));

			actC.add(Calendar.DATE, 1);
			act = actC.getTime();

			Log.e("stockMainActivity.getDataByDay.cursor", c.getCount() + "");
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
					// Log.i("stockMain", messageData.getString("1"));
					stockDataHandler.sendMessage(msgToGui);

					/*
					 * String date =
					 * c.getString(c.getColumnIndex(DatabaseManager
					 * .tradeDateColumnName)); String time =
					 * c.getString(c.getColumnIndex
					 * (DatabaseManager.tradeTimeColumnName)); Double price =
					 * c.getDouble
					 * (c.getColumnIndex(DatabaseManager.priceColumnName)); int
					 * volume =
					 * c.getInt(c.getColumnIndex(DatabaseManager.volumeColumnName
					 * ));
					 * 
					 * tmpPriceAndVolume = new PriceAndVolume(price, volume);
					 * 
					 * try {
					 * 
					 * tmpTimeDate = timeFormat.parse(time); tmpDateDate =
					 * dateFormat.parse(date);
					 * 
					 * } catch (ParseException e) { // TODO Auto-generated catch
					 * block e.printStackTrace(); }
					 * 
					 * tmpStockTime = dateToStockTime(tmpTimeDate); tmpStockDate
					 * = dateToStockDate(tmpDateDate);
					 * 
					 * if (tmpStockDateTree.containsKey(tmpStockDate)) {
					 * 
					 * tmpStockDateTree.get(tmpStockDate).put(tmpStockTime,
					 * tmpPriceAndVolume);
					 * 
					 * } else {
					 * 
					 * tmpStockTimeTree = new TreeMap<StockTime,
					 * PriceAndVolume>(); tmpStockTimeTree.put(tmpStockTime,
					 * tmpPriceAndVolume);
					 * 
					 * tmpStockDateTree.put(tmpStockDate, tmpStockTimeTree);
					 * 
					 * }
					 */

				} while (c.moveToNext());
				Message msgToGui = new Message();

				Bundle messageData = new Bundle();
				msgToGui.what = 1;
				//messageData.putString("end", "end");

				msgToGui.setData(messageData);
				// Log.i("stockMain", messageData.getString("1"));
				stockDataHandler.sendMessage(msgToGui);

			}

		}
		// stocks.put(paperName, tmpStockDateTree);
		databaseManager.close();
		// Log.e("stockMainActivity.getDataByDay.data",
		// stocks.get(paperName).keySet().toString());
		// return stocks;
	}

	private Date[] getNewestData(String paperName) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		DatabaseManager databaseManager = new DatabaseManager(context);

		Cursor c = databaseManager.getNewestData(paperName);
		// TODO if c = null??
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			res = new Date[] { dateD, timeD };

		}
		databaseManager.close();
		// Log.e("stockMainActivity.getNewestData.res[0]",
		// dateFormat.format(res[0]));
		// Log.e("stockMainActivity.getNewestData.res[1]",
		// timeFormat.format(res[1]));
		return res;
	}

	private Date[] getOldestData(String paperName) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		DatabaseManager databaseManager = new DatabaseManager(context);

		Cursor c = databaseManager.getOldestData(paperName);

		// TODO c if null
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			res = new Date[] { dateD, timeD };
		}
		databaseManager.close();
		return res;
	}

	private void deletDataByDate(String paperName, Date tradeDate) {
		DatabaseManager databaseManager = new DatabaseManager(context);
		StockDate stockDate = dateToStockDate(tradeDate);
		databaseManager.deleteStockData(paperName, stockDate);

	}

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

	private Date stockDateToDate(StockDate stockDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(stockDate.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	private StockTime dateToStockTime(Date date) {
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
		SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
		SimpleDateFormat secondFormat = new SimpleDateFormat("ss");

		int hour = Integer.parseInt(hourFormat.format(date));
		;
		int minute = Integer.parseInt(minuteFormat.format(date));
		;
		int second = Integer.parseInt(secondFormat.format(date));
		;

		return new StockTime(hour, minute, second);
	}

	private Date StockTimeToDate(StockTime stockTime) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		Date date = null;
		try {
			date = timeFormat.parse(stockTime.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

}
