package com.example.tourguidemegaphone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RateExperience extends AppCompatActivity {

    private EditText feedbackEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rate_experience);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });

        Button twitterButton = findViewById(R.id.twitterButton);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOnSocialMedia("Twitter");
            }
        });

        Button facebookButton = findViewById(R.id.facebookButton);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOnSocialMedia("Facebook");
            }
        });

        Button instagramButton = findViewById(R.id.instagramButton);
        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOnSocialMedia("Instagram");
            }
        });
    }

    private void submitFeedback() {
        String feedback = feedbackEditText.getText().toString();
        // Handle the submission of feedback
        Toast.makeText(this, "Feedback submitted: " + feedback, Toast.LENGTH_SHORT).show();
    }

    private void shareOnSocialMedia(String platform) {
        // Logic to share on social media
        Toast.makeText(this, "Shared on " + platform, Toast.LENGTH_SHORT).show();
    }
}