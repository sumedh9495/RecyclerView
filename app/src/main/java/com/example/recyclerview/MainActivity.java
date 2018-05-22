package com.example.recyclerview;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "MainActivity";
    FloatingActionButton fab;
    DatabaseHelper helper;
    RecyclerViewAdapter adapter;

    ArrayList<NewContact> contacts;

    // vars
    private Context mContext;
    Intent intent;







    SearchView searchView;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 111;
    //public static final int PERMISSIONS_REQUEST_WRITE_CONTACTS = 222;



    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }














    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       // new DownloadContactDetails().execute();

        fetchContacts();


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
         adapter = new RecyclerViewAdapter(this, all);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    public void fetchContacts() {

        contacts = new ArrayList<>();
        String email = null;

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Email.ADDRESS};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder);


        while (cursor.moveToNext()) {

            NewContact newContact = new NewContact();

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String ID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));


            String newNumber = "";
            for (int i = 0; i < num.length(); i++) {
                if (Character.isDigit(num.charAt(i))) {
                    newNumber = newNumber + "" + num.charAt(i);
                }
            }



/*

            Cursor emailCursor=resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                 SearchManager searchManager   ContactsContract.CommonDataKinds.Email.DATA+" = ?",new String[]{ ID },null);

            while (emailCursor.moveToNext()){
                email=emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                Log.d("MY INFO => :",email);
            }


*/


            String[] projection2 = {ContactsContract.CommonDataKinds.Email.ADDRESS};
            Cursor cursor2 = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection2
                    , null, null, null);

            while (cursor2.moveToNext()) {
                email = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

            }


            String photo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            if (photo == null) {
                photo = "android.resource://com.example.recyclerview/drawable/avatar";
            }

            newContact.setName(name);
            newContact.setNumber(newNumber);
            newContact.setPhoto(photo);
            newContact.setEmail(email);

            contacts.add(newContact);
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            Uri photo1 = Uri.parse(photo);
            db.insertData(name, email, newNumber, photo1, ID);
            Log.d(TAG, "name >>> " + name + ", number >>>> " + num + ", Photo Uri >>>> " + photo + ", Email ==" + email);

        }


        // return  contacts;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<NewContact> newContacts = new ArrayList<>();
        for (NewContact contact: contacts){
            String name = contact.getName().toLowerCase();
            if(name.contains(newText))
                newContacts.add(contact);
        }
        adapter.setFilter(newContacts);
        //adapter = new ContactAdapterRecyclerView(this,newContacts);
        return true;
    }



    class DownloadContactDetails extends AsyncTask<Void,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Wait..");
            progressDialog.setMessage("Fetching all the contacts, it may take some time.");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                //checkPermission();

              //  checkPermission();
                //db.viewAll();
                Toast.makeText(MainActivity.this,"Loaded",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            intent=new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
        }

    }






}
