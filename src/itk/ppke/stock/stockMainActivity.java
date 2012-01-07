package itk.ppke.stock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class stockMainActivity extends Activity {
	/** Called when the activity is first created. */

	public Handler stockDataHandler;
	public Handler paperNameHandler;
	public Handler infoBarHandler;
	private TextView tw;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//tw = (TextView) findViewById(R.id.tw);

	
/*		paperNameHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
				if (msg.what == 0) {

					String s = msg.getData().getString("paperName");
					tw.append(s + "\n");

				}
			}
		};
		
		GetPaperNameThread getPaperNameThread = new GetPaperNameThread(paperNameHandler);
		
		
		*/
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		
		stockDataHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
				if (msg.what == 0) {
					String s = msg.getData().getString("paperName") + " " + 
					 msg.getData().getString("price") ;
					

					tw.append(s + "\n");

				}
			}
		};
		String paperName = "OTP";
		Date fromInterval = null;
		Date toInterval = null;
		try {
			fromInterval = dateFormat.parse("2008-02-22");
			toInterval = dateFormat.parse("2008-02-26");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GetDataThread getDataThread = new GetDataThread(stockDataHandler, paperName,fromInterval, toInterval, getApplicationContext());
		//Thread thread = new Thread(null, stockDataSaveAndDownload, "Background");

		//thread.setPriority(Thread.MAX_PRIORITY);
		//thread.start();
		
		
		infoBarHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
				if (msg.what == 0) {
					String s = msg.getData().getString("paperName") + " " + 
					 msg.getData().getString("date") + " " +
					 msg.getData().getString("time") + " " +
					 msg.getData().getString("price") + " " +
					 msg.getData().getString("volume");

					tw.append(s + "\n");

				}
			}
		};
		

	}

	
	private Runnable getInfobar = new Runnable(){

		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	};

	
	
}