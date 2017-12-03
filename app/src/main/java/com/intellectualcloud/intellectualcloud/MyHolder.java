package com.intellectualcloud.intellectualcloud;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MyHolder {
    TextView tvdescription;

     ImageView img;
    TextView tvtitle;

    public MyHolder(View itemView) {


        tvtitle = itemView.findViewById(R.id.tvTitle);
        img = itemView.findViewById(R.id.ivpost);
        tvdescription = itemView.findViewById(R.id.tvDescription);


    }
}
