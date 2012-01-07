package itk.android.stocks.pages;

import itk.ppke.stock.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Filters extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters);
    }
    
	// menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.filters, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.menu.favorites:
			startActivity(new Intent(getApplicationContext(), Favorites.class));
			return true;
		case R.menu.info:
			Toast.makeText(getApplicationContext(), "info", Toast.LENGTH_SHORT).show();
			return true;
		case R.menu.settings:
			Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
			return true;
		case R.menu.quit:
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}