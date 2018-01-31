package com.sample.mobile.sampleapp.Webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.howdycrowdy.Activities.LoginActivity;
import com.howdycrowdy.SharedPrefrence.SessionManager;
import com.howdycrowdy.Utils.AlertDialogUtility;
import com.howdycrowdy.Utils.ConnectivityDetector;
import com.howdycrowdy.Utils.GlobalData;
import com.howdycrowdy.Utils.LogM;

import org.json.JSONObject;


public class GetJsonWithAndroidNetworkingLib extends AsyncTask<String, JSONObject, JSONObject> {
    private OnUpdateListener onUpdateListener;
    private Context context;
    private JSONObject jsonObject;
    private int intDialogShow = 0;
    private ProgressDialog progressDialog;
    private String url;

    public GetJsonWithAndroidNetworkingLib(Context context, JSONObject jsonObject, String url,
                                           int intDialogShow, OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
        this.jsonObject = jsonObject;
        this.intDialogShow = intDialogShow;
        this.url = url;
        this.context = context;
        LogM.LogV("request :: " + jsonObject);

    }

    @Override
    protected void onPreExecute() {
        if (!ConnectivityDetector.isConnectingToInternet(context)) {
            AlertDialogUtility.showInternetAlert(context);
            return;
        }
        if (intDialogShow == 1) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setTitle("");
            progressDialog.show();
        }
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... param) {
        try {
            AndroidNetworking.post(url)
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                LogM.LogV("response :: " + response);

                                if (response != null) {
                                    if (response.getString(WebField.STATUS).equals("1")) {
                                        onUpdateListener.onUpdateComplete(response, true);
                                    } else {
                                        onUpdateListener.onUpdateComplete(response, false);

                                        // If user is block by admin panel.
                                        if (response.getString(WebField.MESSAGE).equalsIgnoreCase(GlobalData.USER_BLOCK_ALERT)) {


                                            // If use is block and try to login then not pass intent to login otherwise pass intent to Login screen.
                                            if (!url.equalsIgnoreCase(WebField.URL_USER_LOGIN)) {

                                                GlobalData.isUserBlockedByAdmin = true;
                                                SessionManager.clearCredential(context);

                                                Intent intent = new Intent(context, LoginActivity.class);
                                                context.startActivity(intent);
                                            }
                                        } else if (response.getString(WebField.MESSAGE).equalsIgnoreCase(GlobalData.USER_NOT_EXIST)) { // If user is delete from admin panel


                                            GlobalData.isUserNotExist = true;
                                            SessionManager.clearCredential(context);

                                            Intent intent = new Intent(context, LoginActivity.class);
                                            context.startActivity(intent);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (intDialogShow == 1) {
                                    progressDialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            AlertDialogUtility.showToast(context, anError.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonResult) {
        super.onPostExecute(jsonResult);
    }
}