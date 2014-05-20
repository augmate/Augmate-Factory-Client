package com.augmate;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

//activity_augmate_warehouse_confirm_delivery
public class AugmateWarehouseConfirmDelivery extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//	    String packingLocation = (String)getIntent().getSerializableExtra("packingLocation");

		setContentView(R.layout.activity_augmate_warehouse_confirm_delivery);

		WebView imageView = (WebView) findViewById(R.id.orderCompleteAnimation);
        String baseUrl = "file:///android_asset/";
        String fileName = "Job_Completed_animated.gif";
        
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{height:100%25;} </style>");
        strBuilder.append("</head><body>");
        strBuilder.append("<img src=\"" + fileName + "\" height=\"100%\" /></body></html>");
        String data = strBuilder.toString();
        imageView.loadDataWithBaseURL(baseUrl, data, "text/html", "utf-8", null);
        
        imageView.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				setResult(RESULT_OK);
				finish();
				return true;
			}
		});		
        
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
//		TextView packingLocationTextView = (TextView) findViewById(R.id.packingLocation);
//		packingLocationTextView.setText(String.format("Please deliver to packing location %s", packingLocation));	
//		
//		Order order = (Order)getIntent().getSerializableExtra("Order");
//		TextView orderNumberTextView = (TextView) findViewById(R.id.orderNumber);
//		orderNumberTextView.setText(String.format("Order #%d", order.idOrder));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.augmate_warehouse_confirm_delivery, menu);
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		Intent returnIntent = new Intent();
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP 
			|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER // Google Glass single tap
			)
		{
			setResult(RESULT_OK);//, returnIntent);   
			
			// check order complete	after confirm of picking the right product, or after updating product list in current order		
//			setContentView(R.layout.order_complete);
			
			finish();
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(RESULT_CANCELED);//, returnIntent);     
			finish();
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            SkypeHelper.initiateSkype((Context)this);
		}
		return true;
	}
	
	public boolean onTouchEvent(MotionEvent event) 
	{
		setResult(RESULT_OK);
		finish();
	    return true;
	}
}
