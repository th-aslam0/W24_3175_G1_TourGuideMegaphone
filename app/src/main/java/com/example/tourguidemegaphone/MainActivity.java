package com.example.tourguidemegaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //

        TextView txtViewSignUp = findViewById(R.id.txtSignUpInstead);
        EditText editTxtEmail = findViewById(R.id.editTextEmailAddress);
        SpannableString spannableSignUp = new SpannableString(txtViewSignUp.getText());


        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //Toast.makeText(MainActivity.this, "TO-DO: Open Sign Up activity.", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("EMAIL", editTxtEmail.getText().toString());
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
}