package com.dalligna.trackYourBag;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Tag {
	public int handle_id;
	public long timestamp;
	public String handle, latitude, longitude, comment;
	
	public Tag(){}
	public Tag(String handle, int handle_id, long timestamp, String data){
		this.handle_id = handle_id;
		this.handle = handle;
		this.timestamp = timestamp;
		FromJson(data); 
	}
	public Tag(String handle, int handle_id, long timestamp, String latitude, String longitude, String comment){
		this.handle_id = handle_id;
		this.handle = handle;
		this.timestamp = timestamp;
		this.latitude = latitude;
		this.longitude = longitude;
		this.comment = comment;
	}
	
	public String ToString(){
		Date d = new Date(this.timestamp);
		return d + " - lat:" + this.latitude + " long:" + this.longitude + " comment: " + this.comment;
	}
	
	public String ToJson(){
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
	
	public void FromJson(String data){
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