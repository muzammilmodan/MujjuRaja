package com.sample.mobile.sampleapp.Utils;

import android.content.Context;
import android.provider.Settings.Secure;

public class DeviceId {
    public static String getDeviceId(Context context) {
        String deviceId = "";
        try {
            deviceId = Secure.getString(context.getContentResolver(),
                    Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId;
    }


}
