package com.augmate;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Product implements Serializable{
	
	static final long serialVersionUID = 2L;
	
	int idProduct;
	String name;
	double price;
	String description;
	String image;
	String sku;
	int mapX;
	int mapY;
	String aisle;
	String shelfUnit;
	String shelf;
	
	int quantityPicked;
	int quantityToPick;
	
	public void parseJson(JSONObject jsobject) {
		try{
			idProduct=(jsobject.has("idProduct"))?jsobject.getInt("idProduct"):0;
			name=(jsobject.has("name"))?jsobject.getString("name"):"";	
			price=(jsobject.has("price"))?jsobject.getDouble("price"):0;
			description=(jsobject.has("description"))?jsobject.getString("description"):"";	
			image=(jsobject.has("image"))?jsobject.getString("image"):"";	
			sku=(jsobject.has("sku"))?jsobject.getString("sku"):"";
			mapX=(jsobject.has("mapX"))?jsobject.getInt("mapX"):0;
			mapY=(jsobject.has("mapY"))?jsobject.getInt("mapY"):0;
			aisle=(jsobject.has("aisle"))?jsobject.getString("aisle"):"";	
			shelfUnit=(jsobject.has("shelf_unit"))?jsobject.getString("shelf_unit"):"";	
			shelf=(jsobject.has("shelf"))?jsobject.getString("shelf"):"";	
			
			quantityPicked=(jsobject.has("Picked"))?jsobject.getInt("Picked"):0;
			quantityToPick=(jsobject.has("Quantity"))?jsobject.getInt("Quantity"):0;
			
			
		}catch(JSONException e){
			Log.e(User.class.toString(), "Failed to parse"+e.toString());
		}
	}
	
	public String toString(){
		return "Product #"+idProduct;
	}
}


