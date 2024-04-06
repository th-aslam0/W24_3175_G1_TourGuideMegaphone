package com.example.tourguidemegaphone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class EditTourAdActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextStartDateTime, editTextPrice, editTextDuration, editTextBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_tour_ad);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize EditText fields
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextStartDateTime = findViewById(R.id.editTextStartDateTime);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDuration = findViewById(R.id.editTextDuration);
        editTextBanner = findViewById(R.id.editTextBanner);

        Button updateButton = findViewById(R.id.buttonUpdate);

        // Set click listener for Proceed button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve text from EditText fields
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                String startDateTime = editTextStartDateTime.getText().toString();
                String price = editTextPrice.getText().toString();
                String duration = editTextDuration.getText().toString();
                String banner = editTextBanner.getText().toString();

                update(title, description, startDateTime, price, duration,banner);

                // Proceed to TourSessionDetailsActivity
//                Intent intent = new Intent(PaymentProcessActivity.this, TourSessionDetailsActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void update(String title, String description, String startDateTime,
                        String price, String duration,String banner) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourguidemegaphone-e5d7117dd068.herokuapp.com/") // Replace this with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EditTourAdActivity.ApiService apiService = retrofit.create(EditTourAdActivity.ApiService.class);

        EditTourAdActivity.UpdateRequest updateRequest = new EditTourAdActivity.UpdateRequest(title, description, startDateTime, price, duration,banner);

        Call<EditTourAdActivity.UpdateResponse> call = apiService.signup(updateRequest);

        call.enqueue(new Callback<EditTourAdActivity.UpdateResponse>() {
            @Override
            public void onResponse(Call<EditTourAdActivity.UpdateResponse> call, Response<EditTourAdActivity.UpdateResponse> response) {
                if (response.isSuccessful()) {
                    EditTourAdActivity.UpdateResponse signUpResponse = response.body();
                    String token = signUpResponse.getToken();
                    Log.d("DEBUF", "onResponse: " + signUpResponse);

                    Intent intent = new Intent(EditTourAdActivity.this, TourGuideHomeActivity.class);
                    startActivity(intent);
                    // Handle successful login, e.g., save token to SharedPreferences
                } else {
                    // Handle unsuccessful login
                    Toast.makeText(EditTourAdActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EditTourAdActivity.UpdateResponse> call, Throwable t) {
                Log.d("DEBUF", "onError: " + t.getMessage());
                // Handle failure
                Toast.makeText(EditTourAdActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class UpdateRequest {
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("startDateTime")
        private String startDateTime;
        @SerializedName("price")
        private String price;

        @SerializedName("duration")
        private String duration;

        @SerializedName("banner")
        private String banner;

        public UpdateRequest(String title, String description, String startDateTime,
                             String price, String duration,String banner) {
            this.title= title;
            this.description = description;
            this.startDateTime = startDateTime;
            this.price = price;
            this.duration = duration;
            this.banner = banner;
        }
    }

    public class UpdateResponse {
        @SerializedName("token")
        private String message;

        public String getToken() {
            return message;
        }
    }
    public interface ApiService {
        @POST("update")
        Call<EditTourAdActivity.UpdateResponse> signup(@Body EditTourAdActivity.UpdateRequest updateRequest);
    }
}