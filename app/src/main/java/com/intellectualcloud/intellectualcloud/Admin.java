package com.intellectualcloud.intellectualcloud;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intellectualcloud.intellectualcloud.model.post;

public class Admin extends AppCompatActivity {
    Button addnewpost;
    Button addnewworker;
    Button addnewfeature, btn_changedate;
    EditText et_change;
    DatabaseReference db;
    private static final String DB_URL = "https://intellectualcloud-5fe7b.firebaseio.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setTitle("Admin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addnewpost = findViewById(R.id.addnewpost);
        addnewworker = findViewById(R.id.addnewworker);
        addnewfeature = findViewById(R.id.addnewfeature);
        btn_changedate = findViewById(R.id.changedate);
        et_change = findViewById(R.id.ettime);
        db = FirebaseDatabase.getInstance().getReferenceFromUrl(DB_URL);
        btn_changedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = et_change.getText().toString().trim();
                if (!date.equals("") && date != null && date.length() > 5) {
                    db.child("ReleaseDate").setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Admin.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else Toast.makeText(Admin.this, "write the date correctly" +
                        "", Toast.LENGTH_SHORT).show();
            }


        });
        addnewworker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin.this, addnewemp.class));
                finish();

            }
        });

        addnewfeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin.this, addnewf.class));
                finish();

            }
        });
        addnewpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin.this, addnewpost.class));
                finish();

            }
        });
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
