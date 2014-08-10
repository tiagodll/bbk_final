package com.dalligna.nfctracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		SharedPreferences settings = getSharedPreferences("TrackYourBag", 0);
		
		if(settings.getString("Persistence", "").equals("Custom"))
			((RadioGroup)findViewById(R.id.settings_persistence)).check(R.id.settings_custom_handle);
		else
			((RadioGroup)findViewById(R.id.settings_persistence)).check(R.id.settings_handle_system);
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
