package com.TechSky.skyduaapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Khanakhanekidua extends Activity {
Button finish;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	setContentView(R.layout.khanekidua);
	
	    // TODO Auto-generated method stub
	
		finish = (Button) findViewById(R.id.button1);
		finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(Khanakhanekidua.this, "assalamuwalyikum", Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}

}
