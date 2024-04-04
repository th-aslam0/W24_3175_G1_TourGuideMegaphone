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

public class PaymentProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_process);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find views
        EditText editTextCardName = findViewById(R.id.editTextCardName);
        EditText editTextCardNumber = findViewById(R.id.editTextCardNumber);
        EditText editTextCVV = findViewById(R.id.editTextCVV);
        Button buttonProceed = findViewById(R.id.buttonProceed);

        // Set click listener for Proceed button
        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform any validation checks here if needed

                // Proceed to TourSessionDetailsActivity
                Intent intent = new Intent(PaymentProcessActivity.this, TourSessionDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}