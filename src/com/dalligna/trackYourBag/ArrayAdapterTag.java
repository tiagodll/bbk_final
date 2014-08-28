package com.dalligna.trackYourBag;

import java.util.List;

import com.dalligna.nfctracker.R;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ArrayAdapterTag extends ArrayAdapter<Tag> {
	private LayoutInflater inflater;
	Context context;
	int layoutResourceId;
	List<Tag> data = null;
	AlertDialog mActions;
	
	public ArrayAdapterTag(Context context, int layoutResourceId, List<Tag> listContent) {
		super(context, layoutResourceId, listContent);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.data = listContent;
	}
		
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_tag, parent, false);

		Tag tag = data.get(position);
		
		TextView textViewItem1 = (TextView) convertView.findViewById(R.id.tagId);
		textViewItem1.setText("" + tag.handle_id);
		
		TextView textViewItem2 = (TextView) convertView.findViewById(R.id.tagText);
		textViewItem2.setText("" + tag.timestamp);
		
		((Button)convertView.findViewById(R.id.mapButton)).setOnClickListener(new OnClickListener() { 
	    	@Override
	        public void onClick(View v) {
	    		Toast.makeText(v.getContext(), "click!", Toast.LENGTH_LONG).show();
	    		
	    		Activity act = (Activity)context.getApplicationContext();
	    		MapDialog dialog = new MapDialog();
	    		Bundle args = new Bundle();
	    		dialog.setArguments(args);
	    		dialog.show(act.getFragmentManager(), "map");
	    	}
	    });//*/
		return convertView;
	}
}