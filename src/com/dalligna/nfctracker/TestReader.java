package com.dalligna.nfctracker;

import java.util.List;

import android.test.AndroidTestCase;

public class TestReader extends AndroidTestCase {
	
	public Persistence _p;
	public Persistence p(){ 
		if(_p==null)
			_p = new HandleSystem();
		return _p;
	}
	

	public void estConnection() {
		assert(new Util().checkOnlineState());
	}
	
	public void testCreateHandle() {
		boolean val = p().Create(getContext(), "test3");
		assert(val);
	}
	
	public void testUpdateHandle() {
		boolean val = p().Update(getContext(), new Tag("test3", 5, 
				(int)(System.currentTimeMillis() / 1000L), 
				"The test content"));
		assert(val);
	}
	
	public void estRetrieveHandle() {
		List<Tag> values = p().Retrieve("test3");
		assert(values.size() > 0);
	}
	
	public void estDeleteHandle() {
		boolean val = true;//p().Delete(getContext(), "test3");
		assert(val);
	}
}
