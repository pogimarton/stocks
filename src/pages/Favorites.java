package pages;

import itk.ppke.stock.R;

import java.util.ArrayList;
import java.util.Arrays;
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
        String[] paperNames = getIntent().getStringArrayExtra("paperNames");
        
        Arrays.sort(paperNames, String.CASE_INSENSITIVE_ORDER);
        for (String paper : paperNames) {
            CheckBoxPreference checkBoxPreference = new CheckBoxPreference(this);
            checkBoxPreference.setTitle(paper);
            checkBoxPreference.setKey("paper:" + paper);
            checkBoxPreference.setChecked(false);
            prefs.add(checkBoxPreference);
        }
        
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