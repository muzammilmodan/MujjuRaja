package com.sample.mobile.sampleapp.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class MyTextView extends AppCompatTextView {

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //

        String str = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "txt_custom_font");

        String fontName = "";
        try {
            fontName = context.getString(Integer.parseInt(str.substring(1).trim()));
        } catch (Exception e) {
        	e.printStackTrace();
        }
        // Log.d("log_tag", "fontName: "+fontName);

        setFont(context, fontName);
    }

    public void setFont(Context context, String fontName) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
        setTypeface(font);

    }

    public void resizeAndSetText(String text, int maxLength) {
        if (text.length() > maxLength) {
            text = text.substring(0, maxLength) + "...";
        }

        this.setText(text);
    }
}
