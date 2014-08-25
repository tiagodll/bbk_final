package com.dalligna.trackYourBag;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

public class GetRemoteString extends AsyncTask<String, Integer, String> {
	Context ctx = null;
	View view = null;
	private Method doFinish = null;	
	public void setFinishMethod(Method m){ this.doFinish = m; }
	
	public GetRemoteString(Context c, View v)
	{
		this.ctx = c;
		this.view = v;
	}

	protected String doInBackground(String... urls) {
		 String xml = null;
	     StringBuilder sb = new StringBuilder();
		try {
			InputStream is = new URL(urls[0]).openStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
	        String str;
	        while ((str = in.readLine()) != null) {
	            sb.append(str);
	        }
	        in.close();	
	        xml = sb.toString();
    	} catch (Exception e) {	        
	    }
		return xml;
    }
	
	protected void onPostExecute(String result) {
    	Object[] args = { String.valueOf(result), ctx, view};
    	if(doFinish != null)
			try {
				doFinish.invoke(null, args);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
    }
}