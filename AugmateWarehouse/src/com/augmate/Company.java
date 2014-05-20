package com.augmate;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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

public class Company implements Serializable{

	static final long serialVersionUID = 1L;
	
	int idCompany;
	String name;
	String address;
	String phone;
	String state;
	String country;
	String logotype;
	String floorPlan;
	
	User activeUser;
	
	public Company(User u){
		activeUser=u;
	}
	
	public int getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(int idCompany) {
		this.idCompany = idCompany;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLogotype() {
		return logotype;
	}

	public void setLogotype(String logotype) {
		this.logotype = logotype;
	}

	public String getFloorPlan() {
		return floorPlan;
	}

	public void setFloorPlan(String floorPlan) {
		this.floorPlan = floorPlan;
	}

	public ArrayList<Order>  getPendingOrders(){
		ArrayList<Order> orders= new ArrayList<Order>();
		
		int statusCode=-1;
		try{
			DefaultHttpClient client = new DefaultHttpClient();
			//Get connection url
            HttpGet httpGet= ConnectionHelper.getHttpGet("companyOrders/"+this.idCompany, this.activeUser.getUsername(), this.activeUser.getPassword());
            //Make/exec call
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				
				try{
					JSONArray jObj= new JSONArray(ConnectionHelper.getResponseString(response));
					for(int i=0; i< jObj.length(); i++){
						HashMap<String, String> map= new HashMap<String, String>();
						JSONObject o= jObj.getJSONObject(i);
						Order order=new Order();
						order.parseJson(o);
						orders.add(order);
					}
				}catch(JSONException e){
					Log.e(AugmateWarehouseUserLogin.class.toString(), "Error creating JSon Object");
				}				
			}
			
		}catch(ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return orders;
	}
	
	
	/**
	 * Parse json for user object
	 * @param jsobject
	 */
	public void parseJson(JSONObject jsobject){
		try{
			idCompany=(jsobject.has("idCompany"))?jsobject.getInt("idCompany"):0;
			name=(jsobject.has("name"))?jsobject.getString("name"):"";
			address=(jsobject.has("address"))?jsobject.getString("address"):"";
			phone=(jsobject.has("address"))?jsobject.getString("address"):"";
			state=(jsobject.has("state"))?jsobject.getString("state"):"";
			country=(jsobject.has("country"))?jsobject.getString("country"):"";
			logotype=(jsobject.has("logotype"))?jsobject.getString("logotype"):"";
			floorPlan=(jsobject.has("floorPlan"))?jsobject.getString("floorPlan"):"";
		}catch(JSONException e){
			Log.e(User.class.toString(), "Failed to parse"+e.toString());
		}
	}
}
