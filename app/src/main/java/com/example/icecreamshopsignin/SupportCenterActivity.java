package com.example.icecreamshopsignin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
        Button locationButton = findViewById(R.id.location_button);

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

        // Set location button click listener
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Uri with your ice cream shop's location
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=24.7136,46.6753(Happy+Scoop+Ice+Cream)");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                
                // Try to open in Google Maps first
                mapIntent.setPackage("com.google.android.apps.maps");
                
                // Check if Google Maps is installed
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    // If Google Maps is not installed, try to open in any available map app
                    mapIntent.setPackage(null);
                    try {
                        startActivity(mapIntent);
                    } catch (Error e) {
                        e.toString();
                    }
                }
            }
        });
    }
}
