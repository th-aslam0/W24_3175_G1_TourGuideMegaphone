package com.example.tourguidemegaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tourguidemegaphone.model.User;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("EMAIL", "");
        EditText editTxtFirstName = findViewById(R.id.editTxtFirstName);
        EditText editTxtLastName = findViewById(R.id.editTxtLastName);

        EditText editTxtEmail = findViewById(R.id.editTxtSignUpEmail);
        EditText editTxtPassword = findViewById(R.id.editTxtSignUpPassword);
        Button signupBtn = findViewById(R.id.signupBtn);
        RadioGroup roleRG = findViewById(R.id.su_rg_roleRG);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editTxtFirstName.getText().toString();
                String lastName = editTxtLastName.getText().toString();
                String email = editTxtEmail.getText().toString();
                String password = editTxtPassword.getText().toString();
                int selectedRadioButtonId = roleRG.getCheckedRadioButtonId();
                // Find the selected radio button
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

                // Get the text of the selected radio button
                String selectedOption = selectedRadioButton.getText().toString();

                Log.d("DEBUF", firstName + " - " + lastName + " - " + email + "  -  " + password);
                signUp(firstName, lastName, email, password, selectedOption);
            }
        });
    }

    private void signUp(String firstName, String lastName, String email,
                        String password, String role) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourguidemegaphone-e5d7117dd068.herokuapp.com/") // Replace this with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SignUpActivity.ApiService apiService = retrofit.create(SignUpActivity.ApiService.class);

        SignUpActivity.SignupRequest signUpRequest = new SignUpActivity.SignupRequest(firstName, lastName, email, password, role);

        Call<User> call = apiService.signup(signUpRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User signUpResponse = response.body();
                    String role = signUpResponse.getRole();
                    Log.d("DEBUF", "onResponse: " + signUpResponse);
                    Intent intent;
                    switch (role) {
                        case "Tour Guide":
                            intent = new Intent(SignUpActivity.this, TourGuideHomeActivity.class);
                            startActivity(intent);
                            break;
                        case "Tourist":
                            intent = new Intent(SignUpActivity.this, TouristHomeActivity.class);
                            startActivity(intent);
                    }

                } else {
                    // Handle unsuccessful login
                    Toast.makeText(SignUpActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("DEBUF", "onError: " + t.getMessage());
                // Handle failure
                Toast.makeText(SignUpActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class SignupRequest {
        @SerializedName("fname")
        private String fname;
        @SerializedName("lname")
        private String lname;
        @SerializedName("email")
        private String email;
        @SerializedName("password")
        private String password;

        @SerializedName("role")
        private String role;

        public SignupRequest(String firstName,String lastName, String email,
                             String password, String role) {
            this.fname= firstName;
            this.lname = lastName;
            this.email = email;
            this.password = password;
            this.role = role;
        }
    }

    public interface ApiService {
        @POST("signup")
        Call<User> signup(@Body SignUpActivity.SignupRequest signupRequest);
    }
}