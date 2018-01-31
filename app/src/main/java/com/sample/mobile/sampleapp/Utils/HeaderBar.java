package com.sample.mobile.sampleapp.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jobseekerapp.R;


public class HeaderBar extends RelativeLayout {

    public TextView tvTitle,tvHomeTitle;
    public ImageView ivLeft, ivRight,ivRightOfLeft;
    public RelativeLayout rrHomeBtn;
    private Context context;
    View view;
    public HeaderBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public HeaderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void init() {
        try {
             view = LayoutInflater.from(context).inflate(R.layout.headerbar, this, true);
//            rrHomeBtn = (RelativeLayout) findViewById(R.id.rrHomeBtn);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);

//            ivMessage = (ImageView) findViewById(R.id.ivMessage);
//            ivFriendReq = (ImageView) findViewById(R.id.ivFriendReq);
//            ivFrindDetails = (ImageView) findViewById(R.id.ivFrindDetails);
            tvHomeTitle = (TextView) view.findViewById(R.id.tvHomeTitle);

            ivLeft = (ImageView) view.findViewById(R.id.ivLeft);
            ivRightOfLeft= (ImageView) view.findViewById(R.id.ivRightOfLeft);
            ivRight = (ImageView) view.findViewById(R.id.ivRight);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
