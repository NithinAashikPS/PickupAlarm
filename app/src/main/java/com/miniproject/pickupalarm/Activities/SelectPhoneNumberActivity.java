package com.miniproject.pickupalarm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;

import com.miniproject.pickupalarm.Adapters.ContactAdapter;
import com.miniproject.pickupalarm.Interfaces.ContactSelect;
import com.miniproject.pickupalarm.Models.ContactModel;
import com.miniproject.pickupalarm.R;
import com.miniproject.pickupalarm.Services.LiveLocationService;
import com.miniproject.pickupalarm.Singletones.Alert;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SelectPhoneNumberActivity extends AppCompatActivity {

    private List<ContactModel> contactModelList;
    private RecyclerView contactRecyclerView;
    private ContactAdapter contactAdapter;
    private ContactModel contactModel;

    private List<ContactModel> selectedContact;
    private ImageButton setAlarm;

    private Alert alert;
    private Intent alertIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_phone_number);

        alertIntent = new Intent(SelectPhoneNumberActivity.this, LiveLocationService.class);
        alert = Alert.getInstance();
        contactModelList = getContacts(SelectPhoneNumberActivity.this);

        selectedContact = alert.getSelectedContact();
        contactRecyclerView = findViewById(R.id.conact_recyclerview);
        setAlarm = findViewById(R.id.set_alarm);
        contactAdapter = new ContactAdapter(contactModelList, false, new ContactSelect() {
            @Override
            public void onSelect(int position, boolean selectionStatus) {
                contactModel = contactModelList.get(position);
                if (selectionStatus)
                    selectedContact.add(contactModel);
                contactModel.setSelected(selectionStatus);
                if (!selectionStatus)
                    selectedContact.remove(contactModel);
                contactModelList.set(position, contactModel);
                if (selectedContact.size() >= 1)
                    setAlarm.setVisibility(View.VISIBLE);
                else
                    setAlarm.setVisibility(View.GONE);
                contactAdapter.notifyDataSetChanged();
            }
        });
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(SelectPhoneNumberActivity.this, LinearLayoutManager.VERTICAL, false));
        contactRecyclerView.setAdapter(contactAdapter);

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.setSelectedContact(selectedContact);
                ContextCompat.startForegroundService(SelectPhoneNumberActivity.this, alertIntent);
                startActivity(new Intent(SelectPhoneNumberActivity.this, SelectedPhoneActivity.class));
            }
        });

    }

    @SuppressLint("Range")
    public List<ContactModel> getContacts(Context ctx) {

        List<ContactModel> list = new ArrayList<>();

        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Bitmap photo = null;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    while (cursorInfo.moveToNext()) {

                        ContactModel info = new ContactModel();
                        info.setId(id);
                        info.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        info.setMobileNumber(cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        info.setPhoto(photo);
                        info.setPhotoURI(pURI);
                        info.setSelected(false);

                        list.add(info);
                    }

                    cursorInfo.close();
                }
            }
            cursor.close();
        }
        return list;
    }

}