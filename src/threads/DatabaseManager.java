package threads;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import shared.PriceAndVolume;
import shared.StockDate;
import shared.StockTime;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseManager {

	static SQLiteDatabase db;

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "stock_db";
	private static DatabaseHelper mDbHelper = null;

	private static final String StockDataTableName = "favStockData";
	private static final String idColumnName = "id";
	public static final String paperNameColumnName = "paperName";
	public static final String tradeDateColumnName = "tradeDate";
	public static final String tradeTimeColumnName = "tradeTime";
	public static final String priceColumnName = "price";
	public static final String volumeColumnName = "volume";

	private static final String CREATE_TABLE_StockData = "CREATE TABLE IF NOT EXISTS " + StockDataTableName + " ( " + idColumnName + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + paperNameColumnName + " VARCHAR(32) , " + tradeDateColumnName + " DATE , " + tradeTimeColumnName + " DATETIME , " + priceColumnName + " FLOAT , " + volumeColumnName + " BIGINT " + ");";

	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(CREATE_TABLE_StockData);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	public DatabaseManager(Context inC) {
		mDbHelper = new DatabaseHelper(inC);
		db = mDbHelper.getWritableDatabase();
	}

	public void close() {
		mDbHelper.close();
	}

	public Cursor getStockDatabyDay(String name, StockDate stockDate) {
		
		Cursor c = db.query(StockDataTableName, null, "paperName = '" + name + "' and tradeDate = '" + stockDate.toString() + "'", null, null, null, null);

		
		return c;

	}

	public Cursor getNewestData(String paperName) {

		// "SELECT price from RawStockData where paperName = '" + paperN +
		// "' order by tradeDate desc, tradeTime desc Limit 1"

		Cursor c = db.query(StockDataTableName, null, "paperName = ? ", new String[] {paperName}, null, null, "tradeDate desc, tradeTime desc", "1");

		return c;
	}

	public Cursor getOldestData(String paperName) {

		// "SELECT price from RawStockData where paperName = '" + paperN +
		// "' order by tradeDate desc, tradeTime desc Limit 1"

		Cursor c = db.query(StockDataTableName, null, "paperName = ? ", new String[] {paperName}, null, null, "tradeDate asc, tradeTime asc", "1");

		
		
		return c;
	}

	public long insertStockData(String paperName, StockDate date, StockTime time, PriceAndVolume priceAndVolume) {
		
	
		ContentValues initialValues = new ContentValues();

		initialValues.put(paperNameColumnName, "'" + paperName + "'");
		initialValues.put(tradeDateColumnName, date.toString());
		initialValues.put(tradeTimeColumnName, time.toString());
		initialValues.put(priceColumnName, priceAndVolume.getPrice());
		initialValues.put(volumeColumnName, priceAndVolume.getVolume());

		return db.insert(StockDataTableName, null, initialValues);
	}

	public void insertStockData(HashMap<String, TreeMap<StockDate, TreeMap<StockTime, PriceAndVolume>>> stockData) {
		ContentValues initialValues;
		String paperName;
		StockDate date;
		StockTime time;
		PriceAndVolume priceAndVolume;

		for (Iterator<String> itpN = stockData.keySet().iterator(); itpN.hasNext();) {
			paperName = itpN.next();

			for (Iterator<StockDate> ittD = stockData.get(paperName).keySet().iterator(); ittD.hasNext();) {
				date = ittD.next();
				for (Iterator<StockTime> itsT = stockData.get(paperName).get(date).keySet().iterator(); itsT.hasNext();) {
					time = itsT.next();
					priceAndVolume = stockData.get(paperName).get(date).get(time);

					initialValues = new ContentValues();

					initialValues.put(paperNameColumnName, paperName);
					initialValues.put(tradeDateColumnName, date.toString());
					initialValues.put(tradeTimeColumnName, time.toString());
					initialValues.put(priceColumnName, priceAndVolume.getPrice());
					initialValues.put(volumeColumnName, priceAndVolume.getVolume());
					// Log.e("time" ,time.toString());

					db.insert(StockDataTableName, null, initialValues);
					// Log.e("$$$$$$", db.insert(StockDataTableName, null,
					// initialValues)+"");

				}

			}
		}

	}

	public long deleteStockData(String paperName) {
		return db.delete(StockDataTableName, "paperName = ?", new String[] { paperName });
	}

	public long deleteStockData(String paperName, StockDate date) {
		return db.delete(StockDataTableName, "paperName = ? and tradeDate = ? ", new String[] { paperName, date.toString() });
	}

	public void drop() {
		db.execSQL("Drop table " + StockDataTableName + ";");
	}

	// public long insertNote(Note note) {
	// ContentValues initialValues = new ContentValues();
	// initialValues.put(TITLE, note.getTitle());
	// initialValues.put(DESCRIPTION, note.getDescription());
	// return db.insert(NOTES_TABLE, null, initialValues);
	// }
	//
	// public Cursor getAllNotes() {
	// Cursor c = db.query(NOTES_TABLE, new String[] { ID, TITLE, DESCRIPTION },
	// null, null, null, null, null);
	// return c;
	// }

}
