package com.example.tourguidemegaphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourguidemegaphone.databases.QuizDao;
import com.example.tourguidemegaphone.model.Quiz;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.Manifest;

public class QuizActivity extends AppCompatActivity {
    private List<Quiz> quizList;
    private List<String> cityList = new ArrayList<>();
    private QuizDao quizDao;
    private int currentQuestionIndex = 0;
    private int score = 0;
    Button btn_next;
    Spinner spinnerCities;
    RadioGroup rg_options;
    TextView tv_question;
    RadioButton rb_option1;
    RadioButton rb_option2;
    RadioButton rb_option3;
    RadioButton rb_option4;
    String selectedCity;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                getLastLocation();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Permission denied on Location Services.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }
    }

    private void getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Get the city from the location

                            Log.d("QUIZ", "onSuccess, Location: "+ location);
                            selectedCity = getCityFromLocation(location);
                            Log.d("QUIZ", "onSuccess, city: " + selectedCity);
                            Toast.makeText(QuizActivity.this, "You are in " + selectedCity, Toast.LENGTH_SHORT).show();

                            // Load the quiz for the city
                            hideSpinner();
                            loadQuizForCity(selectedCity);
                        } else {
                            // Location is null, handle accordingly
                            setSpinner();
                            spinnerCities.setVisibility(View.VISIBLE);

                        }
                    }
                });
    }

    private void hideSpinner() {
        spinnerCities.setVisibility(View.INVISIBLE);
    }

    private void setSpinner() {
        // Set an OnItemSelectedListener for the Spinner
        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = cityList.get(position);
                loadQuizForCity(selectedCity);
                quizList = quizDao.getQuizForCity(selectedCity);
                displayQuestion(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        spinnerCities.setVisibility(View.VISIBLE);
    }

    private void loadQuizForCity(String city) {

        quizList = quizDao.getQuizForCity(city);

        if (!quizList.isEmpty()) {

            // Display the first question
            displayQuestion(0);
        } else {
            Toast.makeText(this, "No quiz for city: " + city, Toast.LENGTH_SHORT).show();
            // No quiz available for the city, handle accordingly
            setSpinner();
            selectedCity = "Berlin";
            loadQuizForCity(selectedCity);
        }
    }

    private String getCityFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String city = "";

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                city = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return city;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        btn_next = findViewById(R.id.btn_next);
        rg_options = findViewById(R.id.rg_options);
        tv_question = findViewById(R.id.tv_question);
        rb_option1 = findViewById(R.id.rb_option1);
        rb_option2 = findViewById(R.id.rb_option2);
        rb_option3 = findViewById(R.id.rb_option3);
        rb_option4 = findViewById(R.id.rb_option4);

        spinnerCities = findViewById(R.id.spinner_cities);

        quizDao = QuizDao.getInstance(QuizActivity.this);

        // Get the list of cities with available quiz questions from the database
        cityList = quizDao.getCitiesWithQuizzes();

        checkLocationPermission();

//        quizList = quizDao.getQuizForCity("Vancouver, Canada");

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities.setAdapter(adapter);




        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected option from the RadioGroup
                int selectedOption = rg_options.getCheckedRadioButtonId();

                // Check if the selected option is correct
                if (selectedOption != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedOption);
                    int selectedIndex = rg_options.indexOfChild(selectedRadioButton);

                    if (selectedIndex == quizList.get(currentQuestionIndex).getAnswer()) {
                        // Correct answer
                        score++;
                    }
                }

                // Move to the next question
                currentQuestionIndex++;

                // Check if there are more questions
                if (currentQuestionIndex < quizList.size()) {
                    displayQuestion(currentQuestionIndex);
                } else {

                    showFinalScore();
                    resetQuiz();
                }
            }
        });
    }



    private void displayQuestion(int index) {
        Quiz currentQuiz = quizList.get(index);
        tv_question.setText(currentQuiz.getQuestion());
        rb_option1.setText(currentQuiz.getOption1());
        rb_option2.setText(currentQuiz.getOption2());
        rb_option3.setText(currentQuiz.getOption3());
        rb_option4.setText(currentQuiz.getOption4());
        rg_options.clearCheck();
    }

    private void showFinalScore() {
        int totalQuestions = quizList.size();
        double percentage = (double) score / totalQuestions * 100;

        String finalMessage;
        if (percentage >= 70) {
            finalMessage = String.format("Congratulations! You are an expert in %s. Your score is %.0f%%", selectedCity, percentage);
        } else {
            finalMessage = String.format("Keep learning about %s! Your score is %.0f%%", selectedCity, percentage);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz Result")
                .setMessage(finalMessage)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Return to the main activity
                        finish();
                    }
                })
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetQuiz() {
        currentQuestionIndex = 0;
        score = 0;
        // Reset the UI elements
    }
}