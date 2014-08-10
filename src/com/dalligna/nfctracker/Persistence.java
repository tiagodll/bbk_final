package com.dalligna.nfctracker;

import java.util.List;

import android.content.Context;

public interface Persistence {
	public List<Tag> Retrieve(String handle);
	public boolean Create(Context ctx, String handle);
	public boolean Update(Context ctx, Tag tag);
	public boolean Delete(Context ctx, String handle);
}
