package com.augmate;

import org.json.JSONObject;

public interface WebServiceCallbackable {
	public void webServiceCallback(int status, JSONObject jObj);
}
