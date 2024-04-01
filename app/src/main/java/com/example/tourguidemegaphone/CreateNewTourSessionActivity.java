package com.example.tourguidemegaphone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class CreateNewTourSessionActivity extends AppCompatActivity {
    Map<String, Integer> countryCityMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_tour_session);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Sample data for the spinner
//        String[] cities = getResources().getStringArray(R.array.your_string_array);

        countryCityMap.put("Australia", R.array.australia_cities);
        countryCityMap.put("Canada", R.array.canada_cities);
        countryCityMap.put("South Africa", R.array.south_africa_cities);
        countryCityMap.put("United Kingdom", R.array.united_kingdom_cities);
        countryCityMap.put("United States", R.array.united_states_cities);

        EditText etTitle = findViewById(R.id.cnts_et_title);
        EditText etDescription = findViewById(R.id.cnts_et_description);
        EditText etStartTime = findViewById(R.id.cnts_et_time);
        EditText etDuration = findViewById(R.id.cnts_et_duration);
        EditText etPrice = findViewById(R.id.cnts_et_price);
        Button publishBtn = findViewById(R.id.cnts_btn_publish);
        String countrySelected = "";
        String citySelected = "";

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countrySelected.equals("Select Country") || citySelected.equals("Select City")){
                    return;
                }
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String startTime = etStartTime.getText().toString();
                String duration = etDuration.getText().toString();
                String price = etPrice.getText().toString();
                publishTourSession
                        (countrySelected, citySelected, title, description, startTime,
                        duration, Double.parseDouble(price));
            }
        });
    

        Spinner countriesSpinner = findViewById(R.id.cnts_spinner_country);
        Spinner citiesSpinner = findViewById(R.id.cnts_spinner_city);
        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the spinner
                String selectedItem = (String) parentView.getItemAtPosition(position);
                int resourceId = countryCityMap.get(selectedItem);

                // Now you can use the resource ID to access the string array
                String[] citiesSelectedCountry = getResources().getStringArray(resourceId);

                // Create an ArrayAdapter using the sample data
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateNewTourSessionActivity.this,
                        android.R.layout.simple_spinner_item, citiesSelectedCountry);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                citiesSpinner.setAdapter(adapter);
                // Display a toast message with the selected item
                Toast.makeText(getApplicationContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the spinner
                String selectedItem = (String) parentView.getItemAtPosition(position);
                // Display a toast message with the selected item
                Toast.makeText(getApplicationContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

    }

    private void publishTourSession(String country, String city, String tourTitle, String tourDescription, String tourStartDateTime, String tourDuration, Double price) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourguidemegaphone-e5d7117dd068.herokuapp.com/") // Replace this with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CreateNewTourSessionActivity.ApiService apiService = retrofit.create(CreateNewTourSessionActivity.ApiService.class);

        CreateNewTourSessionActivity.PublishRequest publishRequest = new CreateNewTourSessionActivity.
                PublishRequest(country, city, tourTitle, tourDescription, tourStartDateTime, tourDuration, price);

        Call<CreateNewTourSessionActivity.PublishResponse> call = apiService.publish(publishRequest);
        call.enqueue(new Callback<CreateNewTourSessionActivity.PublishResponse>() {
            @Override
            public void onResponse(Call<CreateNewTourSessionActivity.PublishResponse> call, Response<CreateNewTourSessionActivity.PublishResponse> response) {
                if (response.isSuccessful()) {
                    CreateNewTourSessionActivity.PublishResponse publishResponse = response.body();
                    String token = publishResponse.getToken();
                    Log.d("DEBUF", "onResponse: " + publishResponse);

                    Toast.makeText(CreateNewTourSessionActivity.this, "Session Published", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CreateNewTourSessionActivity.this, TourGuideHomeActivity.class);
                    startActivity(intent);

                    // Handle successful login, e.g., save token to SharedPreferences
                } else {
                    // Handle unsuccessful login
                    Toast.makeText(CreateNewTourSessionActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateNewTourSessionActivity.PublishResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(CreateNewTourSessionActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class PublishRequest {
        @SerializedName("country")
        private String country;
        @SerializedName("city")
        private String city;
        @SerializedName("tourTitle")
        private String tourTitle;
        @SerializedName("tourDescription")
        private String tourDescription;
        @SerializedName("tourStartDateTime")
        private String tourStartDateTime;
        @SerializedName("tourDuration")
        private String tourDuration;
        @SerializedName("price")
        private Double price;

        public PublishRequest(String country, String city, String tourTitle, String tourDescription, String tourStartDateTime, String tourDuration, Double price) {
            this.country = country;
            this.city = city;
            this.tourTitle = tourTitle;
            this.tourDescription = tourDescription;
            this.tourStartDateTime = tourStartDateTime;
            this.tourDuration = tourDuration;
            this.price = price;
        }
    }

    public class PublishResponse {
        @SerializedName("token")
        private String message;

        public String getToken() {
            return message;
        }
    }
    public interface ApiService {
        @POST("tours")
        Call<CreateNewTourSessionActivity.PublishResponse> publish(@Body CreateNewTourSessionActivity.PublishRequest publishRequest);
    }
}