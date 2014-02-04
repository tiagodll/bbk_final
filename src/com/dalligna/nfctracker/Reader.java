package com.dalligna.nfctracker;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Reader extends Activity {
	NFCForegroundUtil nfcForegroundUtil = null;
	double latitude, longitude;
	String tag = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.reader);	    
	    nfcForegroundUtil = new NFCForegroundUtil(this);
	    
	    ((Button)findViewById(R.id.saveButton)).setOnClickListener(new OnClickListener() { 
	    	@Override
	        public void onClick(View v) { makeWebRequest(true, v); }
	    });
	}

	public void onPause() {
	    super.onPause();
	    nfcForegroundUtil.disableForeground();
	}
	
	public void onResume() {
	    super.onResume();
	    nfcForegroundUtil.enableForeground();

	    if (!nfcForegroundUtil.getNfc().isEnabled())
	    {
	        Toast.makeText(getApplicationContext(), "Please activate NFC and press Back to return to the application!", Toast.LENGTH_LONG).show();
	        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
	    }else{
	    	handleIntent(getIntent());
	    }
	}

	public void onNewIntent(Intent intent) {
	    handleIntent(intent);
	}
	
	public void handleIntent(Intent intent){
		tag = new String(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID), Charset.forName("US-ASCII"));
		((TextView)getWindow().findViewById(R.id.theText)).setText(tag);
		
		makeWebRequest(false, null);
	}
	
	public void makeWebRequest(boolean write, View v)
	{
		GetRemoteString getstring = new GetRemoteString(getApplicationContext(), getWindow().getDecorView());
		try { getstring.setFinishMethod(Reader.class.getMethod("readNfcsFromWeb", new Class[] {String.class, Context.class, View.class})); } catch (NoSuchMethodException e) { e.printStackTrace(); }
		String location = getLocation();
		TelephonyManager device = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String url = "http://nfctrackerapp.herokuapp.com/tag/" + URLEncoder.encode(tag);
		if(write){
			url += "?type=nfc"
				+ "&location=" + URLEncoder.encode(location)
				+ "&comment=" + URLEncoder.encode(((EditText)v.getRootView().findViewById(R.id.txtComment)).getText().toString());
				//+ "&device=" + URLEncoder.encode(device.getDeviceId()));
		}else{
			url += "?type=read";
		}
		getstring.execute(url);
	}
	
	static public void readNfcsFromWeb(String urlcontent, Context context, View view){
		String strtag = "";
		JSONObject json;
		try {
			json = new JSONObject(urlcontent);
			strtag += "ID: " + json.get("id");
			strtag += "type: " + json.get("type");
			((TextView)view.findViewById(R.id.theText)).setText(strtag);
			
			ArrayList<String> listContent = new ArrayList<String>();
			
			JSONArray history = (JSONArray) json.get("history");
			for(int i=0; i<history.length(); i++)
			{
				JSONObject h = history.getJSONObject(i);
				listContent.add(h.get("time") + " - " + h.get("location") + " - " + h.get("comment"));
			}
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listContent);
			((ListView)view.findViewById(R.id.historyView)).setAdapter(adapter);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* ############################## GEOLOCATION PART ############################## */
	private String getLocation()
	{
	    LocationManager locationManager = (LocationManager)getSystemService (LOCATION_SERVICE);
	    String bestProvider = locationManager.getBestProvider (new Criteria (), false);
	    locationManager.requestLocationUpdates(bestProvider,0 ,0, loc_listener);
		Location location = locationManager.getLastKnownLocation (bestProvider);
		try
		{
			latitude = location.getLatitude ();
			longitude = location.getLongitude ();
		}
		catch (NullPointerException e)
		{
			latitude = -1.0;
			longitude = -1.0;
		}
		return "lat: " + latitude + " lon: " + longitude;
	}
	LocationListener loc_listener = new LocationListener() {
		  public void onLocationChanged(Location l) { }
		  public void onProviderEnabled(String p) { }
		  public void onProviderDisabled(String p) { }
		  public void onStatusChanged(String p, int status, Bundle extras) { }      
	};
}