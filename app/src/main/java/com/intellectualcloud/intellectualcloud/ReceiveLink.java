package com.intellectualcloud.intellectualcloud;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class ReceiveLink extends AppCompatActivity {
    TextView tvforTitle, tvForCon, tvfordec;
    ImageView ivforpic;
    Button sharenot;
    String deeplink, pt = "t", pc = "c", pd = "d", pi = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_link);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvForCon = findViewById(R.id.mconforR);
        tvforTitle = findViewById(R.id.mtitleforR);
        tvfordec = findViewById(R.id.mdescforR);

        ivforpic = findViewById(R.id.ivforR);
        sharenot = findViewById(R.id.shareforR);
        sharenot.setEnabled(false);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            //sample format: https://github.com/st="title"/sd="desc"/


                        }

                        deeplink = deepLink.toString();

                        tvForCon.setText(deeplink);
                        tvforTitle.setText(deeplink);
                        tvfordec.setText(deeplink);
                        PicassoClient.downloadimg(getApplicationContext(), pi, ivforpic);
                        sharenot.setEnabled(true);

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("hey", "getDynamicLink:onFailure", e);
                    }
                });


        sharenot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gcreatedynamiclink();
            }
        });

    }


    private void gcreatedynamiclink() {

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(deeplink))
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


                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            String a = shortLink.toString();
                            // Toast.makeText(Detailed.this, "Inside" + a, Toast.LENGTH_SHORT).show();

                            shareit(a);
                            Uri flowchartLink = task.getResult().getPreviewLink();
                        } else {
                            // Error
                            // ...
                            // Toast.makeText(Detailed.this, "Error", Toast.LENGTH_SHORT).show();
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

