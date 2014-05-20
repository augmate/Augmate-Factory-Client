package com.augmate;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Order implements Serializable{

	static final long serialVersionUID = 2L;
	
	int idOrder;
	int User_idUser;
	int Company_idCompany;
	Date date;
	int status;
	int employee;
	ArrayList<Product> products;
	
	User activeUser;
	
	public Order(){
		this.products = new ArrayList<Product>();
	}
	
	/**
	 * Parse json for user object
	 * @param jsobject
	 */
	public void parseJson(JSONObject jsobject){
		try{
			JSONObject orderjs=(jsobject.has("order"))?jsobject.getJSONObject("order"):null;
			JSONArray productsjs=(jsobject.has("products"))?jsobject.getJSONArray("products"):null;
			parseJsonOrder(orderjs);
			parseJsonProducts(productsjs);
		}catch(JSONException e){
			Log.e(User.class.toString(), "Failed to parse"+e.toString());
		}
	}
	private void parseJsonOrder(JSONObject jsobject){
		try{
			idOrder=(jsobject.has("idOrder"))?jsobject.getInt("idOrder"):0;
			User_idUser=(jsobject.has("User_idUser"))?jsobject.getInt("User_idUser"):0;
			Company_idCompany=(jsobject.has("Company_idCompany"))?jsobject.getInt("Company_idCompany"):0;
			status=(jsobject.has("status"))?jsobject.getInt("status"):0;
			employee=(jsobject.has("employee"))?jsobject.getInt("employee"):0;
			
		}catch(JSONException e){
			Log.e(User.class.toString(), "Failed to parse"+e.toString());
		}
	}
	private void parseJsonProducts(JSONArray productsJs){
		for(int i=0; i< productsJs.length(); i++){
			try{				
				JSONObject p= productsJs.getJSONObject(i);
				Product product=new Product();
				product.parseJson(p);
				products.add(product);
			}catch(JSONException e){
				Log.e(User.class.toString(), "Failed to parse"+e.toString());
			}
		}
	}
	
	public String toString(){
		return "Order #"+idOrder;
	}
	

	
	
	
}
