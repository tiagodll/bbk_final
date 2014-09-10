package com.dalligna.trackYourBag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		SharedPreferences settings = getSharedPreferences("TrackYourBag", 0);
		
		SharedPreferences.Editor editor = settings.edit();
	    editor.putString("Persistence", "Custom");
	    editor.commit();
		
		/*if(settings.getString("Persistence", "").equals("Custom"))
			((RadioGroup)findViewById(R.id.settings_persistence)).check(R.id.settings_custom_handle);
		else
			((RadioGroup)findViewById(R.id.settings_persistence)).check(R.id.settings_handle_system); //*/
		
		((Button)findViewById(R.id.searchTagByIdButton)).setOnClickListener(new OnClickListener() { 
	    	@Override
	        public void onClick(View v) {
	    		Context context = getApplicationContext();
	    		String tagid = ((EditText)findViewById(R.id.searchTagByIdText)).getText().toString();
	    		if(tagid.equals("")){
	    			Toast.makeText(context, "Please insert a tag id to search", Toast.LENGTH_SHORT).show();
	    		}else{
		    		Intent intent = new Intent(MainActivity.this, Reader.class);
			        intent.putExtra("tag", tagid);
			        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		   			context.startActivity(intent);
		   			finish();
	    		}
	    	}
	    });
		
		((Button)findViewById(R.id.close_button)).setOnClickListener(new OnClickListener() { 
	    	@Override
	        public void onClick(View v) {
	    		finish();
	            System.exit(0);
	    	}
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void setPersistence(View view) {
	    boolean checked = ((RadioButton) view).isChecked();
	    String persistence = "";

	    switch(view.getId()) {
	        case R.id.settings_handle_system:
	            if (checked)
	            	persistence = "Handle";
	            break;
	        case R.id.settings_custom_handle:
	            if (checked)
	            	persistence = "Custom";
	            break;
	    }
	    	    
	    SharedPreferences settings = getSharedPreferences("TrackYourBag", 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString("Persistence", persistence);
	    editor.commit();
	}
	
}
