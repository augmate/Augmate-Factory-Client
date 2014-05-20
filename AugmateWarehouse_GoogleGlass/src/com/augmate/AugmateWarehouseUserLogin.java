package com.augmate;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.AbstractHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AugmateWarehouseUserLogin extends Activity implements
		WebServiceCallbackable {

	private static final String DEBUG_TAG = "AugmateWarehouseUserLogin";
	TextView QRTextView;
	MediaPlayer mp;
	User user = new User();
	final int START_ORDER = RESULT_OK + 100;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		QRTextView = (TextView) findViewById(R.id.QRTextView);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.v(DEBUG_TAG, "onKeyDown: keyCode: '" + keyCode + "': event: " + event.toString());
		//if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			IntentIntegrator zxingIntegrator = new IntentIntegrator(this);
			zxingIntegrator.setMessage("Wha?");
			zxingIntegrator.initiateScan();
//		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//			QRTextView.setText("Logging In");
//			String token = "pete:ca72431bf6bc7729c82dbb19ed7a9f2c";
//			WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper(this, "login/0", token);
//			webServiceCallHelper.execute();
		//} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return true;
	}

	public boolean onTouchEvent(MotionEvent event) 
	{
	    Log.v(DEBUG_TAG,"OnTouchEvent !!!");
	    // HO-R For now faking a logging by clicking on view when in emulator
		QRTextView.setText("Logging In");
		String token = "pete:ca72431bf6bc7729c82dbb19ed7a9f2c";
		WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper(this, "login/0", token);
		webServiceCallHelper.execute();

	    return true;
	}
	
	public boolean onClick(int keyCode, KeyEvent event) {
		//if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				//|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
			IntentIntegrator xzingIntegrator = new IntentIntegrator(this);
			xzingIntegrator.initiateScan();
		//}
		
		//if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(event.getRepeatCount() == 0)
	       this.finish();
	        //return true;
	    //}
		
		return true;
	}
	
	@Override
    public void onBackPressed() {
     this.finish();
    }
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (requestCode == START_ORDER) {
			Intent newintent = new Intent(this, AugmateWarehouseOrder.class);
			newintent.putExtra("User", user);
			startActivity(newintent);

		} else {
			if (scanResult.getContents() != null) {
				String result = scanResult.getContents();
				// QRTextView.setText(result);
				QRTextView.setText("Logging In");
				String token = result;
				WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper(
						this, "login/0", token);
				webServiceCallHelper.execute();
			}

		}	
	}

	public void webServiceCallback(int status, JSONObject jObj) {
		
		if(status == 200){
			Log.d("AugmateWarehouseActivity.webServiceCallback:", jObj.toString());
			mp = MediaPlayer.create(AugmateWarehouseUserLogin.this, R.raw.ding);  
			mp.start();

			user.parseJson(jObj);	
			
			Intent intent = new Intent(this, glassvideo.class);
			startActivityForResult(intent,START_ORDER);
		}
		else if(status == 401){
			QRTextView.setText("User/Password is incorrect");
			mp = MediaPlayer.create(AugmateWarehouseUserLogin.this, R.raw.buzzer);  
			mp.start();
		}
		else {
			QRTextView.setText(String.format("Status code not handeled: %d", status));
		}
	}
}
