package com.augmate;
// test..
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class User implements Serializable {

	static final long serialVersionUID = 4L;
	
	String name;
	String email;
	String username;
	String password;
	
	
	public User(){
		
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	int Company_idCompany;
	int idUser;
	int role;
	int perms;
	
	Company company;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getCompany_idCompany() {
		return Company_idCompany;
	}
	public void setCompany_idCompany(int company_idCompany) {
		Company_idCompany = company_idCompany;
	}
	public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getPerms() {
		return perms;
	}
	public void setPerms(int perms) {
		this.perms = perms;
	}
	
	public Company getCompany(){
		
		if(this.Company_idCompany > 0 && company==null){
			try{
				DefaultHttpClient client = new DefaultHttpClient();
				//Get connection url
		        HttpGet httpGet= ConnectionHelper.getHttpGet("company/"+this.Company_idCompany, username, password);
		        //Make/exec call
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					company=new Company(this);
					try{
						JSONObject jObj= new JSONObject(ConnectionHelper.getResponseString(response));
						company.parseJson(jObj);
					}catch(JSONException e){
						Log.e(AugmateWarehouseUserLogin.class.toString(), "Error creating JSon Object");
					}
					
					
					
				}
			}catch(ClientProtocolException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		return company;
		
	}
	
	/*
	public void login(String token)
	{
		WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper("login/0", token); 
	}
	
	public void login(String u, String p)
	{
		this.username=u;
		this.password=p;
		
		int statusCode=-1;
		//try{
			String token = Base64.encodeToString((username+":"+password).getBytes(), Base64.NO_WRAP);
			WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper("login/0", token); 
			webServiceCallHelper.execute();
			*//*
			DefaultHttpClient client = new DefaultHttpClient();
			//Get connection url
			HttpGet httpGet= ConnectionHelper.getHttpGet("login/0", username, password);
            //Make/exec call
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				
				try{
					JSONObject jObj= new JSONObject(ConnectionHelper.getResponseString(response));
					this.parseJson(jObj);
				}catch(JSONException e){
					Log.e(AugmateWarehouseActivity.class.toString(), "Error creating JSon Object");
				}
				
				
				
			}
			*//*
		//}catch(ClientProtocolException e) {
		//	e.printStackTrace();
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}

		return statusCode;
	}
*/
	/**
	 * Parse json for user object
	 * @param jsobject
	 */
	public void parseJson(JSONObject jsobject){
		try{
			idUser=(jsobject.has("idUser"))?jsobject.getInt("idUser"):0;
			username=(jsobject.has("username"))?jsobject.getString("username"):"";
			password=(jsobject.has("password"))?jsobject.getString("password"):"";
			email=(jsobject.has("email"))?jsobject.getString("email"):"";
			role=(jsobject.has("role"))?jsobject.getInt("role"):-1;
			perms=(jsobject.has("perms"))?jsobject.getInt("perms"):-1;
			name=(jsobject.has("name"))?jsobject.getString("name"):"";
			Company_idCompany=(jsobject.has("Company_idCompany"))?jsobject.getInt("Company_idCompany"):0;
		}catch(JSONException e){
			Log.e(User.class.toString(), "Failed to parse"+e.toString());
		}
	}
}


