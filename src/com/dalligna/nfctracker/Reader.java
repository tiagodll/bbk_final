package com.dalligna.nfctracker;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.util.Log;
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
	SharedPreferences settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.reader);	    
	    nfcForegroundUtil = new NFCForegroundUtil(this);
	    settings = getSharedPreferences("TrackYourBag", 0);
	    
	    ((Button)findViewById(R.id.saveButton)).setOnClickListener(new OnClickListener() { 
	    	@Override
	        public void onClick(View v) {
	    		if(settings.getString("Persistence", "").equals("Custom"))
	    			makeWebRequest(true, v);
	    		else
	    			makeHandleRequest(true, v);
	    	}
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
		//tag = new String(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID), Charset.forName("US-ASCII"));
		byte[] extraid = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		tag = ByteArrayToHexString(extraid);
        
		((TextView)getWindow().findViewById(R.id.theText)).setText(tag);
		
		if(settings.getString("Persistence", "").equals("Custom"))
			makeWebRequest(false, null);
		else
			makeHandleRequest(false, null);
	}
	private String ByteArrayToHexString(byte [] inarray) {
	    int i, j, in;
	    String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	    String out= "";
	 
	    for(j = 0 ; j < inarray.length ; ++j) 
	        {
	        in = (int) inarray[j] & 0xff;
	        i = (in >> 4) & 0x0f;
	        out += hex[i];
	        i = in & 0x0f;
	        out += hex[i];
	        }
	    return out;
	}
	
	public void makeHandleRequest(boolean write, View v){
		ArrayList<String> listContent = new ArrayList<String>();
		
		HandleSystem p = new HandleSystem();
		List<Tag> list = p.Retrieve(tag);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listContent);
		((ListView)getWindow().findViewById(R.id.historyView)).setAdapter(adapter);
	}
	
	static public void readNfcsFromHandle(String urlcontent, Context context, View view){

		List<Tag> listContent = new HandleSystem().Retrieve(urlcontent);
		
		ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(context, android.R.layout.simple_list_item_1, listContent);
		((ListView)view.findViewById(R.id.historyView)).setAdapter(adapter);
	}
	
	public void makeWebRequest(boolean write, View v)
	{
		GetRemoteString getstring = new GetRemoteString(getApplicationContext(), getWindow().getDecorView());
		try { getstring.setFinishMethod(Reader.class.getMethod("readNfcsFromWeb", new Class[] {String.class, Context.class, View.class})); } catch (NoSuchMethodException e) { e.printStackTrace(); }
		getLocation();
		String url = "http://nfctrackerapp.herokuapp.com/tag/" + URLEncoder.encode(tag);
		if(write){
			url += "?type=nfc"
				+ "&latitude=" + latitude
				+ "&longitude=" + longitude
				+ "&comment=" + URLEncoder.encode(((EditText)v.getRootView().findViewById(R.id.txtComment)).getText().toString());
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
			
			ArrayList<Tag> listContent = new ArrayList<Tag>();
			
			JSONArray history = (JSONArray) json.get("history");
			for(int i=0; i<history.length(); i++)
			{
				JSONObject h = history.getJSONObject(i);
				listContent.add(new Tag(json.getString("id"), 1, h.getInt("time"), h.getString("latitude"), h.getString("longitude"), h.getString("comment")));
			}
			
			ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(context, android.R.layout.simple_list_item_1, listContent);
			((ListView)view.findViewById(R.id.historyView)).setAdapter(adapter);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/* ############################## GEOLOCATION PART ############################## */
	private boolean getLocation()
	{
	    LocationManager locationManager = (LocationManager)getSystemService (LOCATION_SERVICE);
	    String bestProvider = locationManager.getBestProvider (new Criteria (), false);
	    locationManager.requestLocationUpdates(bestProvider,0 ,0, loc_listener);
		Location location = locationManager.getLastKnownLocation (bestProvider);
		try
		{
			latitude = location.getLatitude ();
			longitude = location.getLongitude ();
			return true;
		}
		catch (NullPointerException e)
		{
			latitude = -1.0;
			longitude = -1.0;
			return false;
		}
	}
	LocationListener loc_listener = new LocationListener() {
		  public void onLocationChanged(Location l) { }
		  public void onProviderEnabled(String p) { }
		  public void onProviderDisabled(String p) { }
		  public void onStatusChanged(String p, int status, Bundle extras) { }      
	};
}