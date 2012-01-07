package shared;

import java.io.Serializable;

public class FromTimeTradeRequestData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String	paperName;
	private final StockDate	date;
	private final StockTime time;
	
	public FromTimeTradeRequestData(String paperName, StockDate date, StockTime time) {
		this.paperName = paperName;
		this.date = date;
		this.time = time;
	}

	public String getPaperName() {
		return paperName;
	}

	public StockDate getDate() {
		return date;
	}
	
	public StockTime getTime(){
		return time;
	}
}
