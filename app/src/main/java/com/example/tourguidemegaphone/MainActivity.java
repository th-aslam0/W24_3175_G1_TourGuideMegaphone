package com.example.tourguidemegaphone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

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

import com.google.gson.annotations.SerializedName;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtViewSignUp = findViewById(R.id.txtSignUpInstead);
        EditText editTxtEmail = findViewById(R.id.editTextEmailAddress);
        EditText editTxtPassword = findViewById(R.id.editTextPassword);
        SpannableString spannableSignUp = new SpannableString(txtViewSignUp.getText());
        Button btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTxtEmail.getText().toString();
                String password = editTxtPassword.getText().toString();
                Log.d("DEBUF", email + "  -  " + password);
                login(email, password);
            }
        });
        
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

    private void login(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourguidemegaphone-e5d7117dd068.herokuapp.com/") // Replace this with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    String token = loginResponse.getToken();
                    Log.d("DEBUF", "onResponse: " + loginResponse);

                    Intent intent = new Intent(MainActivity.this, TourGuideHomeActivity.class);
                    startActivity(intent);

                    // Handle successful login, e.g., save token to SharedPreferences
                } else {
                    // Handle unsuccessful login
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(MainActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class LoginRequest {
        @SerializedName("email")
        private String email;
        @SerializedName("password")
        private String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    public class LoginResponse {
        @SerializedName("token")
        private String message;

        public String getToken() {
            return message;
        }
    }
    public interface ApiService {
        @POST("login")
        Call<LoginResponse> login(@Body LoginRequest loginRequest);
    }
}