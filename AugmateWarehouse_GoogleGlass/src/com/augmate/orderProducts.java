package com.augmate;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class orderProducts implements Serializable{

	static final long serialVersionUID = 1L;
	
	int Order_idOrder;	
	int Product_idProduct;
	int Quantity;
	int Picked;
	String name;
	int sku;
	
	Product product;
	public orderProducts(){
	}
	
	public orderProducts(Product p){
		product = p; 
	}

	public void parseJson(JSONObject jsobject){
		try{			
			Order_idOrder=(jsobject.has("Order_idOrder"))?jsobject.getInt("Order_idOrder"):0;
			Product_idProduct=(jsobject.has("Product_idProduct"))?jsobject.getInt("Product_idProduct"):0;			
			Quantity=(jsobject.has("Quantity"))?jsobject.getInt("Quantity"):0;
			Picked=(jsobject.has("Picked"))?jsobject.getInt("Picked"):0;
			name=(jsobject.has("name"))?jsobject.getString("name"):"";	
			sku=(jsobject.has("sku"))?jsobject.getInt("sku"):0;
			
			
			
		}catch(JSONException e){
			Log.e(User.class.toString(), "Failed to parse"+e.toString());
		}
	}
	
	public String toString(){
		return "Product #"+Product_idProduct;
	}
}


