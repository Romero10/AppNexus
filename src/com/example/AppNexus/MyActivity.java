package com.example.AppNexus;

import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DispayedContacts();
        Button btnNewContact = (Button) findViewById(R.id.button);
        Button btnCancel = (Button) findViewById(R.id.button3);


        View.OnClickListener NewContact = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddContactForm();
            }
        };

        View.OnClickListener Cancel = new View.OnClickListener(){
            @Override
        public void onClick(View v){onDestroy();}
        };

        btnNewContact.setOnClickListener(NewContact);

        btnCancel.setOnClickListener(Cancel);


    }

    public ArrayList<String> getContacts(Context context) {
        ArrayList<String> contacts = new ArrayList<String>();

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                cursor.moveToNext();
            }

        }
        return contacts;
    }

    public ArrayList<String> getPhones(Context context){
        ArrayList<String> phone = new ArrayList<String>();

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            final String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                Cursor pCur = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null);
                pCur.moveToFirst();
                phone.add(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                pCur.close();
                cursor.moveToNext();
            }
        }
        return phone;
    }

    public void DispayedContacts(){

        ListView listview = (ListView) findViewById(R.id.listView);
        List<String> contact = getContacts(getBaseContext());
        List<String> phone = getPhones(getBaseContext());
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i=0; i<contact.size(); i++) {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("contact", contact.get(i));
            datum.put("phone", phone.get(i));
            data.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"contact", "phone"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        listview.setAdapter(adapter);
    }

    public void OpenAddContactForm(){
        Intent intent = new Intent(MyActivity.this, AddContact.class);
        startActivity(intent);
    }

    public void onDestroy(){
        moveTaskToBack(true);
        super.onDestroy();

        System.runFinalizersOnExit(true);
        System.exit(0);
    }
}


