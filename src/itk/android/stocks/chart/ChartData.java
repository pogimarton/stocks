package itk.android.stocks.chart;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.achartengine.model.TimeSeries;

import android.util.Log;

public class ChartData {

	public String paperName = "";

	TimeSeries series;

	public ChartData(Date[] x, double[] y, String p) {


		
	}

	public ChartData(TimeSeries series, String paperName) {
		this.paperName = paperName;
		this.series=series;
		
	}


	public void append(Date tradeDate, double priceOrVolume)
	{
		
		series.add(tradeDate, priceOrVolume);
	}
	


}
