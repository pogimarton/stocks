package itk.android.stocks.pages;

import itk.ppke.stock.R;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Favorites extends PreferenceActivity {

	private PreferenceCategory category;
	private List<CheckBoxPreference> prefs;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.favorites);

        //fetch the item where you wish to insert the CheckBoxPreference, in this case a PreferenceCategory with key "targetCategory"
        category = (PreferenceCategory)findPreference("favorite_list");

        prefs = new ArrayList<CheckBoxPreference>();
        
        CheckBoxPreference checkBoxPreference = new CheckBoxPreference(this);
        checkBoxPreference.setTitle("OTP bank");
        checkBoxPreference.setSummary("az otp bank r�szv�nyei");
        checkBoxPreference.setKey("otp");
        checkBoxPreference.setChecked(false);
        prefs.add(checkBoxPreference);
        		
        CheckBoxPreference checkBoxPreference2 = new CheckBoxPreference(this);
        checkBoxPreference2.setTitle("RICHTER");
        checkBoxPreference2.setSummary("gedeon b�csi a n�k b�lv�nya");
        checkBoxPreference2.setKey("ric");
        checkBoxPreference2.setChecked(false);
        prefs.add(checkBoxPreference2);
        
        for (CheckBoxPreference p : prefs) {
        	category.addPreference(p);	
        }
        
        //setContentView(R.layout.favorites);
    }
    
	// menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.favorites, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.menu.all:
			for (CheckBoxPreference p : prefs) {
				p.setChecked(true);
			}
			return true;
		case R.menu.none:
			for (CheckBoxPreference p : prefs) {
				p.setChecked(false);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}