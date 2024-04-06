package com.example.tourguidemegaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tourguidemegaphone.databases.LoginDao;
import com.example.tourguidemegaphone.model.User;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity implements LoginDao.LoginResponseCallback{
    private LoginDao loginDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginDao = LoginDao.getInstance(SplashActivity.this);

        User lastLoggedInuser = loginDao.getLastLoginInfo();
        if (lastLoggedInuser.getEmail() != null) {
            Log.d("SPLASHACT", "Login in with stored credentials.");
            loginDao.login(lastLoggedInuser, SplashActivity.this);
        } else {
            Log.d("SPLASHACT", "No previous login data. Going to mainAct.");
            TimerTask timerTask = null;
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    startActivity(
                            new Intent(SplashActivity.this,MainActivity.class));
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask,1000);
        }
    }

    @Override
    public void onLoginResponseReceived(LoginDao.LoginResponse loginResponse) {
        //Check for failure
        TimerTask timerTask = null;
        if (loginResponse == null) {
            Toast.makeText(this, "There was an error in the login process.", Toast.LENGTH_SHORT).show();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    startActivity(
                            new Intent(SplashActivity.this,MainActivity.class));
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask,500);
        } else if (loginResponse.getError() != null) {
            Toast.makeText(this, "Login error: " + loginResponse.getError(), Toast.LENGTH_SHORT).show();
        } else {
            //Successful response
            Log.d("SPLASHACT", "Successful response.");
            Intent intent;
            Timer timer;
            switch (loginResponse.getRole()) {
                case "Tour Guide":
                    Log.d("SPLASHACT", "Login in as Tour Guide...");
                    intent = new Intent(SplashActivity.this, TourGuideHomeActivity.class);
                    timerTask = new TimerTask() {
                        @Override
                        public void run() { startActivity(intent); }
                    };
                    timer = new Timer();
                    timer.schedule(timerTask,200);
                    break;
                case "Tourist":
                    Log.d("SPLASHACT", "Login in as Tourist...");
                    intent = new Intent(SplashActivity.this, TouristHomeActivity.class);
                    timerTask = new TimerTask() {
                        @Override
                        public void run() { startActivity(intent); }
                    };
                    timer = new Timer();
                    timer.schedule(timerTask,200);
            }
        }
    }

    @Override
    public void onLoginResponseError(String error) {
        Toast.makeText(SplashActivity.this, "Login error: " + error, Toast.LENGTH_SHORT).show();
        Log.d("SPLASHACT", "Login error: " + error);
    }

    @Override
    public void onNetworkError(Throwable t) {
        Toast.makeText(SplashActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d("SPLASHACT", "Network error: " + t.getMessage());
    }
}