package com.dalligna.nfctracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

public class Util {
	public boolean checkOnlineState(){
		try {
			return InetAddress.getByName("http://nfctrackerapp.herokuapp.com").isReachable(1000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String readRawTextFile(Context ctx, int resId)
	{
	    InputStream inputStream = ctx.getResources().openRawResource(resId);

	    InputStreamReader inputreader = new InputStreamReader(inputStream);
	    BufferedReader buffreader = new BufferedReader(inputreader);
	    String line;
	    StringBuilder text = new StringBuilder();

	    try {
	        while (( line = buffreader.readLine()) != null) {
	            text.append(line);
	            text.append('\n');
	        }
	    } catch (IOException e) {
	        return null;
	    }
	    return text.toString();
	}
	
	public byte[] readRawBinFile(Context ctx, int resId)
	{
		AssetFileDescriptor sampleFD = ctx.getResources().openRawResourceFd(resId);
		long size = sampleFD.getLength();
				
	    InputStream is = ctx.getResources().openRawResource(resId);
	    byte[] bin = new byte[(int)size];
	    try {
			is.read(bin);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return bin;
	}
	
	static public String ByteArrayToHexString(byte [] inarray) {
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
}
