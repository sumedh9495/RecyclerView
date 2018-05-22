package com.example.recyclerview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditContact extends AppCompatActivity {

    final int REQUEST_CODE_GALLLERY = 999;
    String uri;
    String contactid;

    TextView mFirstName;
    TextView mLastName;
    TextView mMobile;
    Button mUpdate;
    Button mCancel;
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        String fname1 = getIntent().getStringExtra("firstname");
        String lname1 = getIntent().getStringExtra("lastname");
        String mobile1 = getIntent().getStringExtra("mobile");
        uri =  getIntent().getStringExtra("image");
        contactid = getIntent().getStringExtra("contactid");




        Log.d("in Edit Contact","==++--> " + fname1);

        mFirstName =  (TextView) findViewById(R.id.etFirstName);
        mLastName = (TextView) findViewById(R.id.etLastName);
        mMobile =  (TextView) findViewById(R.id.etMobile);
        mUpdate = (Button) findViewById(R.id.btnUpdate);
        mCancel = (Button) findViewById(R.id.btnCancel1);
        mImage = (ImageView) findViewById(R.id.imageView_edit);

        mFirstName.setText(fname1);
        mLastName.setText(lname1);
        mMobile.setText(mobile1);
        mImage.setImageURI(Uri.parse(uri));


        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        EditContact.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLLERY
                );
            }
        });






        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditContact.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final DatabaseHelper db = new DatabaseHelper(EditContact.this);
         final int abc = db.getId(mFirstName.getText().toString(),mMobile.getText().toString());

        Log.d("this","555555 = > " +abc);

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = mFirstName.getText().toString();
                String b = mLastName.getText().toString();
                String c = mMobile.getText().toString();

                int success = db.updateContact(a,b,c,abc,uri,contactid);
                boolean x;

                if(success==0)
                {
                    x = false;
                }
                else
                {
                    x = true;
                }

                if(x){
                    Toast.makeText(EditContact.this,"Contact Updated" , Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(EditContact.this,MainActivity.class);
                    startActivity(intent);

                }


            }
        });

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLLERY);
            }
            else {

                Toast.makeText(EditContact.this, "you don't have permission to access file location", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GALLLERY && resultCode == RESULT_OK && data != null){
            uri =  String.valueOf(data.getData());

            Log.d("this","************* " +uri);

            try {
                InputStream inputStream = getContentResolver().openInputStream(Uri.parse(uri));
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                mImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
