package com.miniproject.pickupalarm.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.miniproject.pickupalarm.R;

public class AuthenticationActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private Button loginButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        phoneNumber = findViewById(R.id.phoe_number);
        loginButton = findViewById(R.id.login_btn);
        mAuth = FirebaseAuth.getInstance();

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (phoneNumber.getText().toString().length() != 10)
                    phoneNumber.setError("Phone number should contain 10 numbers.");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signInWithEmailAndPassword(
                        String.format("%s@gmail.com", phoneNumber.getText().toString()),
                                phoneNumber.getText().toString()
                        )
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                if (e.getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                                    mAuth.createUserWithEmailAndPassword(
                                            String.format("%s@gmail.com", phoneNumber.getText().toString()),
                                                    phoneNumber.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(AuthenticationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else
                                    Toast.makeText(AuthenticationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}