package com.example.tourguidemegaphone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TourSessionDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tour_session_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        // Finding views by their IDs
//        ImageView imageView = findViewById(R.id.imageView);
//        TextView headingTextView = findViewById(R.id.headingTextView);
//        TextView priceTextView = findViewById(R.id.priceTextView);
//        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
//        TextView smallHeadingTextView = findViewById(R.id.smallHeadingTextView);
//        TextView smallTextView = findViewById(R.id.smallTextView);
        Button buyButton = findViewById(R.id.buy_button);
//        EditText pin1EditText = findViewById(R.id.pin1EditText);
//        EditText pin2EditText = findViewById(R.id.pin2EditText);
//        EditText pin3EditText = findViewById(R.id.pin3EditText);
//        EditText pin4EditText = findViewById(R.id.pin4EditText);

        // Setting up example values
//        imageView.setImageResource(R.drawable.your_image);
//        headingTextView.setText("Product Name");
//        priceTextView.setText("$9.99");
//        descriptionTextView.setText("Description of the product goes here.");
//        smallHeadingTextView.setText("Additional Information");
//        smallTextView.setText("Some additional information about the product.");
        buyButton.setOnClickListener(view -> {
            // Perform buy action here

            // Proceed to TourSessionDetailsActivity
            Intent intent = new Intent(TourSessionDetailsActivity.this, PaymentProcessActivity.class);
            startActivity(intent);
        });
    }
}
