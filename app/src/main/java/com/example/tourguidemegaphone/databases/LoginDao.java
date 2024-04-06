package com.example.tourguidemegaphone.databases;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.tourguidemegaphone.MainActivity;
import com.example.tourguidemegaphone.TourGuideHomeActivity;
import com.example.tourguidemegaphone.TouristHomeActivity;
import com.example.tourguidemegaphone.model.User;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class LoginDao {
    public static LoginDao instance;
    private LoginDbHelper dbHelper;

    public static LoginDao getInstance(Context context) {
        if (instance == null)
            instance = new LoginDao(context);
        return instance;
    }

    private LoginDao() { }

    private LoginDao(Context context) {
        dbHelper = new LoginDbHelper(context);
    }

    public void saveLoginData(String email, String token, String fName, String lName, String role) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LoginContract.LoginEntry.COLUMN_NAME_FNAME, fName);
        values.put(LoginContract.LoginEntry.COLUMN_NAME_LNAME, lName);
        values.put(LoginContract.LoginEntry.COLUMN_NAME_ROLE, role);
        values.put(LoginContract.LoginEntry.COLUMN_NAME_EMAIL, email);
        values.put(LoginContract.LoginEntry.COLUMN_NAME_TOKEN, token);

        // Check if email already exists in the database
        String selection = LoginContract.LoginEntry.COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArgs = { email };
        Cursor cursor = db.query(
                LoginContract.LoginEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            // Email already exists, update the record
            db.update(
                    LoginContract.LoginEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );
        } else {

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(LoginContract.LoginEntry.TABLE_NAME, null, values);
        }
    }

    public User getLastLoginInfo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LoginContract.LoginEntry.COLUMN_NAME_EMAIL,
                LoginContract.LoginEntry.COLUMN_NAME_TOKEN,
                LoginContract.LoginEntry.COLUMN_NAME_FNAME,
                LoginContract.LoginEntry.COLUMN_NAME_LNAME,
                LoginContract.LoginEntry.COLUMN_NAME_ROLE
        };

        // Order by _id DESC to get the last ID
        String sortOrder = LoginContract.LoginEntry._ID + " DESC";

        Cursor cursor = db.query(
                LoginContract.LoginEntry.TABLE_NAME,   // The table to query
                projection,                            // The array of columns to return (pass null to get all)
                null,                                  // The columns for the WHERE clause
                null,                                  // The values for the WHERE clause
                null,                                  // don't group the rows
                null,                                  // don't filter by row groups
                sortOrder,                             // The sort order
                "1"                                    // Limit the output
        );

        String email = null;
        String fName = null;
        String lName = null;
        String role = null;
        String token = null;

        if (cursor.moveToFirst()) {
            email = cursor.getString(
                    cursor.getColumnIndexOrThrow(LoginContract.LoginEntry.COLUMN_NAME_EMAIL));

            fName = cursor.getString(
                    cursor.getColumnIndexOrThrow(LoginContract.LoginEntry.COLUMN_NAME_FNAME));

            lName = cursor.getString(
                    cursor.getColumnIndexOrThrow(LoginContract.LoginEntry.COLUMN_NAME_LNAME));

            role = cursor.getString(
                    cursor.getColumnIndexOrThrow(LoginContract.LoginEntry.COLUMN_NAME_ROLE));

            token = cursor.getString(
                    cursor.getColumnIndexOrThrow(LoginContract.LoginEntry.COLUMN_NAME_TOKEN));
        }
        cursor.close();

        return new User(email, token, fName, lName, role);
    }

    private void saveLoginDataToDb(String email, String token, String fName, String lName, String role) {
        //Warning: We are saving the password in cleartext instead of the token,
        //until the backends is updated and starts returning a token.
        this.saveLoginData(email, token, fName, lName, role);
    }

    public void login(User user, final LoginResponseCallback callback) {
        //private void login(String email, String password)

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourguidemegaphone-e5d7117dd068.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        LoginRequest loginRequest = new LoginRequest(user.getEmail(), user.getToken());

        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("LOGIN", "Successful login:  " + response.isSuccessful());
                    // Handle successful login, e.g., save token to SharedPreferences
                    LoginResponse loginResponse = response.body();
                    callback.onLoginResponseReceived(loginResponse);

                    //To-do: If we have a token stored in the database, use that as login
                    // Here we need to add the session in database
                    saveLoginDataToDb(user.getEmail(),
                            user.getToken(), // Saves password in token field.
                            loginResponse.getFname(),
                            loginResponse.getLname(),
                            loginResponse.getRole());
                } else {
                    // Handle unsuccessful login
                    Log.d("LOGIN", "Unsuccesful login: " + response.code());
                    callback.onLoginResponseError("Login failed with status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t)  {
                // Handle failure
                callback.onNetworkError(t);
            }
        });
    }

    public interface LoginResponseCallback {
        void onLoginResponseReceived(LoginResponse loginResponse);
        void onLoginResponseError(String error);
        void onNetworkError(Throwable t);
    }

    public interface ApiService {
        @POST("login")
        Call<LoginResponse> login(@Body LoginRequest loginRequest);
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
        @SerializedName("_id")
        private String _id;
        @SerializedName("email")
        private String email;
        @SerializedName("password")
        private String password;
        @SerializedName("fname")
        private String fname;
        @SerializedName("lname")
        private String lname;
        @SerializedName("role")
        private String role;
        @SerializedName("__v")
        private String __v;
        @SerializedName("token")
        private String token;
        @SerializedName("error")
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String get__v() {
            return __v;
        }

        public void set__v(String __v) {
            this.__v = __v;
        }

        public void setToken(String token) {
            this.token = token;
        }

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

}

