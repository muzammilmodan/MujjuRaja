package com.sample.mobile.sampleapp.Webservices;

import org.json.JSONObject;

public interface OnUpdateListener {

	void onUpdateComplete(JSONObject jsonObject, boolean isSuccess);
}
