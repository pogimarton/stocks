package pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ChartSelector extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final List<String> favoriteList = new ArrayList<String>();

		SharedPreferences prefManager = PreferenceManager.getDefaultSharedPreferences(this);
		Map<String, ?> all = prefManager.getAll();

		for (String key : all.keySet()) {
			Boolean b = (Boolean) all.get(key);

			if (b) {
				if (key.indexOf("paper") == 0) {
					// Log.e("shared", key);
					String[] splits = key.split(":");
					favoriteList.add(splits[1]);
				}
			}
		}
		
		Collections.sort(favoriteList);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, android.R.id.text1, favoriteList);
		setListAdapter(adapter);
		
		ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String selectedPaper = favoriteList.get(position);
				
				//Toast.makeText(getApplicationContext(), "Selected: " + selectedPaper, Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(getApplicationContext(), Main.class);
				intent.putExtra("selected", selectedPaper);
				startActivity(intent);
				
				finish();
			}
			
		});
	}

}
