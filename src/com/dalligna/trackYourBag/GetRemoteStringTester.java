package com.dalligna.trackYourBag;

import android.content.Context;
import android.test.AndroidTestCase;
import android.view.LayoutInflater;
import android.view.View;

public class GetRemoteStringTester extends AndroidTestCase {
	
	public void test1ReadHtml() {
		Context context = getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.reader, null);
		GetRemoteString getstring = new GetRemoteString(context, view);  
		try { getstring.setFinishMethod(GetRemoteStringTester.class.getMethod("readNfcsFromWeb", new Class[] {String.class, Context.class, View.class})); } catch (NoSuchMethodException e) { e.printStackTrace(); }
		getstring.execute("http://nfctrackerapp.herokuapp.com/");
	}
	
	static public void readNfcsFromWeb(String urlcontent, Context context, View view){
		
		assertTrue(urlcontent.length() > 0);
	}
}