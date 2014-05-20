package com.augmate;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Base64;
import android.util.Log;

public class ConnectionHelper {

	static String hostApp="augmate/cms/warehouse/index.php/api/";
	static HttpHost targetHost = new HttpHost("zoetis.augmate.com", 80, "http");
	/**
	 * Get connection with auth to connect to server
	 * @param request
	 * @return
	 */
	public static HttpGet getHttpGet(String request, String username, String password){
		return getHttpGet(request, username+":"+password);
/*		
		HttpGet httpGet = new HttpGet("http://"+targetHost.toHostString()+"/"+hostApp+request);
        httpGet.setHeader("Authorization", "Basic "+Base64.encodeToString((username+":"+password).getBytes(), Base64.NO_WRAP));
        return httpGet;
        */
	}
	/**
	 * Get connection with auth to connect to server
	 * @param request
	 * @return
	 */
	public static HttpGet getHttpGet(String request, String token){
		HttpGet httpGet = new HttpGet("http://"+targetHost.toHostString()+"/"+hostApp+request);
        httpGet.setHeader("Authorization", "Basic "+Base64.encodeToString((token).getBytes(), Base64.NO_WRAP));
        return httpGet;
	}
	/**
	 * Get response string from server to can parse with json or xml
	 * @param response
	 * @return
	 */
	public static String getResponseString(HttpResponse response){
		StringBuilder builder = new StringBuilder();
		HttpEntity entity = response.getEntity();
		try{
			InputStream content = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				Log.d ("line: ", line);
				Log.d ("after line: ", line);
				
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
