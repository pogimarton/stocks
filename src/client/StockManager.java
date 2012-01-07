package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;



import shared.*;

public class StockManager {
	//first key: paper name
	//second key: date
	//third key: time
	
	
	static final private HashMap<String, TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>>	stocks	= new HashMap<String, TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>> ();
	
	private StockManager() {
	}
	
	public static TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>> getDayTradesOfPaper (String paperName, StockDate date) {
//TODO itt ellenőrizzuk hogy megvannak az adatok??
//		if (stocks.containsKey(paperName)) {
//			TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>> paperTrades = stocks.get(paperName);
//			if (paperTrades.containsKey(date))
//				return paperTrades.get(date);
//		}
		
		StockClientRequest<DayTradeRequestData> request = new StockClientRequest<DayTradeRequestData>(RequestType.GetDayTradeOfPaper, new DayTradeRequestData(paperName, date));
		
		StockServerResponse<?> response = StockClient.processRequest(request);
		
		if (response.getResponseTo() == RequestType.GetDayTradeOfPaper) {
			
			java.lang.Object responseObject = response.getResponseData();
			
			if (responseObject instanceof TreeMap) {
				
				TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>> responseData=null;
				try {
					responseData = ((TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>) responseObject);
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				return responseData;
			}
			
		}
		
		//TODO ez nem tetszik nem lenne jobb ha null al terne vissza
		return new TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>();
	}
	
	
	public static TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>> getFromTimeTradesOfPaper (String paperName, StockDate date, StockTime time)
	{
		StockClientRequest<FromTimeTradeRequestData> request = new StockClientRequest<FromTimeTradeRequestData>(RequestType.GetFromTimeTradeData, new FromTimeTradeRequestData(paperName, date, time));
		StockServerResponse<?> response = StockClient.processRequest(request);
		
		if (response.getResponseTo() == RequestType.GetFromTimeTradeData) {
			
			java.lang.Object responseObject = response.getResponseData();
			if (responseObject instanceof TreeMap) {
				TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>> responseData=null;
				try {
					responseData = ((TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>) responseObject);
				
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				return responseData;
			}
		}
		
		//TODO ez nem tetszik nem lenne jobb ha null al terne vissza
		return new TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>();
	}
	

	
	public static Vector<String> getAllPaperNames()
	{
		StockClientRequest<?> request = new StockClientRequest<String>(RequestType.GetPaperName, null);
		StockServerResponse<?> response = StockClient.processRequest(request);
		
		if (response.getResponseTo() == RequestType.GetPaperName) {
			
			java.lang.Object responseObject = response.getResponseData();
			if (responseObject instanceof Vector) {
				Vector<String> responseData=null;
				try {
					responseData = (Vector<String>) responseObject;
				
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				return responseData;
			}
		}
		
		//TODO ez nem tetszik nem lenne jobb ha null al terne vissza
		return new Vector<String>();
	}
	
	public static TreeMap<String,Integer> getLastPapernamesAndPrice(Vector<String> favPaperNames)
	{
		StockClientRequest<LastPapernameAndPrice> request = new StockClientRequest<LastPapernameAndPrice>(RequestType.GetLastPapernameAndPrice, new LastPapernameAndPrice(favPaperNames));
		StockServerResponse<?> response = StockClient.processRequest(request);
		
		if (response.getResponseTo() == RequestType.GetLastPapernameAndPrice) {
			
			java.lang.Object responseObject = response.getResponseData();
			if (responseObject instanceof TreeMap) {
				TreeMap<String,Integer> responseData=null;
				try {
					responseData = (TreeMap<String,Integer>) responseObject;
				
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				return responseData;
			}
		}
		
		//TODO ez nem tetszik nem lenne jobb ha null al terne vissza
		return new TreeMap<String,Integer>();
	}
	
	

}
