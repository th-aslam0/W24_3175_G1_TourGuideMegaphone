package com.example.tourguidemegaphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class TouristHomeActivity extends AppCompatActivity {

    List<TourModel> sessions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_home);
        getSessions();
    }

    private void getSessions() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://tourguidemegaphone-e5d7117dd068.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<TourModel>> call = apiService.getSessions();
        call.enqueue(new Callback<List<TourModel>>() {
            @Override
            public void onResponse(Call<List<TourModel>> call, Response<List<TourModel>> response) {
                if (response.isSuccessful()) {
                    sessions = response.body();
                    Log.d("DEBUF", "onResponse: " + sessions);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.toh_rv);
                    recyclerView.setAdapter(new TouristSessionRVAdapter(TouristHomeActivity.this, sessions, new TouristSessionRVAdapter.OnItemClickListener() {
                        @Override
                        public void viewItemClick(TourModel item, int position) {
                            Intent intent = new Intent(TouristHomeActivity.this, TourSessionDetailsActivity.class);
                            intent.putExtra("tourData", item);
                            startActivity(intent);
                        }
                    }));
                    recyclerView.setLayoutManager(new LinearLayoutManager(TouristHomeActivity.this));
                    // Handle successful login, e.g., save token to SharedPreferences
                } else {
                    // Handle unsuccessful login
                    Toast.makeText(TouristHomeActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TourModel>> call, Throwable t) {
                Log.d("DEBUF", "onError: " + t.getMessage());
                // Handle failure
                Toast.makeText(TouristHomeActivity.this, "Failed to get Sessions: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public interface ApiService {
        @GET("tours")
        Call<List<TourModel>> getSessions();
    }
}