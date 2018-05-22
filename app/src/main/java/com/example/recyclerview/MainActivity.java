package com.example.recyclerview;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FloatingActionButton fab;
    DatabaseHelper helper;

    ArrayList<NewContact> contacts;

    // vars
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressDialog dialog=new ProgressDialog(MainActivity.this);
        dialog.setMessage("message");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        fetchContacts();

        dialog.hide();

        helper = new DatabaseHelper(this);

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,
                        CreateContact.class);

                startActivity(myIntent);
            }
        });


        Log.d(TAG,"-------> onCreate: stared");
        initImageBitmaps();




    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.getAllContacts();
        initRecyclerView();
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        initRecyclerView();
    }

    private void initRecyclerView(){

        DatabaseHelper h = new DatabaseHelper(this);

      //  ArrayList<Contact> all=(ArrayList<Contact>) h.viewAll();

        ArrayList<NewContact> all=(ArrayList<NewContact>) h.getAllContacts();

        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, all);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    public void fetchContacts(){

        contacts = new ArrayList<>();
        String email = null;

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Email.ADDRESS};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder= null;

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri,projection,selection,selectionArgs,sortOrder);




        while(cursor.moveToNext()){

            NewContact newContact = new NewContact();

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String ID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));



            String newNumber="";
            for(int i=0;i<num.length();i++){
                if(Character.isDigit(num.charAt(i))) {
                    newNumber=newNumber+""+num.charAt(i);
                }
            }



/*

            Cursor emailCursor=resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Email.DATA+" = ?",new String[]{ ID },null);

            while (emailCursor.moveToNext()){
                email=emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                Log.d("MY INFO => :",email);
            }


*/




            String[] projection2 = {ContactsContract.CommonDataKinds.Email.ADDRESS};
            Cursor cursor2 = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,projection2
                    ,null,null,null);

            while (cursor2.moveToNext()){
                email= cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

            }






            String photo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            if(photo == null)
            {
                photo = "android.resource://com.example.recyclerview/drawable/avatar";
            }

            newContact.setName(name);
            newContact.setNumber(newNumber);
            newContact.setPhoto(photo);
            newContact.setEmail(email);

            contacts.add(newContact);
          DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            Uri photo1 = Uri.parse(photo);
             db.insertData(name,email,newNumber,photo1,ID);
            Log.d(TAG,"name >>> "+ name + ", number >>>> " + num + ", Photo Uri >>>> " + photo + ", Email ==" +email );

        }



       // return  contacts;
    }

}
