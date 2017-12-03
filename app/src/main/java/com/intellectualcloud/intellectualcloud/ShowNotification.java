package com.intellectualcloud.intellectualcloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ShowNotification extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);

        Intent i = getIntent();


        String title = i.getStringExtra("title");
        String body = i.getStringExtra("body");
        String url = i.getStringExtra("picture_url");

        Toast.makeText(this, title+body+url, Toast.LENGTH_SHORT).show();
    }
}
