package com.sample.mobile.sampleapp.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.mobile.pregmmaapp.R;

public class AlertDialogUtility {

    public static void SHOW_TOAST(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showAlert(Context context, String msg) {
        new AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name)).setMessage(msg).setCancelable(false).setNeutralButton("OK", null).show();
    }

    public static void SHOW_INTERNET_ALERT(Context context) {
        new AlertDialog.Builder(context).setIcon(0).setTitle(GlobalData.STR_INTERNET_ALERT_TITLE).setMessage(GlobalData.STR_INETRNET_ALERT_MESSAGE)
                .setCancelable(false).setNeutralButton("OK", null).show();
    }

    public static void CustomAlert(Context context, String title, String message, String Positive_text, String Negative_text, DialogInterface.OnClickListener PositiveListener, DialogInterface.OnClickListener NegativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title).setMessage(message).setNeutralButton(Positive_text, PositiveListener).setNeutralButton(Negative_text, NegativeListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showConfirmAlert(Context context, String msg, DialogInterface.OnClickListener onYesClick) {
        new AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name)).setMessage(msg).setCancelable(false).setNegativeButton("NO", null)
                .setPositiveButton("YES", onYesClick).show();
    }
}
