package com.example.icecreamshopsignin;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IceCreamDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ice_cream_detail);
        ImageView btnBack = findViewById(R.id.btnBack);

        // Get data from intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 0.0);
        String description = intent.getStringExtra("description");
        int imageResource = intent.getIntExtra("image_resource", R.drawable.img);


        // Set up views
        ImageView imageView = findViewById(R.id.ivDetailImage);
        TextView nameText = findViewById(R.id.tvDetailName);
        TextView priceText = findViewById(R.id.tvDetailPrice);
        TextView descText = findViewById(R.id.tvDetailDescription);

        imageView.setImageResource(imageResource);
        nameText.setText(name);
        priceText.setText(String.format("$%.2f", price));
        descText.setText(description);

        // Set click listeners
        btnBack.setOnClickListener(v -> onBackPressed());


    }
}