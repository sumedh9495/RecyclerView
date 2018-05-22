package com.example.recyclerview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetails extends AppCompatActivity {

    private static final String TAG = "ContactDetails";
    Button edit;
    Button delete;
    ImageView sms;
    ImageView Call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        edit = findViewById(R.id.btnEdit);
        delete = findViewById(R.id.btnDelete);
        sms = findViewById(R.id.imageView_sms);
        Call = findViewById(R.id.imageView_call_detail);
        getIncomingIntent();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                String fname1 = getIntent().getStringExtra("firstName");
                String lname1 = getIntent().getStringExtra("lastName");
                String mobile1 = getIntent().getStringExtra("Mobile");
                String image1 = getIntent().getStringExtra("image");

                Intent intent = new Intent(ContactDetails.this,EditContact.class);
                intent.putExtra("firstname",fname1);
                intent.putExtra("lastname",lname1);
                intent.putExtra("mobile",mobile1);
                intent.putExtra("image",image1);
                startActivity(intent);

                return true;

            case R.id.item2:
                String mobile3 = getIntent().getStringExtra("Mobile");
                DatabaseHelper db = new DatabaseHelper(ContactDetails.this);
                db.deleteContact(mobile3);
                Toast.makeText(ContactDetails.this,"deleted" ,Toast.LENGTH_LONG);

                Intent intent1 = new Intent(ContactDetails.this,MainActivity.class);
                startActivity(intent1);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getIncomingIntent(){
        Log.d(TAG,"--->> checking for incoming intents");

        if(getIntent().hasExtra("firstName") && getIntent().hasExtra("lastName")
                && getIntent().hasExtra("Mobile")){

            String fname1 = getIntent().getStringExtra("firstName");
            String lname1 = getIntent().getStringExtra("lastName");
            String mobile1 = getIntent().getStringExtra("Mobile");
            String image1 = getIntent().getStringExtra("image");
            String contactid = getIntent().getStringExtra("contactid");
            setData(fname1,lname1,mobile1,image1,contactid);

        }
    }

    private void setData(final String fname ,final String lname , final String Mobile,final String image,final String contactid)
    {
        TextView f= findViewById(R.id.tvFirstName);
        TextView l= findViewById(R.id.tvLastName);
        TextView m = findViewById(R.id.tvMobile);
        ImageView mm = findViewById(R.id.imageView_edit);

        f.setText(fname);
        l.setText(lname);
        m.setText(Mobile);

       /*if(image.equals(" ") || image.equals(null))
       {
            Uri uri = Uri.parse("android.resource://com.example.recyclerview/drawable/avatar.png");
           mm.setImageURI(uri);

           Log.d("In Contact Details "," ======> " + image);
       }
*/
//        Log.d("In Contact Details "," ======> 2" + image );


            mm.setImageURI(Uri.parse(image));



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(ContactDetails.this);
                db.deleteContact(Mobile);
                Toast.makeText(ContactDetails.this,"deleted" ,Toast.LENGTH_LONG);

                Intent intent = new Intent(ContactDetails.this,MainActivity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetails.this,EditContact.class);
                intent.putExtra("firstname",fname);
                intent.putExtra("lastname",lname);
                intent.putExtra("mobile",Mobile);
                intent.putExtra("image",image);
                intent.putExtra("contactid",contactid);
                startActivity(intent);
            }
        });



        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                //        + Mobile)));

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + Mobile));
                intent.putExtra("sms_body", "msg From PhoneBook App");
                startActivity(intent);
            }
        });


        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + Mobile));
                startActivity(i);
            }
        });
    }
}
