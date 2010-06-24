package com.huewu.example.checkable;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CheckableLayout extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setListAdapter(
				new ArrayAdapter<String>(this, R.layout.my_list_item, android.R.id.text1, GENRES));

		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}


	private static final String[] GENRES = new String[] {
		"Action", "Adventure", "Animation", "Children", "Comedy", "Documentary", "Drama",
		"Foreign", "History", "Independent", "Romance", "Sci-Fi", "Television", "Thriller"
	};
}