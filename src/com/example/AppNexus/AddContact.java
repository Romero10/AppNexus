package com.example.AppNexus;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Roman on 06.05.2015.
 */
public class AddContact extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.add_contacts);

        Button btnNewContact = (Button) findViewById(R.id.button2);

        View.OnClickListener btn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor = getBaseContext().getContentResolver().query(
                        ContactsContract.Contacts.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );

                getBaseContext().getContentResolver();
                 EditText edit2 = (EditText) findViewById(R.id.editText);
                 EditText edit1 = (EditText) findViewById(R.id.editText2);
                ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
                op.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                op.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                        .withValueBackReference(Data.RAW_CONTACT_ID, cursor.getCount())
                        .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(StructuredName.DISPLAY_NAME, edit1.getText().toString())
                        .build());

                op.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValueBackReference(Data.RAW_CONTACT_ID, cursor.getCount())
                        .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(Phone.NUMBER, edit2.getText().toString())
                        .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                        .build());

                try{
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY,op);
                }
                catch (Exception e){

                }

            }
        };

        btnNewContact.setOnClickListener(btn);
    }
}
