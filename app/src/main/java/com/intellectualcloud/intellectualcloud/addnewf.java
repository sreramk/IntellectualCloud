package com.intellectualcloud.intellectualcloud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.intellectualcloud.intellectualcloud.model.post;

import java.io.IOException;

public class addnewf extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText /*et_desc,*/ et_title, et_con,et_d;
    DatabaseReference db;
    private static final String DB_URL = "https://intellectualcloud-5fe7b.firebaseio.com/Primaryfeatures";

    Button btn_submit;
    ImageButton btn_choosepic;
    Uri uri;
    String title, content, description;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewf);
        getSupportActionBar().setTitle("Add new Feature");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        et_con = findViewById(R.id.fcontent);
       et_d=findViewById(R.id.fdescription);
        et_title = findViewById(R.id.fname);
        btn_choosepic = findViewById(R.id.choosepicf);
        btn_submit = findViewById(R.id.submitf);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        btn_choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startposting();
            }
        });


    }

    private void startposting() {
        ProgressDialog pd = new ProgressDialog(addnewf.this);
        pd.setMessage("UPloading");
        pd.show();
        title = et_title.getText().toString().trim();
        content = et_con.getText().toString().trim();
        description = et_d.getText().toString().trim();
        db = FirebaseDatabase.getInstance().getReferenceFromUrl(DB_URL);
        final String id = db.push().getKey();
        mStorageRef.child("post_images").child(id).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downoaduri = taskSnapshot.getDownloadUrl();
                db.child(id).setValue(new post(title, content, description, downoaduri.toString()));
            }
        });
        pd.dismiss();
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(addnewf.this, Home.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.choosepicf);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

