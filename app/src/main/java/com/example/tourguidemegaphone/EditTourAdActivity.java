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
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class EditTourAdActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextStartDateTime, editTextPrice, editTextDuration, editTextBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_tour_ad);
        Intent intent = getIntent();
        TourModel tourData = (TourModel) intent.getSerializableExtra("tourData");

        // Initialize EditText fields
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextTitle.setText(tourData.getTourTitle());

        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDescription.setText(tourData.getTourDescription());

        editTextStartDateTime = findViewById(R.id.editTextStartDateTime);
        editTextStartDateTime.setText(tourData.getTourStartTime());

        editTextPrice = findViewById(R.id.editTextPrice);
        editTextPrice.setText(Double.toString(tourData.getTourPrice()));

        editTextDuration = findViewById(R.id.editTextDuration);
        editTextDuration.setText(tourData.getTourDuration());


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

                update(tourData._id, title, description, startDateTime, price, duration);

                // Proceed to TourSessionDetailsActivity
//                Intent intent = new Intent(PaymentProcessActivity.this, TourSessionDetailsActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void update(String _id,String title, String description, String startDateTime,
                        String price, String duration) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourguidemegaphone-e5d7117dd068.herokuapp.com/") // Replace this with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        UpdateRequest updateRequest = new UpdateRequest(title, description, startDateTime, price, duration);

        Call<UpdateResponse> call = apiService.update(_id, updateRequest);

        call.enqueue(new Callback<EditTourAdActivity.UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                if (response.isSuccessful()) {
                    UpdateResponse signUpResponse = response.body();
                    TourModel updatedTour = signUpResponse.updatedTour();
                    Log.d("DEBUF", "onResponse: " + updatedTour);
                    Toast.makeText(EditTourAdActivity.this, "Tour Updated", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditTourAdActivity.this, ViewTourGuideSessionsActivity.class);
                    startActivity(intent);
                } else {
                    // Handle unsuccessful login
                    Toast.makeText(EditTourAdActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
                Log.d("DEBUF", "onError: " + t.getMessage());
                // Handle failure
                Toast.makeText(EditTourAdActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class UpdateRequest {
        @SerializedName("tourTitle")
        private String tourTitle;
        @SerializedName("tourDescription")
        private String tourDescription;
        @SerializedName("tourStartTime")
        private String tourStartTime;
        @SerializedName("tourPrice")
        private String tourPrice;

        @SerializedName("tourDuration")
        private String tourDuration;

        public UpdateRequest(String title, String description, String startDateTime,
                             String price, String duration) {
            this.tourTitle= title;
            this.tourDescription = description;
            this.tourStartTime = startDateTime;
            this.tourPrice = price;
            this.tourDuration = duration;
        }
    }

    public class UpdateResponse {
        @SerializedName("updatedTour")
        private TourModel updatedTour;

        public TourModel updatedTour() {
            return updatedTour;
        }
    }
    public interface ApiService {
        @PUT("tours/{id}")
        Call<UpdateResponse> update(@Path("id") String id, @Body UpdateRequest updateRequest);
    }
}