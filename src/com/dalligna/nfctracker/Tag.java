package com.dalligna.nfctracker;

import org.json.JSONException;
import org.json.JSONObject;

public class Tag {
	public int handle_id, timestamp;
	public String handle, latitude, longitude, comment;
	
	public Tag(){}
	public Tag(String handle, int handle_id, int timestamp, String data){
		this.handle_id = handle_id;
		this.handle = handle;
		this.timestamp = timestamp;
		FromString(data); 
	}
	public Tag(String handle, int handle_id, int timestamp, String latitude, String longitude, String comment){
		this.handle_id = handle_id;
		this.handle = handle;
		this.timestamp = timestamp;
		this.latitude = latitude;
		this.longitude = longitude;
		this.comment = comment;
	}
	
	public String ToString(){
		JSONObject json = new JSONObject();
		try {
			json.put("latitude", latitude);
			json.put("longitude", longitude);
			json.put("comment", comment);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	public void FromString(String data){
		try {
			JSONObject json = new JSONObject(data);
			latitude = json.getString("latitude");
			longitude = json.getString("longitude");
			comment = json.getString("comment");
		} catch (JSONException e) {
			comment = data;
		}
	}
}