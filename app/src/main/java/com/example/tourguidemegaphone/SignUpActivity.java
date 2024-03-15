package com.example.tourguidemegaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("EMAIL", "");
        EditText editTxtEmail = findViewById(R.id.editTxtEmail);
        editTxtEmail.setText(email);
    }
}