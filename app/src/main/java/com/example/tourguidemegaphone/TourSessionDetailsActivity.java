package com.example.tourguidemegaphone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class TourSessionDetailsActivity extends AppCompatActivity {
    private TextView textViewTitle, textViewDescription,
            textViewStartDateTime, textViewPrice,
            textViewDuration, textViewBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tour_session_details);
        Intent intent = getIntent();
        TourModel tourData = (TourModel) intent.getSerializableExtra("tourData");

//        textViewTitle = findViewById(R.id.editTextTitle);
//        textViewTitle.setText(tourData.getTourTitle());
//
//        textViewDescription = findViewById(R.id.editTextDescription);
//        textViewDescription.setText(tourData.getTourDescription());
//
//        textViewStartDateTime = findViewById(R.id.editTextStartDateTime);
//        textViewStartDateTime.setText(tourData.getTourStartTime());
//
        textViewPrice = findViewById(R.id.tsd_price);
//
//        textViewDuration = findViewById(R.id.editTextDuration);
//        textViewDuration.setText(tourData.getTourDuration());
//
//
        Button buyButton = findViewById(R.id.buy_button);
        buyButton.setOnClickListener(view -> {
            // Perform buy action here

            // Proceed to TourSessionDetailsActivity
            Intent i = new Intent(TourSessionDetailsActivity.this, PaymentProcessActivity.class);
            i.putExtra("price", textViewPrice.getText().toString());
            startActivity(i);
        });
    }
}
