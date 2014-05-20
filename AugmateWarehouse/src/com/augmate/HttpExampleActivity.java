package com.augmate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HttpExampleActivity extends Activity {
    private static final String DEBUG_TAG = "HttpExample";
    /*
    private EditText urlText;
    private TextView textView;
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   
        /*
        urlText = (EditText) findViewById(R.id.myUrl);
        textView = (TextView) findViewById(R.id.myText);
        
        */
        //myClickHandler();
        
 		new DownloadWebpageTask().execute("login/0");
    }

    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void myClickHandler(/*View view*/) {
        // Gets the URL from the UI's text field.
        String stringUrl = "http://zoetis.augmate.com/augmate/cms/warehouse/index.php/api/login/0";// urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            Log.d(DEBUG_TAG, "No network connection available.");
            //textView.setText("No network connection available.");
        }
    }

     // Uses AsyncTask to create a task away from the main UI thread. This task takes a 
     // URL string and uses it to create an HttpUrlConnection. Once the connection
     // has been established, the AsyncTask downloads the contents of the webpage as
     // an InputStream. Finally, the InputStream is converted into a string, which is
     // displayed in the UI by the AsyncTask's onPostExecute method.
     private class DownloadWebpageTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... urls) {
              
            // params comes from the execute() call: params[0] is the url.
            try {
                return makeWebServiceCall(urls[0]);
            } catch (IOException e) {
				Log.e(AugmateWarehouseUserLogin.class.toString(), "Unable to retrieve web page. URL may be invalid.");
                return null;// HO-R return message in JSON?
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(JSONObject result) {
            // textView.setText(result);
            Log.d(DEBUG_TAG, "in onPostExecute, result=" + result);
       }
    }

	private JSONObject makeWebServiceCall(String actionPath)  throws IOException {
		
		JSONObject jObj = null;
		try{
			String username = "pete";
			String password = "capstone";
			
			DefaultHttpClient client = new DefaultHttpClient();
			//Get connection url
	        HttpGet httpGet= ConnectionHelper.getHttpGet(actionPath, username, password);
	        //Make/exec call
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				try{
					jObj= new JSONObject(ConnectionHelper.getResponseString(response));
				}catch(JSONException e){
					Log.e(AugmateWarehouseUserLogin.class.toString(), "Error creating JSon Object");// HO-R genericize
				}
			}
		}catch(ClientProtocolException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}

		return jObj;
		
	}

  
  //Reads an InputStream and converts it to a String.
	public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	   Reader reader = null;
	   reader = new InputStreamReader(stream, "UTF-8");        
	   char[] buffer = new char[len];
	   reader.read(buffer);
	   return new String(buffer);
	}
}
