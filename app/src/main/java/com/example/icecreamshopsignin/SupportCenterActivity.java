package com.example.icecreamshopsignin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SupportCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus_activity);
        ImageView btnback = findViewById(R.id.btnBack);
        btnback.setOnClickListener(v -> { getOnBackPressedDispatcher().onBackPressed();
        });

        // Initialize buttons
        Button emailButton = findViewById(R.id.email_button);
        Button phoneButton = findViewById(R.id.phone_button);

        // Set email button click listener
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "supportcenter@HappyScoop.sa";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support Inquiry");
                startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        });

        // Set phone button click listener
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "tel:123456789"; // Replace with your phone number
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse(phoneNumber));
                startActivity(phoneIntent);
            }
        });
    }
}
