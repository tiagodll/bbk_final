package com.dalligna.trackYourBag;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

public class HandleSystemTester extends AndroidTestCase {	
	HandleSystem p = new HandleSystem();
	
	public void test1CreateHandle() {
		long start = Calendar.getInstance().getTimeInMillis();
		boolean val = p.Create(getContext(), "test5");
		
		long stop = Calendar.getInstance().getTimeInMillis();
		Date d = new Date(stop-start-3600*1000);
		Log.i("Test", "UpdateHandle total time: " + DateFormat.getTimeInstance().format(d));
		assert(val);
	}
	
	public void test2UpdateHandle() {
		long start = Calendar.getInstance().getTimeInMillis();
		boolean val = p.Update(getContext(), new Tag("test5", 5, 
				(int)(System.currentTimeMillis() / 1000L), 
				"The test content"));
		long stop = Calendar.getInstance().getTimeInMillis();
		Date d = new Date(stop-start-3600*1000);
		Log.i("Test", "UpdateHandle total time: " + DateFormat.getTimeInstance().format(d));
		assert(val);
	}
	
	public void test3RetrieveHandle() {
		long start = Calendar.getInstance().getTimeInMillis();
		List<Tag> values = p.Retrieve("test5");
		
		long stop = Calendar.getInstance().getTimeInMillis();
		Date d = new Date(stop-start-3600*1000);
		Log.i("Test", "UpdateHandle total time: " + DateFormat.getTimeInstance().format(d));
		assert(values.size() > 0);
	}
	
	public void test4DeleteHandle() {
		long start = Calendar.getInstance().getTimeInMillis();
		boolean val = p.Delete(getContext(), "test5");

		long stop = Calendar.getInstance().getTimeInMillis();
		Date d = new Date(stop-start-3600*1000);
		Log.i("Test", "UpdateHandle total time: " + DateFormat.getTimeInstance().format(d));
		assert(val);
	}//*/
}
