package com.sample.mobile.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sample.mobile.sampleapp.Utils.AlertDialogUtility;
import com.sample.mobile.sampleapp.Utils.ConnectivityDetector;
import com.sample.mobile.sampleapp.Utils.DeviceId;
import com.sample.mobile.sampleapp.Utils.GlobalData;
import com.sample.mobile.sampleapp.Utils.SessionManager;
import com.sample.mobile.sampleapp.Webservices.GetJsonWithAndroidNetworkingLib;
import com.sample.mobile.sampleapp.Webservices.OnUpdateListener;
import com.sample.mobile.sampleapp.Webservices.WebField;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static String deviceId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        
    }

    private void callLoginWebservice(String email, String loginType, String password) {
        try {
            deviceId = DeviceId.getDeviceId(this);
            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(MainActivity.this)) {
                JSONObject jsonObjectInput = new JSONObject();
                jsonObjectInput.put(WebField.EMAIL, email);
                jsonObjectInput.put(WebField.DEVICE_TYPE, WebField.DEVICE_TYPE);
                jsonObjectInput.put(WebField.FIRST_NAME, "");
                jsonObjectInput.put(WebField.LAST_NAME, "");

                jsonObjectInput.put(WebField.PASSWORD, password);
                jsonObjectInput.put(WebField.PROFILE_PIC, "");

                jsonObjectInput.put(WebField.DEVICE_TOKEN, deviceId);
                jsonObjectInput.put(WebField.USER_LOGIN.REQUEST_LGOIN_TYPE, loginType);

                new GetJsonWithAndroidNetworkingLib(MainActivity.this,
                        jsonObjectInput, WebField.URL_USER_LOGIN, GlobalData.intFlagShow, new OnUpdateListener() {
                    @Override
                    public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                        if (isSuccess) {
                            try {
//                                if (jsonObject.getString(WebField.USER_LOGIN.RESPONSE_USER_TYPE).matches("1")) {
//                                    JSONObject jobjDetails = jsonObject.getJSONObject(WebField.NORMAL_USER_DETAILS.RESPONSE_USER_DETAILS);
//                                    SessionManager.setBusinessUserDetails(MainActivity.this,
//                                            jobjDetails.getInt(WebField.USER_ID), jobjDetails.getString(WebField.EMAIL),
//                                            jobjDetails.getString(WebField.BUSINESS_USER_REGISTER.REQUEST_BUSINESS_NAME),
//                                            jsonObject.getInt(WebField.USER_LOGIN.RESPONSE_USER_TYPE),
//                                            jobjDetails.getString(WebField.USER_LOGIN.RESPONSE_CURRENCY_ID));
//                                    Intent intent = new Intent(MainActivity.this, NearByLocationActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    JSONObject jobjDetails = jsonObject.getJSONObject(WebField.NORMAL_USER_DETAILS.RESPONSE_USER_DETAILS);
//                                    SessionManager.setNormalUserDetails(MainActivity.this,
//                                            jobjDetails.getInt(WebField.USER_ID), jobjDetails.getString(WebField.EMAIL),
//                                            jobjDetails.getString(WebField.FIRST_NAME), jobjDetails.getString(WebField.LAST_NAME),
//                                            jsonObject.getInt(WebField.USER_LOGIN.RESPONSE_USER_TYPE),
//                                            jobjDetails.getInt(WebField.USER_LOGIN.RESPONSE_LOGIN_TYPE));
//
//                                    Intent intent = new Intent(MainActivity.this, NearByLocationActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }

                                GlobalData.isUserBlockedByAdmin=false;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                AlertDialogUtility.showSnakeBar((jsonObject.getString(WebField.MESSAGE)), scrollMain, MainActivity.this);
                                etEmail.requestFocus();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).execute();
            } else {
                AlertDialogUtility.SHOW_INTERNET_ALERT(MainActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
}
