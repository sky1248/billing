package com.upb.taxbilling.qr;

import android.graphics.Bitmap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * Decodes a qr image.
 * @author Alicia Arce
 * @author Bethsy Pinedo
 * @author Graciela Quiroga
 * @author Rolando Lopez
 * @author Guillermo Torrez
 * @author Carlos Abasto
 */
public class QRDecoder {
	
	/**
	 * Method to decode the QR code of a given image
	 * @param img to be decoded
	 * @return
	 * @throws Exception 
	 */
	public static String decodeQRCode(Bitmap img) throws Exception {
		if (img == null) {
			throw new Exception("Image not found");
		}
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width * height];
		img.getPixels(pixels, 0, width, 0, 0, width, height);
		LuminanceSource src = new RGBLuminanceSource(
				width, height, pixels);
		Result res = null;
		try 
		{
			BinaryBitmap bmp = new BinaryBitmap(new HybridBinarizer(src));
			QRCodeReader reader = new QRCodeReader();
			res = reader.decode(bmp);
		}
		catch (ReaderException e) 
		{
			throw new Exception("Image not found");
		}
		return res.getText();
	}
}
