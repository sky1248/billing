package com.TechSky.skyduaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DifferentDuas extends Activity {

	Button b1,b2,b3,b4,b5,b6,b7,b8;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.differentduas);
	    b1 = (Button) findViewById(R.id.button1);
	    b2 = (Button) findViewById(R.id.button2);
	    b3 = (Button) findViewById(R.id.button3);
	    b4 = (Button) findViewById(R.id.button4);
	    b5 = (Button) findViewById(R.id.button5);
	    
	    b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in =new Intent(DifferentDuas.this,Sonekiduva.class);
				startActivity(in);
				
				
			}
		});
	    
	    
	    b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in1 = new Intent(DifferentDuas.this,Sonekiduva.class);
				startActivity(in1);
			}
		});
	    b3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in2 = new Intent(DifferentDuas.this, Namazsurahs.class);
				startActivity(in2);
			}
		});
	    
	    b5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	    
	    
	    // TODO Auto-generated method stub
	}

}
