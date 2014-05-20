package com.augmate;

import org.json.JSONObject;

public interface WebServiceCallbackable {
	public void webServiceCallback(int requestCode, int status, JSONObject jObj);
}
