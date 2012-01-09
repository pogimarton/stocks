package chart;

import java.util.Date;

public class StockChart {

	public ChartData priceChart;
	public ChartData volumeChart;

	public StockChart() {
		priceChart = null;
		volumeChart = null;
		setDemoData();
	}

	public StockChart(ChartData c1, ChartData c2) {
		this.priceChart = c1;
		this.volumeChart = c2;
	}

	public void setDemoData() {
		Date[] xdata = new Date[] { new Date(108, 9, 1), new Date(108, 9, 8), new Date(108, 9, 15), new Date(108, 9, 22), new Date(108, 9, 29), new Date(108, 10, 5), new Date(108, 10, 12), new Date(108, 10, 19), new Date(108, 10, 26), new Date(108, 11, 3), new Date(108, 11, 10), new Date(108, 11, 17) };
		double[] ydata = new double[] { 142, 123, 142, 152, 149, 122, 110, 120, 125, 155, 146, 150 };
		double[] ydata2 = new double[] { 122, 143, 131, 132, 139, 142, 150, 140, 155, 115, 126, 130 };

		priceChart = new ChartData(xdata, ydata, "OTP 1");
		volumeChart = new ChartData(xdata, ydata2, "OTP 2");
	}

	public void append(Date tradeDate, double price, int volume) {
		priceChart.append(tradeDate, price);
		volumeChart.append(tradeDate, volume);
	}

	/*
	 * public XYMultipleSeriesDataset generateDatasetFromCharts() {
	 * XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	 * dataset.addSeries(priceChart.getTimeSeries(priceChart.paperName));
	 * dataset.addSeries(volumeChart.getTimeSeries(volumeChart.paperName));
	 * return dataset; }
	 */

}
