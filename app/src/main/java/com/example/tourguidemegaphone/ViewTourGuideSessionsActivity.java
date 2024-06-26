package com.example.tourguidemegaphone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguidemegaphone.databases.LoginDao;
import com.example.tourguidemegaphone.model.User;
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
import retrofit2.http.POST;
import retrofit2.http.Path;

public class ViewTourGuideSessionsActivity extends AppCompatActivity {
    List<TourModel> sessions = new ArrayList<>();
    private LoginDao loginDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_tour_guide_sessions);
        loginDao = LoginDao.getInstance(ViewTourGuideSessionsActivity.this);
        User user = loginDao.getLastLoginInfo();
        getSessions(user.getEmail());

    }

    private void getSessions(String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourguidemegaphone-e5d7117dd068.herokuapp.com/") // Replace this with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        SessionsRequest sessionsRequest = new SessionsRequest(email);

        Call<List<TourModel>> call = apiService.getSessions(sessionsRequest);
        call.enqueue(new Callback<List<TourModel>>() {
            @Override
            public void onResponse(Call<List<TourModel>> call, Response<List<TourModel>> response) {
                if (response.isSuccessful()) {
                     sessions = response.body();
                    Log.d("DEBUF", "onResponse: " + sessions);
                    RecyclerView recyclerView = (RecyclerView)findViewById(R.id.vtgs_rv);
                    recyclerView.setAdapter(new TourSessionRVAdapter(ViewTourGuideSessionsActivity.this,
                            sessions, new TourSessionRVAdapter.OnItemClickListener() {

                        @Override
                        public void editItemClick(TourModel item, int position) {
                            Toast.makeText(ViewTourGuideSessionsActivity.this, "Edit Clicked", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ViewTourGuideSessionsActivity.this, EditTourAdActivity.class);
                            intent.putExtra("tourData", item); // Put your data here
                            startActivity(intent);
                        }

                        @Override
                        public void deleteItemClick(TourModel item, int position) {
                            deleteSession(item.get_id(), position);
                            Toast.makeText(ViewTourGuideSessionsActivity.this, "Delete Clicked", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void joinItemClick(TourModel item, int position) {
                            Toast.makeText(ViewTourGuideSessionsActivity.this, "Join Clicked", Toast.LENGTH_LONG).show();
                        }

                    }));
                    recyclerView.setLayoutManager(new LinearLayoutManager(ViewTourGuideSessionsActivity.this));
                    // Handle successful login, e.g., save token to SharedPreferences
                } else {
                    // Handle unsuccessful login
                    Toast.makeText(ViewTourGuideSessionsActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TourModel>> call, Throwable t) {
                Log.d("DEBUF", "onError: " + t.getMessage());
                // Handle failure
                Toast.makeText(ViewTourGuideSessionsActivity.this, "Failed to get Sessions: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void deleteSession(String id, int pos) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourguidemegaphone-e5d7117dd068.herokuapp.com/") // Replace this with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);



        Call<DeleteResponse> call = apiService.deleteSession(id);
        call.enqueue(new Callback<DeleteResponse>() {

            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ViewTourGuideSessionsActivity.this, "Tour Deleted Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ViewTourGuideSessionsActivity.this, ViewTourGuideSessionsActivity.class);
                    startActivity(intent);
                } else {
                    // Handle unsuccessful login
                    Toast.makeText(ViewTourGuideSessionsActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Toast.makeText(ViewTourGuideSessionsActivity.this, "Delete failed" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public class SessionsRequest {

        @SerializedName("email")
        private String email;
        public SessionsRequest(String email) {
            this.email = email;
        }
    }

    public class SessionsResponse {
        @SerializedName("sessions")
        List<ViewTourGuideSessionsActivity.SessionsResponse> sessions;

        public List<ViewTourGuideSessionsActivity.SessionsResponse> getSessionsList() {
            return sessions;
        }
    }
    public class DeleteResponse {
        @SerializedName("message")
        String message;

        public String getMessage() {
            return message;
        }
    }
    public interface ApiService {
        @POST("tours_by_email")
        Call<List<TourModel>> getSessions(@Body SessionsRequest sessionRequest);

        @DELETE("tours/{id}")
        Call<DeleteResponse> deleteSession(@Path("id") String id);
    }
}
