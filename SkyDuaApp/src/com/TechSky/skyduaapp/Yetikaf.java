package com.TechSky.skyduaapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Yetikaf extends Activity {
TextView tv1,tv2,tv3,tv4;
Button finish;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.yetikaf);
	   tv1 = (TextView) findViewById(R.id.textView1);
	   tv2 = (TextView) findViewById(R.id.textView2);
	   tv3 = (TextView) findViewById(R.id.textView3);
	   tv4 = (TextView) findViewById(R.id.textView4);
	   finish = (Button) findViewById(R.id.button1);
	   finish.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(Yetikaf.this, "Assalamuwalyikum",Toast.LENGTH_LONG).show();
			finish();
		}
	});
			   
	
	    // TODO Auto-generated method stub
	}

}
