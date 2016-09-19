package com.TechSky.skyduaapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Sonekiduva extends Activity {
Button finish;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.sonekiduva);
	finish = (Button) findViewById(R.id.button1);
	finish.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		
			finish();
			
		}
	});
	    // TODO Auto-generated method stub
	}

}
