package com.example.recyclerview;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;

public class CreateContact extends AppCompatActivity {

    ImageView image;
    TextView firstname;
    TextView lastname;
    TextView mobile;
    Button save;
    Button Cancel;

    private String taskChoosen;
    final int REQUEST_CODE_GALLLERY = 999;
    final int REQUEST_CODE_CAMERA = 888;
    private Intent intent;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        firstname = findViewById(R.id.ltvFirstName);
        lastname = findViewById(R.id.ltvLastName);
        mobile = findViewById(R.id.ltvMobile);
        save = findViewById(R.id.btn_save);
        Cancel = findViewById(R.id.btnCancel1);
        image = findViewById(R.id.imageView_add);

        uri = Uri.parse("android.resource://com.example.recyclerview/drawable/avatar");

        image.setImageURI(uri);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                ActivityCompat.requestPermissions(
                        CreateContact.this,
                        new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLLERY
                );
*/

                selectImage();


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String f = firstname.getText().toString();
               String l = lastname.getText().toString();
               String m = mobile.getText().toString();

               DatabaseHelper db = new DatabaseHelper(CreateContact.this);

              // boolean success = db.saveContact(f,l,m);
               try {
                   db.insertData(f, l, m, uri,null);
                   Toast.makeText(CreateContact.this,"Contact Added" , Toast.LENGTH_LONG).show();

               }
               catch (Exception e){
                   e.printStackTrace();
               }

            /*   if(success)
               {
                   Toast.makeText(CreateContact.this,"Contact Added" , Toast.LENGTH_LONG).show();
                   Intent intent = new Intent(CreateContact.this,MainActivity.class);
                   startActivity(intent);
               }
              */
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateContact.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return  byteArray;



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

                Toast.makeText(CreateContact.this, "you don't have permission to access file location", Toast.LENGTH_LONG).show();
            }
                return;
        }

        if(requestCode == REQUEST_CODE_CAMERA) {
            if ((grantResults.length>0) && (grantResults[0]==PackageManager.PERMISSION_GRANTED) &&
                    (grantResults[1]==PackageManager.PERMISSION_GRANTED) &&
                    (grantResults[2]==PackageManager.PERMISSION_GRANTED)){

                Intent cameraIntent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);


                startActivityForResult(cameraIntent,REQUEST_CODE_CAMERA);
            }
            else{
                Toast.makeText(this,"No Permission to Access Camera",Toast.LENGTH_LONG).show();
            }
            return;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        if(requestCode == REQUEST_CODE_GALLLERY && resultCode == RESULT_OK && data != null){
             uri =  data.getData();

            Log.d("this","************* " +uri);

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        */


        if(requestCode == REQUEST_CODE_GALLLERY){
            try {
                uri = data.getData();
                Log.d("camera",""+uri);
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Log.d("nik nik ","done "+bitmap.toString());

                image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                Log.d("nik nik ","gallery gallery");
                e.printStackTrace();
            }
        }

        if(requestCode == REQUEST_CODE_CAMERA){
            uri = data.getData();
            Log.d("camera",""+uri);

            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            // imageView.setImageBitmap(photo);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination=new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis()+".jpg");
            FileOutputStream fo;
            String str;
            try {
                destination.createNewFile();
                fo=new FileOutputStream(destination);
                uri=Uri.parse(destination.getAbsolutePath());
                Log.d("camera path",destination.getAbsolutePath());
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(imageBitmap);

          /*  ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();*/
        }



        super.onActivityResult(requestCode, resultCode, data);
    }



    public void selectImage(){
        final CharSequence[] items={"Open Camera","Choose from gallery","Cancel"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Add Contact Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                if (items[which].equals("Open Camera")){
                    taskChoosen="Open Camera";

                    ActivityCompat.requestPermissions(
                            CreateContact.this,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_CAMERA
                    );
                }
                else if (items[which].equals("Choose from gallery")){
                    taskChoosen="Choose from gallery";

                    ActivityCompat.requestPermissions(
                            CreateContact.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_GALLLERY
                    );
                }
                else if (items[which].equals("Cancel")){
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }
}
