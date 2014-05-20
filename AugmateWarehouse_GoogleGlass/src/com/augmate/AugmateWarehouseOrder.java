package com.augmate;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AugmateWarehouseOrder extends Activity implements WebServiceCallbackable {
	Order order;
	User user;
	int currentProductIndex = 0;
	List<String> codeTypes;
	MediaPlayer mp;
	
	Context context;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    context=(Context)this;
	    // TODO Auto-generated method stub
	    setContentView(R.layout.current_product_to_pick);
		TextView skuText = (TextView) findViewById(R.id.SKUTextView);
		skuText.setText("Loading...");

	    user= (User)getIntent().getSerializableExtra("User");
	    
	    Toast.makeText(this, "Hello "+user.getName()+" we are loading your order to pick. Please wait", Toast.LENGTH_SHORT).show();
	    
	    //Product Scan codes
	    this.codeTypes= new ArrayList<String>();
	    this.codeTypes.add("UPC_A");
	    this.codeTypes.add("UPC_E");	
		
	    String token = user.username + ":" + user.password;
		WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper(this, "nextOrder/0", token);
		webServiceCallHelper.execute();

		LinearLayout ll = (LinearLayout) findViewById(R.id.productLayout);
		ll.setVisibility(View.INVISIBLE);
		LinearLayout llConfirm = (LinearLayout) findViewById(R.id.confirmProductPicking);
		llConfirm.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		ArrayList<Product> products = order.products;

		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			// go to next item, or loop back to first
			if (products.size() > 1) {
				if (currentProductIndex >= products.size() - 1) {
					// loop back to first item
					currentProductIndex = 0;
				} else {
					currentProductIndex++;
				}
				this.populate();
			}
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) { 
			// go to next item, or loop back to first
			if (products.size() > 1) {
				if (currentProductIndex > 0) {
					// loop back to first item
					currentProductIndex--;
				} else {
					currentProductIndex = products.size() - 1;
				}
				this.populate();
			}
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getRepeatCount() > 0) {
				finish();
			} else {
				IntentIntegrator xzingIntegrator = new IntentIntegrator(this);
				xzingIntegrator.initiateScan(codeTypes);
			}
		}
		return true;
	}

	// get the scan result
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult.getContents() != null) {
			String result = scanResult.getContents();
			
			ArrayList<Product> products = order.products;
			Product lookingFor = products.get(currentProductIndex);
			
			if (result.contentEquals(lookingFor.sku)) {
				mp = MediaPlayer.create(AugmateWarehouseOrder.this, R.raw.ding); 
				mp.setVolume(1.0f, 1.0f);
				mp.start();
				
				Intent confirmPickIntent = new Intent(this, AugmateWarehouseConfirmPick.class);
				confirmPickIntent.putExtra("Product", lookingFor);
				startActivity(confirmPickIntent);
				/*
				TextView skuTextView = (TextView) findViewById(R.id.skuPickedProduct);
				skuTextView.setText(String.format("SKU #%s", lookingFor.sku));
				
				TextView numberOfProductsToPick = (TextView) findViewById(R.id.numberOfProductsToPick);
				numberOfProductsToPick.setText(String.format("%s", (lookingFor.quantityToPick-lookingFor.quantityPicked)));
				
				LinearLayout ll = (LinearLayout) findViewById(R.id.productLayout);
				ll.setVisibility(View.INVISIBLE);
				LinearLayout llConfirm = (LinearLayout) findViewById(R.id.confirmProductPicking);
				llConfirm.setVisibility(View.VISIBLE);
				*/
			}
			else{
				//Toast.makeText(AugmateWarehouseOrder.this, "The sku product no match",Toast.LENGTH_LONG).show();
				mp = MediaPlayer.create(AugmateWarehouseOrder.this, R.raw.buzzer);  
				mp.start();
				LinearLayout ll = (LinearLayout) findViewById(R.id.productLayout);
				ll.setBackgroundColor(0x80ae0000);
			}
		}		
	}
	
	private void populate() {
		ArrayList<Product> products = order.products;
		Product nextProduct = products.get(currentProductIndex);
		Log.d("product", nextProduct.toString());
		
		// HO-R ListView listView= (ListView) findViewById(R.id.listView1);
		
		TextView orderNumberTextView = (TextView) findViewById(R.id.OrderNumberTextView);
		orderNumberTextView.setText(String.format("%d", order.idOrder));
		
		TextView itemNumberInOrderTextView = (TextView) findViewById(R.id.itemNumberInOrderTextView);
		itemNumberInOrderTextView.setText(String.format("Item %d of %d", currentProductIndex+1, products.size()));

		TextView skuTextView = (TextView) findViewById(R.id.SKUTextView);
		skuTextView.setText(String.format("SKU #%s", nextProduct.sku));

		TextView nameTextView = (TextView) findViewById(R.id.NameTextView);
		nameTextView.setText(String.format("Name: %s", nextProduct.name));

		TextView qtyToPickTextView = (TextView) findViewById(R.id.qtyToPickTextView);
		qtyToPickTextView.setText(String.format("Qty to Pick: %d", nextProduct.quantityToPick));
		
		TextView qtyPickedTextView = (TextView) findViewById(R.id.qtyPickedTextView);
		qtyPickedTextView.setText(String.format("Qty Picked: %d", nextProduct.quantityPicked));
		
		TextView aisleTextView = (TextView) findViewById(R.id.aisleTextView);
		aisleTextView.setText(String.format("Aisle: %s", nextProduct.aisle));

		TextView shelfUnitTextView = (TextView) findViewById(R.id.shelfUnitTextView);
		shelfUnitTextView.setText(String.format("Shelf Unit: %s", nextProduct.shelfUnit));
			
		TextView shelfTextView = (TextView) findViewById(R.id.shelfTextView);
		shelfTextView.setText(String.format("Shelf: %s", nextProduct.shelf));
			
		LinearLayout ll = (LinearLayout) findViewById(R.id.productLayout);
		ll.setVisibility(View.VISIBLE);
		
		//Download the image product
		new DownloadImageTask((ImageView) findViewById(R.id.imageProduct)).execute("http://cms.augmate.net/upload/"+nextProduct.image);		
	}
	
	public void webServiceCallback(int status, JSONObject jObj) {
		
		if(status == 200){
			Log.d("AugmateWarehouseOrder.webServiceCallback:", jObj.toString());
			
			this.order = new Order();
			order.parseJson(jObj);
			this.populate();

			/* HO-R does this move to populate, too?
		    listView.setOnItemClickListener(new OnItemClickListener() {
		    	
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(),
	    			"Click ListItem Number " + orders.get(position).idOrder, Toast.LENGTH_LONG)
	    			.show();
					Intent intent = new Intent(context, AugmateWarehouseProduct.class);
					intent.putExtra("Order", orders.get(position));
					startActivity(intent);
				}
		    }); 
		    */

			//Intent intent = new Intent(this, AugmateWarehouseOrder.class);
			//intent.putExtra("User", user);
			//startActivity(intent);
		}
		else if(status == 401){
		}
		else{
		}
	}
}
