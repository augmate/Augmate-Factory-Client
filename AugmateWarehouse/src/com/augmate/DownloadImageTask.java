package com.augmate;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	String tag;
	DownloadImageTaskConfirmationDelegate delegate;

	public interface DownloadImageTaskConfirmationDelegate
	{
		boolean shouldDisplayImage(String tag);
	}
	
	public DownloadImageTask(ImageView bmImage) {
		this(bmImage, null, null);
	}

	public DownloadImageTask(ImageView bmImage, String tag, DownloadImageTaskConfirmationDelegate delegate) {
	    this.bmImage = bmImage;
	    this.tag = tag;
	    this.delegate = delegate;
	}

	protected Bitmap doInBackground(String... urls) {
	    String urldisplay = urls[0];
	    Bitmap mIcon11 = null;
	    try {
	        InputStream in = new java.net.URL(urldisplay).openStream();
	        mIcon11 = BitmapFactory.decodeStream(in);
	    } catch (Exception e) {
	        Log.e("Error", "Exception downloading image: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		if(result!=null 
				&& (this.delegate == null || (this.delegate != null && this.delegate.shouldDisplayImage(this.tag)))
				) {	
			bmImage.setImageBitmap(result);
		}
	}
}