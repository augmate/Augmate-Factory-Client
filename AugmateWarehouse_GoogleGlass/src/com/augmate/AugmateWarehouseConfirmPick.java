package com.augmate;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.augmate_warehouse_confirm_pick, menu);
		return true;
	}

}
