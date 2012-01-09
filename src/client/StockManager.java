package client;


import java.io.IOException;
import java.net.UnknownHostException;

import java.util.TreeMap;
import java.util.Vector;



import shared.*;

public class StockManager {

	StockClient stockClient;
		
	public StockManager() throws UnknownHostException, IOException {
		stockClient = new StockClient();
	}
	
	@SuppressWarnings("unchecked")
	public TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>> getDayTradesOfPaper (String paperName, StockDate date) {
		
		StockClientRequest<DayTradeRequestData> request = new StockClientRequest<DayTradeRequestData>(RequestType.GetDayTradeOfPaper, new DayTradeRequestData(paperName, date));
		
		StockServerResponse<?> response = stockClient.processRequest(request);
		
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
		
		
		return new TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>();
	}
	
	
	@SuppressWarnings("unchecked")
	public TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>> getFromTimeTradesOfPaper (String paperName, StockDate date, StockTime time)
	{
		StockClientRequest<FromTimeTradeRequestData> request = new StockClientRequest<FromTimeTradeRequestData>(RequestType.GetFromTimeTradeData, new FromTimeTradeRequestData(paperName, date, time));
		StockServerResponse<?> response = stockClient.processRequest(request);
		
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
		
		
		return new TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>();
	}
	

	
	@SuppressWarnings("unchecked")
	public Vector<String> getAllPaperNames()
	{
		StockClientRequest<?> request = new StockClientRequest<String>(RequestType.GetPaperName, null);
		StockServerResponse<?> response = stockClient.processRequest(request);
		
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
		
		
		return new Vector<String>();
	}
	
	@SuppressWarnings("unchecked")
	public TreeMap<String,Integer> getLastPapernamesAndPrice(Vector<String> favPaperNames)
	{
		StockClientRequest<LastPapernameAndPrice> request = new StockClientRequest<LastPapernameAndPrice>(RequestType.GetLastPapernameAndPrice, new LastPapernameAndPrice(favPaperNames));
		StockServerResponse<?> response = stockClient.processRequest(request);
		
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
		
		
		return new TreeMap<String,Integer>();
	}
	
	

}
