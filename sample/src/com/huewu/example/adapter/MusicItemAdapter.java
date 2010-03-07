package com.huewu.example.adapter;

import com.huewu.example.R;
import com.huewu.example.provider.IMusicItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MusicItemAdapter extends ArrayAdapter<IMusicItem>{
	
	LayoutInflater inflater = null;
	int layout = 0;

	public MusicItemAdapter(Context context, int layout,
			IMusicItem[] objects) {
		super(context, layout, objects);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = (View)inflater.inflate(layout, null);
		
		TextView text = (TextView) convertView.findViewById(R.id.title);
		text.setText(getItem(position).getTtitle());
		return convertView;
	}
}//end of class
