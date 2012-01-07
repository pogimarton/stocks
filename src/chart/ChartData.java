package chart;

import java.util.Date;

import org.achartengine.model.TimeSeries;

public class ChartData {

	public String paperName = "";

	TimeSeries series;

	public ChartData(Date[] x, double[] y, String p) {

	}

	public ChartData(TimeSeries series, String paperName) {
		this.paperName = paperName;
		this.series = series;

	}

	public void append(Date tradeDate, double priceOrVolume) {

		series.add(tradeDate, priceOrVolume);
	}

}
