package com.example.tourguidemegaphone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

                // Proceed to TourSessionDetailsActivity
//                Intent intent = new Intent(PaymentProcessActivity.this, TourSessionDetailsActivity.class);
//                startActivity(intent);
            }
        });
    }
}