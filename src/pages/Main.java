package pages;

import itk.ppke.stock.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import threads.GetDataThread;
import threads.GetInfobarThread;
import threads.GetPaperNameThread;

import chart.StockChart;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

	private GraphicalView chart;
	private XYMultipleSeriesRenderer renderer;
	private XYMultipleSeriesDataset dataset;

	public Handler stockDataHandler;
	public Handler paperNameHandler;
	public Handler getInfobarHandler;

	public TimeSeries currentPriceSeries;
	public TimeSeries currentVolumeSeries;

	private ProgressDialog progressDialog;

	private String[] paperNames;

	private String paperName;
	private Date fromDate;
	private Vector<String> favPaperNames;

	private Dialog timeSelectDialog;
	
	private StockChart demo;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		favPaperNames = new Vector<String>();

		setContentView(R.layout.main);

		// get element references
		// final ImageButton selectorPrev = (ImageButton) findViewById(R.selector.previous);
		// final ImageButton selectorNext = (ImageButton) findViewById(R.selector.next);
		final ImageButton selectorListAllFav = (ImageButton) findViewById(R.selector.list);

		// paper names handlerenek beallitase
		setPaperNameHandler();

		// paper names adatainak lekerese
		new GetPaperNameThread(paperNameHandler);

		// favorit lekekerese
		getFavPaperNames();

		// infobar handlerenek beallitasa
		setInfobarHandler();

		// infobar adatainak lekerese
		new GetInfobarThread(getInfobarHandler, favPaperNames);

		final ImageButton controlRefresh = (ImageButton) findViewById(R.controls.refresh);
		final ImageButton controlTimeSelect = (ImageButton) findViewById(R.controls.time_select);
		final ImageButton controlZoomIn = (ImageButton) findViewById(R.controls.zoom_in);
		final ImageButton controlZoomOut = (ImageButton) findViewById(R.controls.zoom_out);

		final TextView infobarTextView = (TextView) findViewById(R.infobar.textview);
		infobarTextView.setText(R.string.infobar_default);

		timeSelectDialog = new Dialog(this);
		timeSelectDialog.setContentView(R.layout.time_select_dialog);
		timeSelectDialog.setTitle(R.string.select_day);
		timeSelectDialog.setCancelable(true);
		
		selectorListAllFav.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ChartSelector.class);
				startActivityForResult(intent,1);
			}
		});

		controlRefresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				getPaperName();
				getFromDate();

				progressDialog = ProgressDialog.show(Main.this, "", getString(R.string.loading_));
				new GetDataThread(stockDataHandler, paperName, fromDate, fromDate, getApplicationContext());

				// favorit lekekerese
				getFavPaperNames();

				// infobar handlerenek beallitasa
				new GetInfobarThread(getInfobarHandler, favPaperNames);
			}
		});

		controlTimeSelect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				timeSelectDialog.show();
			}
		});

		Button datePickerOk = (Button) timeSelectDialog.findViewById(R.date_picker.ok_button);
		Button datePickerCancel = (Button) timeSelectDialog.findViewById(R.date_picker.cancel_button);

		datePickerOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getPaperName();
				getFromDate();

				progressDialog = ProgressDialog.show(Main.this, "", getString(R.string.loading_));
				new GetDataThread(stockDataHandler, paperName, fromDate, fromDate, getApplicationContext());

				
				timeSelectDialog.hide();
			}
		});

		datePickerCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				timeSelectDialog.hide();
			}
		});

		controlZoomIn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				chart.zoomIn();
			}
		});

		controlZoomOut.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				chart.zoomOut();
			}
		});

	}

	private void setInfobarHandler() {
		getInfobarHandler = new Handler() {

			StringBuilder infoBarStringBuilder = new StringBuilder();

			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
				if (msg.what == 0) {
					// igy kell hozzaadni adatot
					infoBarStringBuilder.append(msg.getData().getString("paperName") + " " + msg.getData().getString("price") + " | ");
				}
				if (msg.what == 1) {
					// progressDialog.dismiss();
					sendInfoBarString();
				}
				if (msg.what == 2) {
					// TODO nincs kapcsoalt, nincs adat, 
					Toast.makeText(getApplicationContext(), msg.getData().getString("error"), Toast.LENGTH_SHORT).show();
				}
			}

			public void sendInfoBarString() {
				Main.this.setInfobar(infoBarStringBuilder.toString());
			}

		};

	}

	private void setInfobar(String infoBarString) {
		final TextView infobarTextView = (TextView) findViewById(R.infobar.textview);
		infobarTextView.setText(infoBarString);
		infobarTextView.setSelected(true);
	}

	private void setPaperNameHandler() {

		paperNames = new String[] { "", "" };

		paperNameHandler = new Handler() {

			String tmpPaperNames = "";

			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
				if (msg.what == 0) {
					// igy kell hozzaadni adatot
					tmpPaperNames += msg.getData().getString("paperName") + ";";
				}
				if (msg.what == 1) {
					// progressDialog.dismiss();
					sendPaperNames();
				}
				if (msg.what == 2) {
					// TODO nincs kapcsoalt, nincs adat, 
					Toast.makeText(getApplicationContext(), msg.getData().getString("error"), Toast.LENGTH_SHORT).show();
				}
			}

			public void sendPaperNames() {
				String[] paperNamesArray = tmpPaperNames.split(";");
				Main.this.setPaperNames(paperNamesArray);
			}
		};

	}

	public void setPaperNames(String[] paperNames) {
		this.paperNames = paperNames;
		final AutoCompleteTextView selectorAutoCompleteView = (AutoCompleteTextView) findViewById(R.selector.autocomplete);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.selector_autocomplete_list_item, paperNames);
		selectorAutoCompleteView.setAdapter(adapter);
	}

	private void setStockDataHandler() {

		final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd" + " " + "HH:mm:ss");
		stockDataHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
				if (msg.what == 0) {
					try {

						Date tmpDate = dateTimeFormat.parse(msg.getData().getString("date") + " " + msg.getData().getString("time"));
						// igy kell hozzaadni adatot
						double tmpprice = Double.parseDouble(msg.getData().getString("price"));
						double tmpvolume = Integer.parseInt(msg.getData().getString("volume")) / 10000;
						currentPriceSeries.add(tmpDate, tmpprice);
						currentVolumeSeries.add(tmpDate, tmpvolume);

					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					chart.repaint();
				}
				if (msg.what == 1) {
					
						progressDialog.dismiss();
					
				}
				if (msg.what == 2) {
					// TODO nincs kapcsoalt, nincs adat, 
					progressDialog.dismiss();
					Toast.makeText(getApplicationContext(), msg.getData().getString("error"), Toast.LENGTH_SHORT).show();
				}
			}
		};

	}

	
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		
		if(resultCode == 1)
		{
			this.paperName = data.getStringExtra("selected");
		Toast.makeText(getApplicationContext(), "Selected: " + paperName, Toast.LENGTH_SHORT).show();
		final AutoCompleteTextView selectorAutoCompleteView = (AutoCompleteTextView) findViewById(R.selector.autocomplete);
		selectorAutoCompleteView.setText(this.paperName);
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// itt kapja el a kiv�lasztott chartot ha a ChartSelectorban r�kattintott
		// ha null, akkor egy default �rt�ket kell be�ll�tani
		

		setStockDataHandler();
		getPaperName();
		getFromDate();
		
		
		if(paperName != null && !paperName.equals(""))
		{
			progressDialog = ProgressDialog.show(Main.this, "", getString(R.string.loading_));
			
			new GetDataThread(stockDataHandler, paperName, fromDate, fromDate, getApplicationContext());
			
		}
		if (chart == null) {
			renderer = new XYMultipleSeriesRenderer();
			setupRenderer(renderer);

			final LinearLayout chartLayout = (LinearLayout) findViewById(R.main.chart);

			demo = new StockChart();

			// SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

			dataset = new XYMultipleSeriesDataset();
			//dataset.removeSeries(1);
			//dataset.removeSeries(2);
			
			TimeSeries priceSeries = new TimeSeries(demo.priceChart.paperName);
			TimeSeries volumeSeries = new TimeSeries(demo.volumeChart.paperName);
			
			
			dataset.addSeries(priceSeries);
			dataset.addSeries(volumeSeries);
			currentPriceSeries = priceSeries;
			currentVolumeSeries = volumeSeries;
			
			
			chart = ChartFactory.getTimeChartView(this, dataset, renderer, "yyyy.MM.dd. hh:mm");
			chartLayout.addView(chart);
		} else {
			chart.repaint();
		}
	}

	private void getFavPaperNames() {
		SharedPreferences prefManager = PreferenceManager.getDefaultSharedPreferences(this);
		Map<String, ?> all = prefManager.getAll();
		favPaperNames = new Vector<String>();
		for (String key : all.keySet()) {
			Boolean b = (Boolean) all.get(key);

			if (b) {
				if (key.indexOf("paper") == 0) {
					// Log.e("shared", key);
					String[] splits = key.split(":");
					favPaperNames.add(splits[1]);
				}
			}
		}

	}

	private void getFromDate() {
		// TODO this.fromDate Date bealitase ha meg nem lenne beallitva
		// legyen deffault datum
				
			if(currentPriceSeries != null)
			{
				Log.e("torles", "torles");
				currentPriceSeries.clear();
				currentVolumeSeries.clear();

			}
					
			
		 
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DatePicker datePicker = (DatePicker) timeSelectDialog.findViewById(R.date_picker.from_date);
		
		fromDate = new Date();
		
		Log.e("year",""+datePicker.getYear());
		Log.e("month",""+datePicker.getMonth());
		Log.e("day",""+datePicker.getDayOfMonth());
		
		int year = datePicker.getYear();
		int month = datePicker.getMonth();
		int day = datePicker.getDayOfMonth();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		
		
		
		fromDate = calendar.getTime();
		
		Log.e("data", dateFormat.format(fromDate));
		
	}

	private void getPaperName() {
		// TODO this.paperName String ertekenek odaadasa
		
		final AutoCompleteTextView selectorAutoCompleteView = (AutoCompleteTextView) findViewById(R.selector.autocomplete);

		this.paperName = selectorAutoCompleteView.getText().toString();
		
	}

	private void setupRenderer(XYMultipleSeriesRenderer renderer) {

		renderer.setChartTitleTextSize(0);
		renderer.setChartTitle("");
		renderer.setXTitle("");
		renderer.setYTitle("");

		renderer.setShowAxes(false);
		renderer.setAxisTitleTextSize(16);
		renderer.setAxesColor(Color.TRANSPARENT);
		// renderer.setXAxisMin(new Date(108, 9, 1).getTime());
		// renderer.setXAxisMax(new Date(108, 11, 17).getTime());
		// renderer.setYAxisMin(50);
		// renderer.setYAxisMax(200);

		// renderer.setShowLabels(true);
		renderer.setLabelsTextSize(10);
		renderer.setLabelsColor(Color.GRAY);
		renderer.setXLabels(5);
		renderer.setYLabels(10);

		renderer.setShowLegend(false);
		renderer.setLegendTextSize(15);

		renderer.setPointSize(5f);
		renderer.setShowGrid(true);

		// up, left, down, right
		renderer.setMargins(new int[] { 0, 10, 10, 0 });
		renderer.setMarginsColor(Color.parseColor("#1e1e1e"));

		// renderer.setZoomEnabled(true);
		renderer.setExternalZoomEnabled(true);
		renderer.setZoomButtonsVisible(false);
		// renderer.setZoomLimits(null);

		// styles
		XYSeriesRenderer sr;

		sr = new XYSeriesRenderer();
		sr.setColor(Color.GREEN);
		sr.setPointStyle(PointStyle.POINT);
		sr.setDisplayChartValues(true);
		renderer.addSeriesRenderer(sr);

		sr = new XYSeriesRenderer();
		sr.setColor(Color.CYAN);
		sr.setPointStyle(PointStyle.POINT);
		sr.setDisplayChartValues(true);
		renderer.addSeriesRenderer(sr);
	}

	// menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		switch (item.getItemId()) {
		case R.menu.favorites:
			intent = new Intent(getApplicationContext(), Favorites.class);
			intent.putExtra("paperNames", paperNames);
			startActivity(intent);
			return true;
		case R.menu.info:
			Toast.makeText(getApplicationContext(), R.string.app_about, Toast.LENGTH_LONG).show();
			return true;
		case R.menu.quit:
			intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}