package pages.sandbox;

import itk.ppke.stock.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import android.view.Menu;
import android.view.MenuItem;

public class Settings extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	// menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, 0, "Show current settings");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			//startActivity(new Intent(this, ShowSettingsActivity.class));
			return true;
		}
		return false;
	}

}
