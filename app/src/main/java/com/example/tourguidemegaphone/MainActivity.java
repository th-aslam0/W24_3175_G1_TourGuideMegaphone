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

import com.example.tourguidemegaphone.databases.CurrencyRatesDAO;
import com.example.tourguidemegaphone.databases.LoginDao;
import com.google.gson.annotations.SerializedName;

public class MainActivity extends AppCompatActivity {
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

        loginDao = new LoginDao(MainActivity.this);

        //set the username and password from the db, if they exist
        String email = loginDao.getLastLoginEmail();
        if (email != null){
            editTxtEmail.setText(email);
            editTxtPassword.setText(loginDao.getUserToken(email));
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTxtEmail.getText().toString();
                String password = editTxtPassword.getText().toString();
                Log.d("LOGIN", email + "  -  " + password);
                login(email, password);
            }
        });
        
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //Toast.makeText(MainActivity.this, "TO-DO: Open Sign Up activity.", Toast.LENGTH_SHORT).show();
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

    private void login(String email, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourguidemegaphone-e5d7117dd068.herokuapp.com/")
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
                    Log.d("LOGIN", "onResponse: " + loginResponse);
                    Log.d("LOGIN", "Token: " + token);
                    Log.d("LOGIN", "LoginResponse: " + loginResponse.toString());

                    //To-do: If we have a token stored in the database, use that as login
                    // Here we need to add the session in database
                    saveLoginDataToDb(email, password, token);

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

    private void saveLoginDataToDb(String email, String password, String token) {
        //Warning: We are saving the password in cleartext instead of the token,
        //until the backends is updated and starts returning a token.
        loginDao.saveUserSession(email, password);
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
        private String token;

        public String getToken() {
            return token;
        }

        @Override
        public String toString() {
            return "LoginResponse{" +
                    "token='" + token + '\'' +
                    '}';
        }
    }
    public interface ApiService {
        @POST("login")
        Call<LoginResponse> login(@Body LoginRequest loginRequest);
    }
}