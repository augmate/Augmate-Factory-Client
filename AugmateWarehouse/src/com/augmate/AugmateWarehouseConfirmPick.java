package com.augmate;

import java.util.ArrayList;

import com.google.zxing.integration.android.IntentIntegrator;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AugmateWarehouseConfirmPick extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    Product product = (Product)getIntent().getSerializableExtra("Product");

		setContentView(R.layout.activity_augmate_warehouse_confirm_pick);
		
		TextView skuTextView = (TextView) findViewById(R.id.skuPickedProduct);
		skuTextView.setText(String.format("SKU #%s", product.sku));
		
		TextView numberOfProductsToPick = (TextView) findViewById(R.id.numberOfProductsToPick);
		numberOfProductsToPick.setText(String.format("%s", (product.quantityToPick-product.quantityPicked)));
		
		// Load the animated gif into a webview
		WebView imageView = (WebView) findViewById(R.id.imageProduct);
		imageView.setHorizontalScrollBarEnabled(false);
		imageView.setVerticalScrollBarEnabled(false);
		
		// TODO: These are currently hardcoded, but should be pulled from backend eventually
        String baseUrl = "file:///android_asset/";
        String fileName = "muffler.gif";
        
        if (product.name.contains("Engine"))
        {
        	fileName = "engine.gif";
        }
        else if (product.name.contains("Muffler"))
        {
        	fileName = "muffler.gif";
        }
        else if (product.name.contains("Assy"))
        {
        	fileName = "conrods.gif";
        }
        else
        {
        	baseUrl = "http://zoetis.augmate.com/augmate/cms/hp/upload/";
        	fileName = product.image;
        }
        // TODO: Refactor this into a class
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{height:100%25;} </style>");
        strBuilder.append("</head><body>");
        strBuilder.append("<img src=\"" + fileName + "\" height=\"100%\" /></body></html>");
        String data = strBuilder.toString();
        imageView.loadDataWithBaseURL(baseUrl, data, "text/html", "utf-8", null);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.augmate_warehouse_confirm_pick, menu);
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
			finish();
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(RESULT_CANCELED);//, returnIntent);     
			finish();
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            SkypeHelper.initiateSkype((Context)this);
		}
		return true;
	}
}
