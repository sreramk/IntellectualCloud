package com.intellectualcloud.intellectualcloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.gson.Gson;
import com.intellectualcloud.intellectualcloud.model.post;
import com.squareup.picasso.Picasso;

public class Detailed extends AppCompatActivity {

    TextView tvmtitle, tvmcon, tvmdesc;
    ImageView iv;
    Button btn_share;
    String share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Gson gson = new Gson();
        String data = getIntent().getExtras().getString("obj", "fool");
        post post = new post();
        tvmcon = findViewById(R.id.mcon);
        tvmdesc = findViewById(R.id.mdesc);
        tvmtitle = findViewById(R.id.mtitle);
        iv = findViewById(R.id.imgview);
        btn_share = findViewById(R.id.share);

        if (!data.equals("fool")) {
            post = gson.fromJson(data, post.class);
            tvmtitle.setText(post.getPost_title());
            tvmdesc.setText(post.getPost_description());
            tvmcon.setText(post.getPost_content());
            String url = post.getImg_path();
            share = post.getPost_title();
            drawimg(url);
        }
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check this article " + share);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

    }

    private String createDynamicLink() {


        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://youtube.com/"))
                .setDynamicLinkDomain("xb6hq.app.goo.gl")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.intellectualcloud.intellectualcloud")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("Example of a Dynamic Link")
                                .setDescription("This link works whether the app is installed or not!")
                                .build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        return dynamicLinkUri.toString();
    }

    private void drawimg(String url) {
        if (url != null && url.length() > 0) {
            Picasso.with(this).load(url).placeholder(R.drawable.placeholder).into(iv);

        } else {
            Picasso.with(this).load(R.drawable.placeholder).into(iv);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, Home.class));
    }
}
