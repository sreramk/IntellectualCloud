package com.intellectualcloud.intellectualcloud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.intellectualcloud.intellectualcloud.model.post;

public class About extends AppCompatActivity {
    private static final String DB_URL = "https://intellectualcloud-5fe7b.firebaseio.com/";
    private DatabaseReference db;
   String link;//githubl;
    Button  btn_call, btn_mail;
    ListView lvforworkers, lvforfeatures;
    TextView tv_teamcon, tv_teamdesc, tv_title;
    ImageView iv_teamimg;
    private String DB_URLforAbout = "https://intellectualcloud-5fe7b.firebaseio.com/About";
    FirebaseClient firebaseClient;
    private String DB_URLforworkers = "https://intellectualcloud-5fe7b.firebaseio.com/Team";
    private String DB_URLforfeatures =
            "https://intellectualcloud-5fe7b.firebaseio.com/Primaryfeatures";
    RelativeLayout rlaboutl;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("About Us");

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollv);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        iv_teamimg = findViewById(R.id.teampic);
        tv_teamcon = findViewById(R.id.teamcon);
        tv_teamdesc = findViewById(R.id.teamdesc);
        lvforworkers = findViewById(R.id.lv_forworkers);
        lvforfeatures = findViewById(R.id.lv_forfeatures);
        rlaboutl = findViewById(R.id.aboutrl);
        btn_call = findViewById(R.id.btn_number);
        btn_mail = findViewById(R.id.btn_contactus);

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9940245619"));
                startActivity(intent);

            }
        });
        btn_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","myintellectualcloud@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Write the subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

            }
        });

        abouttheteam();

        firebaseClient = new FirebaseClient(this, DB_URLforworkers, lvforworkers);

        firebaseClient.refreshdata();
        lvforworkers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                post post = (post) adapterView.getItemAtPosition(i);
              link= post.getPost_content();

                View parentLayout = findViewById(R.id.aboutrl);
                Snackbar.make(parentLayout, "Want to see my works? Check My Github page! ", Snackbar.LENGTH_LONG)
                        .setAction("Yeah!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent a = new Intent(Intent.ACTION_VIEW);
                                if (!link.startsWith("http://") && !link.startsWith("https://"))
                                    link = "http://" + link;
                                a.setData(Uri.parse(link));
                                startActivity(a);

                            }
                        })
                .setActionTextColor(getResources().getColor(android.R.color.white))
                        .show();
            }
        });


        firebaseClient = new FirebaseClient(this, DB_URLforfeatures, lvforfeatures);
        firebaseClient.refreshdata();
    }


    private void abouttheteam() {

        db = FirebaseDatabase.getInstance().getReferenceFromUrl(DB_URLforAbout);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post p = dataSnapshot.getValue(post.class);
                setteamdetails(p);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setteamdetails(post p) {
        tv_teamdesc.setText(p.getPost_description());
        tv_teamcon.setText(p.getPost_content());
        PicassoClient.downloadimg(this, p.getImg_path(), iv_teamimg);
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
