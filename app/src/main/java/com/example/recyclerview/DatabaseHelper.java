package com.example.recyclerview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager.db";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_IMAGE = "image_data";
    private static final String KEY_CONTACTSID = "contact_id";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,8 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement," + KEY_FIRSTNAME + " TEXT," +
                KEY_EMAIL + " TEXT,"+
                KEY_PH_NO + " TEXT,"
                + KEY_IMAGE + " TEXT,"+ KEY_CONTACTSID + " TEXT"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        onCreate(db);

    }

    // code to get all contacts in a list view
    public List<NewContact> getAllContacts() {
        List<NewContact> contactList = new ArrayList<NewContact>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NewContact contact = new NewContact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setEmail(cursor.getString(2));
                contact.setNumber(cursor.getString(3));
                contact.setPhoto(cursor.getString(4));
                contact.setContactid(cursor.getString(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;

    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return  database.rawQuery(sql,null);
    }



    public boolean saveContact(String first_name, String last_name, String mob_number) {
        SQLiteDatabase mydb = this.getWritableDatabase();
        Log.d("nik nik ",""+first_name+"  ");

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FIRSTNAME,first_name);
        contentValues.put(KEY_EMAIL,last_name);
        contentValues.put(KEY_PH_NO,mob_number);

        long result = mydb.insert(TABLE_CONTACTS,null,contentValues);
        if(result == -1)
            return false;
        return true;
    }



    public void deleteContact(String mobile) {

        Log.d("this","==in delete" + mobile);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_PH_NO + " = ?",
                new String[] { String.valueOf(mobile) });
        db.close();


    }

    public int updateContact(String a, String b ,String c,int d,String e,String f) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, a );
        values.put(KEY_EMAIL, b);
        values.put(KEY_PH_NO, c);
        values.put(KEY_IMAGE,e);
      //  values.put(KEY_CONTACTSID,f);

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(d) });
    }

    public int getId(String firstName,String MobileNUmber)
    {
        Log.d("In getId ","=--=-=-=- + " + MobileNUmber);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_FIRSTNAME,KEY_EMAIL , KEY_PH_NO }, KEY_PH_NO + "=?",
                new String[] { String.valueOf(MobileNUmber) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


        int id = Integer.parseInt(cursor.getString(0));
        Log.d("-----","Id======= " + id);

        return id;
    }


    public int getId1(String firstName,String contact_id)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_FIRSTNAME,KEY_EMAIL , KEY_PH_NO,KEY_IMAGE,KEY_CONTACTSID }, KEY_CONTACTSID + "=?",
                new String[] { String.valueOf(contact_id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();



        int id = Integer.parseInt(cursor.getString(0));

        return id;
    }





    public void insertData(String first_name, String last_name, String mob_number , Uri image,String contact_id) {
        SQLiteDatabase db = getWritableDatabase();



        int flag = 0;



        Cursor cursor = getAll();
        String image1 = String.valueOf(image);

        while (cursor.moveToNext()) {
            if (cursor.getString(5).equals(contact_id) ) {

                int id =  getId1(first_name,contact_id);
                updateContact(first_name,last_name,mob_number,id,image1,contact_id);


                flag = 1;
            break;}
        }

        long result;

        if (flag == 0) {
            Log.d("IN Insert","hello");



            String sql = "Insert into contacts values (Null,?,?,?,?,?)";

            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1, first_name);
            statement.bindString(2, last_name);
            statement.bindString(3, mob_number);
            statement.bindString(4, String.valueOf(image));
            statement.bindString(5,contact_id);
            statement.executeInsert();
        }
    }

    public Cursor getAll()
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_CONTACTS,null);
        return res;
    }



}
