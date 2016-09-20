package com.upb.taxbilling.view.qr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.upb.taxbilling.MainMenu;
import com.upb.taxbilling.R;
import com.upb.taxbilling.controller.billanalyzer.BillAnalyzer;
import com.upb.taxbilling.qr.QRDecoder;

/**
 * 
 * @author Bethsy Pinedo
 * @author Graciela Quiroga
 */
public class QRCameraFragment extends Fragment {
	ImageView imgFavorite;
	public static File file;
	public static Bitmap bit;  
	/*
	 * Declaration of a textview object 
	 */
	private TextView textbox;
	private TextView readtext;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_qrcamera,
				container, false);
		/**
		 * Declaration of interface entities
		 */
		final Button btn1 = (Button) view.findViewById(R.id.button1);
		textbox = (TextView) view.findViewById(R.id.textView2);
		readtext= (TextView) view.findViewById(R.id.textView3);
		imgFavorite = (ImageView)view.findViewById(R.id.imageView1);
		/**
		 * Shows the QR code(saved in resources) on a Imageview
		 */
	    imgFavorite.setImageResource(R.drawable.logo);
		/**
		 * On Click image Event
		 */
	      imgFavorite.setOnClickListener(new OnClickListener() {
	         @Override
	         public void onClick(View v) {
	            open();
	         }
	      });
	
	btn1.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			/**
			 * Binarization of the image of the QR Code
			 */
	
			String msg = "";
			try {
				msg = QRDecoder.decodeQRCode(bit);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (msg== null)
			{
				textbox.setText("La imagen no puede ser decodificada, Tome una Nueva");
				if (file.exists ()) file.delete ();  
	
			}
			else
				{
				textbox.setText(msg);
				try {
					/**
					 * Generating a code for the file name 
					 */
					Random generator = new Random();
				    int n = 1;
				    n = generator.nextInt(n);
					String file = "textoQR_" + n;  
					OutputStreamWriter QRtext = new OutputStreamWriter(arg0.getContext().openFileOutput(file,Activity.MODE_PRIVATE));
					/**
					 * Writes the String on the .txt file
					 */
					QRtext.write(textbox.getText().toString());
					QRtext.flush();
					QRtext.close();
					
					/**
					 * Shows that the file has been saved
					 */
					Toast.makeText(arg0.getContext(), "Guardado",
							Toast.LENGTH_SHORT).show();
	                 
					/**
					 * Clear the imageView 
					 */
					imgFavorite.setImageDrawable(null);
				} 
				catch (IOException ex) {
					ex.printStackTrace();
				}
				/**
				 * Deletes the pthotograph
				 */
				if (file.exists ()) file.delete ();
				/**
				 * Reads the text file
				 */
				try {
					Random generatorread = new Random();
				    int m = 1;
				    m = generatorread.nextInt(m);
				    InputStreamReader archivo = new InputStreamReader(arg0.getContext().openFileInput("textoQR_"+ m));
	                BufferedReader br = new BufferedReader(archivo);
	                String linea = br.readLine();
	                String todo = "";
	                while (linea != null) {
	                    todo = todo + linea + "\n";
	                    linea = br.readLine();
	                }
	                br.close();
	                archivo.close();
	                readtext.setText(todo);
	            } 
				catch (IOException e) {
	            }
		}
			saveBillIntoTable();
		}
	    } ) ;
	return view;
}

	
	/**
	 * Open the camera
	 */
	  public void open(){
	      Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	      startActivityForResult(intent, 0);
	   }

	  /**
	   * Creates a bitmap of the camera activity result
	   */
	   @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      // TODO Auto-generated method stub
	      super.onActivityResult(requestCode, resultCode, data);
	      Bitmap bp = (Bitmap) data.getExtras().get("data");
	      imgFavorite.setImageBitmap(bp);	 
	      bit = ((BitmapDrawable)imgFavorite.getDrawable()).getBitmap();
	      SaveImage(bp);
		   }

	   public void SaveImage(Bitmap bp) {

		    File myDir = new File("/sdcard/");    
		    String fname = "qrinclinado.png";
		    file = new File (myDir, fname);
		    if (file.exists ()) file.delete (); 
		    try {
		           FileOutputStream out = new FileOutputStream(file);
		           bp.compress(Bitmap.CompressFormat.PNG, 90, out);
		           out.flush();
		           out.close();

		    } 
		    catch (Exception e) {
		           e.printStackTrace();
		    }

		   }
	   
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/**
		 * Handle action bar item clicks here. The action bar will
		 * automatically handle clicks on the Home/Up button, so long
		 * as you specify a parent activity in AndroidManifest.xml.
		 */
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	* Decodes the bill text and saves it's respective bill into the table.
	*/
	private void saveBillIntoTable() {
		try {
			BillAnalyzer.parseBill(textbox.getText().toString());
			MainMenu main = (MainMenu) getActivity();
			main.getActionBar().setSelectedNavigationItem(2);
		} catch (Exception ex) {
			Toast.makeText(getActivity(), "Error al agregar factura a la tabla",
			Toast.LENGTH_SHORT).show();
		}
	}
}
