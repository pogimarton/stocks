package shared;

import java.io.Serializable;

public class DayTradeRequestData implements Serializable{
	private static final long serialVersionUID = 1L;
	private final String	paperName;
	private final StockDate	date;
	
	public DayTradeRequestData(String paperName, StockDate date) {
		this.paperName = paperName;
		this.date = date;
	}

	public String getPaperName() {
		return paperName;
	}

	public StockDate getDate() {
		return date;
	}
}
