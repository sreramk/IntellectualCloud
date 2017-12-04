package com.intellectualcloud.intellectualcloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.intellectualcloud.intellectualcloud.model.post;
import com.squareup.picasso.Picasso;

public class Detailed extends AppCompatActivity {

    TextView tvmtitle, tvmcon, tvmdesc;
    ImageView iv;
    Button btn_share;
    String pt, pc, pd, pi;

    /* pt -post title,pd - post desc, pc - post content,pi - post img */
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

            pt = post.getPost_title();
            tvmtitle.setText(pt);
            pd = post.getPost_description();
            tvmdesc.setText(pd);
            pc = post.getPost_content();
            tvmcon.setText(pc);
            pi = post.getImg_path();

            drawimg(pi);
        }
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharelink();
            }
        });

    }

    String shortlinkcreator(String url) {
        return url;
    }

    private void sharelink() {


        gcreatedynamiclink();

    }


    //https://github.com/" + pt + pd + pc + pi


    private void gcreatedynamiclink() {

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://github.com/ " + "<pt>" + pt + "</pt>" + "<pd>" + pd + "</pd>" + "<pc>" + pc + "</pc>" + "<pi> " + pi + "</pi>"))
                .setDynamicLinkDomain("xb6hq.app.goo.gl")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.intellectualcloud.intellectualcloud")
                                .build())

                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(pt)
                                .setDescription(pd)
                                .setImageUrl(Uri.parse(pi))
                                .build())

                // ...
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            String a = shortLink.toString();
                            Toast.makeText(Detailed.this, "Inside" + a, Toast.LENGTH_SHORT).show();

                            shareit(a);
                            Uri flowchartLink = task.getResult().getPreviewLink();
                        } else {
                            // Error
                            // ...
                            Toast.makeText(Detailed.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void shareit(String msg) {
        if (msg != null) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Did you know " + pt + "? \n" +
                            msg);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else
            Toast.makeText(this, "NO", Toast.LENGTH_LONG).show();

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
