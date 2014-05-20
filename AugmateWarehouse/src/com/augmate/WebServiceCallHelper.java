package com.augmate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class WebServiceCallHelper {

	String actionPath;
	String token;
	WebServiceCallbackable webServiceCallbackable;
	int requestCode;

	int statusCode;

	public WebServiceCallHelper(WebServiceCallbackable webServiceCallbackable, int requestCode,
			String actionPath, String token) {
		this.webServiceCallbackable = webServiceCallbackable;
		this.requestCode = requestCode;
		this.actionPath = actionPath;
		this.token = token;
	}

	public void execute() {
		new DownloadWebpageTask().execute(actionPath);
	}

	private static final String DEBUG_TAG = "WebServiceCallHelper";

	// Uses AsyncTask to create a task away from the main UI thread. This task
	// takes a
	// URL string and uses it to create an HttpUrlConnection. Once the
	// connection
	// has been established, the AsyncTask downloads the contents of the webpage
	// as
	// an InputStream. Finally, the InputStream is converted into a string,
	// which is
	// displayed in the UI by the AsyncTask's onPostExecute method.
	private class DownloadWebpageTask extends
			AsyncTask<String, Void, JSONObject> {
		@Override
		protected JSONObject doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			try {
				
				return makeWebServiceCall(urls[0]);
			} catch (IOException e) {
				Log.e(WebServiceCallHelper.class.toString(),
						"Unable to retrieve web page. URL may be invalid.");
				return null;// HO-R return message in JSON?
			}
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(JSONObject result) {
			// textView.setText(result);
			Log.d(DEBUG_TAG, "in onPostExecute, result=" + result);
			webServiceCallbackable.webServiceCallback(requestCode, statusCode, result);
		}
	}

	private JSONObject makeWebServiceCall(String actionPath) throws IOException {

		JSONObject jObj = null;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			// Get connection url
			Log.d("call", actionPath);
			HttpGet httpGet = ConnectionHelper.getHttpGet(actionPath,
					this.token);
			
			if (!isNetworkAvailable()) {
				statusCode = 501;// HO-R for now
			} else {
				// Make/exec call		
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				statusCode = statusLine.getStatusCode();
				// HO-R need to think this through if (statusCode == 200) {
				try {
					jObj = new JSONObject(ConnectionHelper.getResponseString(response));
				} catch (JSONException e) {
					Log.e(WebServiceCallHelper.class.toString(),
						"Error creating JSon Object");// HO-R genericize
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;//HO-R how to handle
		}

		return jObj;

	}

	// Reads an InputStream and converts it to a String.
	public String readIt(InputStream stream, int len) throws IOException,
			UnsupportedEncodingException {
		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] buffer = new char[len];
		reader.read(buffer);
		return new String(buffer);
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager)((Context) this.webServiceCallbackable).getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		Log.d("activeNetworkInfo", activeNetworkInfo.toString());
		Log.d("activeNetworkInfo.getTypeName()", activeNetworkInfo.getTypeName());
		return activeNetworkInfo != null && activeNetworkInfo.isConnected() && ("WIFI".equals(activeNetworkInfo.getTypeName()) || "mobile".equals(activeNetworkInfo.getTypeName()));
	}
}
