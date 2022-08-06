package com.miniproject.pickupalarm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.miniproject.pickupalarm.Adapters.ContactAdapter;
import com.miniproject.pickupalarm.Models.ContactModel;
import com.miniproject.pickupalarm.R;
import com.miniproject.pickupalarm.Singletones.Alert;

public class SelectedPhoneActivity extends AppCompatActivity {

    private RecyclerView selectedPhoneNumberRecyclerView;
    private ContactAdapter contactAdapter;

    private Alert alert;

    private ImageButton addContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_phone);

        alert = Alert.getInstance().setActivity(SelectedPhoneActivity.this);
        selectedPhoneNumberRecyclerView = findViewById(R.id.selected_phone_number);
        addContact = findViewById(R.id.add_contacts);
        contactAdapter = new ContactAdapter(alert.getSelectedContact(), true);
        selectedPhoneNumberRecyclerView.setLayoutManager(new LinearLayoutManager(SelectedPhoneActivity.this, LinearLayoutManager.VERTICAL, false));
        selectedPhoneNumberRecyclerView.setAdapter(contactAdapter);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectedPhoneActivity.this, SelectPhoneNumberActivity.class));
                finish();
            }
        });
    }
}