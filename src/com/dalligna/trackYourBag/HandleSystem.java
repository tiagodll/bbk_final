package com.dalligna.trackYourBag;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import com.dalligna.trackYourBag.R;

import net.handle.hdllib.AbstractMessage;
import net.handle.hdllib.AbstractResponse;
import net.handle.hdllib.AddValueRequest;
import net.handle.hdllib.AdminRecord;
import net.handle.hdllib.Common;
import net.handle.hdllib.CreateHandleRequest;
import net.handle.hdllib.DeleteHandleRequest;
import net.handle.hdllib.Encoder;
import net.handle.hdllib.HandleException;
import net.handle.hdllib.HandleResolver;
import net.handle.hdllib.HandleValue;
import net.handle.hdllib.PublicKeyAuthenticationInfo;
import net.handle.hdllib.ResolutionRequest;
import net.handle.hdllib.ResolutionResponse;
import android.content.Context;

public class HandleSystem implements Persistence {
	static final String hdl = "10763";
	static final String auth_hdl = "0.NA/" + hdl;
	static final int auth_index = 300;
	  
	public List<Tag> Retrieve(String handle) {
		byte someHandle[] = null;
		try {
			someHandle = (hdl + "/" + handle).getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<Tag> docs = new ArrayList<Tag>();

		ResolutionRequest request =  new ResolutionRequest(someHandle, null, null, null);
		HandleResolver resolver = new HandleResolver();
		AbstractResponse response;
		try {
			response = resolver.processRequest(request);
			if(response.responseCode == AbstractMessage.RC_SUCCESS) {
			  HandleValue values[] = ((ResolutionResponse)response).getHandleValues();
			  for(int i=0; i < values.length; i++) {
				  docs.add(new Tag(handle, values[i].getIndex(), values[i].getTimestamp(), values[i].getDataAsString()));
			  }
			  return docs;
			}
		} catch (HandleException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//java net.handle.apps.simple.HDLCreate <auth handle> <auth index> <privkey> <handle>
	public boolean Create(Context ctx, String handle){
		try {
		  PrivateKey privkey = getPrivKey(ctx);
		  byte[] auth_handle = auth_hdl.getBytes("UTF8");
		  byte[] service_handle = (hdl + "/" + handle).getBytes("UTF8");		
	      PublicKeyAuthenticationInfo auth = new PublicKeyAuthenticationInfo(auth_handle, auth_index, privkey);
	      
	      HandleValue[] val = { new HandleValue(100, "HS_ADMIN".getBytes("UTF8"), Encoder.encodeAdminRecord(getAdmin()), HandleValue.TTL_TYPE_RELATIVE, 86400, (int)(System.currentTimeMillis()/1000), null, true, true, true, false) };
	      CreateHandleRequest req = new CreateHandleRequest(service_handle, val, auth);
	      HandleResolver resolver = new HandleResolver();
	      resolver.traceMessages = true;
	      AbstractResponse response = resolver.processRequest(req);

	      // RC_SUCCESS, RC_ERROR, RC_INVALID_ADMIN, and RC_INSUFFICIENT_PERMISSIONS.
	      if (response.responseCode == AbstractMessage.RC_SUCCESS){
	         System.out.println("\nGot Response: \n"+response);
	         return true;
	      }
	      else {
	        System.out.println("\nGot Error: \n"+response);
	      }
	    } catch (Throwable t) {
	      System.err.println("\nError: "+t);
	    }
	    return false;
	}
	
	//java net.handle.apps.simple.HDLSetServiceHandle <auth handle> <privkey> <service handle> <prefixfile>
	public boolean Update(Context ctx, Tag tag){
		PrivateKey privkey = getPrivKey(ctx);
		byte[] auth_handle = null;
		try { auth_handle = auth_hdl.getBytes("UTF8"); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		byte[] service_handle = null;
		try { service_handle = (hdl + "/" + tag.handle_id).getBytes("UTF8"); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }		
		byte[] handle_data = null;
		try { handle_data = tag.ToString().getBytes("UTF8"); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }		
		PublicKeyAuthenticationInfo auth = new PublicKeyAuthenticationInfo(auth_handle, auth_index, privkey);

		ResolutionRequest rreq = new ResolutionRequest(service_handle, null, null, null);
		rreq.authoritative = true;
		HandleResolver resolver = new HandleResolver();
		HandleValue newServValue = new HandleValue(103, Common.BLANK_HANDLE, handle_data);
		AddValueRequest addreq = new AddValueRequest(rreq.handle, newServValue, auth);
		try {
			AbstractResponse addresp = resolver.processRequest(addreq);
			if(addresp!=null && addresp.responseCode==AbstractMessage.RC_SUCCESS) {
			  System.err.println("Error adding HS_SERV for prefix '" + hdl + "/" + tag.handle_id + "': " + addresp);
			}
		} catch (HandleException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//java net.handle.apps.simple.HDLCreate <auth handle> <auth index> <privkey> <handle>
	public boolean Delete(Context ctx, String handle){
		try {
		  PrivateKey privkey = getPrivKey(ctx);
		  byte[] auth_handle = auth_hdl.getBytes("UTF8");
		  byte[] service_handle = (hdl + "/" + handle).getBytes("UTF8");
	      PublicKeyAuthenticationInfo auth = new PublicKeyAuthenticationInfo(auth_handle, auth_index, privkey);
	      
	      DeleteHandleRequest req = new DeleteHandleRequest(service_handle, auth);
	      HandleResolver resolver = new HandleResolver();
	      resolver.traceMessages = true;
	      AbstractResponse response = resolver.processRequest(req);

	      // RC_SUCCESS, RC_ERROR, RC_INVALID_ADMIN, and RC_INSUFFICIENT_PERMISSIONS.
	      if (response.responseCode == AbstractMessage.RC_SUCCESS){
	         System.out.println("\nGot Response: \n"+response);
	         return true;
	      }
	      else {
	        System.out.println("\nGot Error: \n"+response);
	      }
	    } catch (Throwable t) {
	      System.err.println("\nError: "+t);
	    }
	    return false;
	}
	
	public AdminRecord getAdmin(){
		try {
			return new AdminRecord(auth_hdl.getBytes("UTF8"), auth_index, true, true , true, true, true, true, true, true, true, true, true, true);
		} catch (Throwable t) {
			System.err.println("\nError: "+t);
	    }
	    return null;
	}
	public PrivateKey getPrivKey(Context ctx){
		byte[] key = new Util().readRawBinFile(ctx, R.raw.admpriv);
		PrivateKey privkey = null;
	    byte secKey[] = null;
	    try {
	      if(net.handle.hdllib.Util.requiresSecretKey(key)){
	        secKey = "paul1chartier".getBytes("UTF8");
	      }
	      key = net.handle.hdllib.Util.decrypt(key, secKey);
	      privkey = net.handle.hdllib.Util.getPrivateKeyFromBytes(key, 0);
	    }
	    catch (Throwable t){
	      System.err.println("Can't load private key: " +t);
	      return null;
	    }
	    return privkey;
	}
}
