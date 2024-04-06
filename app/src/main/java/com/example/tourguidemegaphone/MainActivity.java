package com.example.tourguidemegaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourguidemegaphone.databases.LoginDao;
import com.example.tourguidemegaphone.model.User;

public class MainActivity extends AppCompatActivity implements LoginDao.LoginResponseCallback {
    private LoginDao loginDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtViewSignUp = findViewById(R.id.txtSignUpInstead);
        EditText editTxtEmail = findViewById(R.id.editTextEmailAddress);
        EditText editTxtPassword = findViewById(R.id.editTextPassword);
        SpannableString spannableSignUp = new SpannableString(txtViewSignUp.getText());
        Button btnSignIn = findViewById(R.id.btnSignIn);

        loginDao = LoginDao.getInstance(MainActivity.this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTxtEmail.getText().toString();
                String password = editTxtPassword.getText().toString();
                Log.d("LOGIN", email + "  -  " + password);
                User newLoggingInUser = new User(email, password, null, null, null);
                loginDao.login(newLoggingInUser, MainActivity.this);
            }
        });
        
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Bundle bundle = new Bundle();
                bundle.putString("LOGIN", "email: " + editTxtEmail.getText().toString());
                Intent intent = new Intent
                        (MainActivity.this,SignUpActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        };

        // setting a link to "sign up" part.
        spannableSignUp.setSpan(clickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtViewSignUp.setText(spannableSignUp);
        txtViewSignUp.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onLoginResponseReceived(LoginDao.LoginResponse loginResponse) {
        //Check for failure
        if (loginResponse == null) {
            Toast.makeText(MainActivity.this, "There was an error in the login process.", Toast.LENGTH_SHORT).show();
        } else if (loginResponse.getError() != null) {
            Toast.makeText(MainActivity.this, "Login error: " + loginResponse.getError(), Toast.LENGTH_SHORT).show();
        } else {
            //Successful response
            Intent intent;
            switch (loginResponse.getRole()) {
                case "Tour Guide":
                    intent = new Intent(MainActivity.this, TourGuideHomeActivity.class);
                    startActivity(intent);
                    break;
                case "Tourist":
                    intent = new Intent(MainActivity.this, TouristHomeActivity.class);
                    startActivity(intent);
            }
        }
    }

    @Override
    public void onLoginResponseError(String error) {
        Toast.makeText(MainActivity.this, "Login error: " + error, Toast.LENGTH_SHORT).show();
        Log.d("MAINACT", "Login error: " + error);
    }

    @Override
    public void onNetworkError(Throwable t) {
        Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d("MAINACT", "Network error: " + t.getMessage());
    }
}