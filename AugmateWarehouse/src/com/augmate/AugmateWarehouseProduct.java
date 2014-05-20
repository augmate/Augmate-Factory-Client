package com.augmate;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class AugmateWarehouseProduct extends Activity {
	ArrayList<orderProducts> products;
	Order order;
	TextView map ;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	   
	    setContentView(R.layout.products);
	    
	    order = (Order)getIntent().getSerializableExtra("Order");
	    products = products;
	    	 
	    if (products != null){
	    	ListView productlist= (ListView) findViewById(R.id.listView1);
	    	productArrayAdapter adapter= new productArrayAdapter(this, products);
	    
	    //String[] values = {"order 1001", "order 1002", "order 1003"};
	    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);      

	    	productlist.setAdapter(adapter);
	    
	    
	    // show products in map
	    /*
	    map = (TextView)findViewById(R.id.textView3);
	    map.setOnClickListener(new OnClickListener() {      	
            @Override
            public void onClick(View v) {       	
            	Intent intent = new Intent(v.getContext(), map.class);
				startActivity(intent);
            }
            });  
	    */
	    
	    // scan selected object
	    productlist.setOnItemClickListener(new OnItemClickListener() {	    	
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.setPackage("com.google.zxing.client.android");
                intent.putExtra("SCAN_MODE", "ONE_D_MODE");
            	startActivityForResult(intent, 0); 
			}
	    }); 
	    
	    } // end product list
	    
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	// get the scan result
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                
                // Handle successful scan, showing product picked.
                Toast.makeText(AugmateWarehouseProduct.this, contents+" picked",Toast.LENGTH_LONG).show();               
            }
        }
    }
	

}
