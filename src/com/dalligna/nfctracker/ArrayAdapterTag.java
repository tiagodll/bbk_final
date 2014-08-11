package com.dalligna.nfctracker;

import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterTag extends ArrayAdapter<Tag> {
	private LayoutInflater inflater;
	Context context;
	int layoutResourceId;
	List<Tag> data = null;
	
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
		textViewItem2.setText(tag.ToString());
		//textViewItem.setTag(objectItem.itemId);
		return convertView;
	}
}