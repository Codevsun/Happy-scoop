package com.example.icecreamshopsignin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class IceCreamDetailActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private int iceCreamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ice_cream_detail);
        
        databaseHelper = new DatabaseHelper(this);
        ImageView btnBack = findViewById(R.id.btnBack);
        MaterialButton btnAddToCart = findViewById(R.id.btnAddToCartDetail);

        // Get data from intent
        Intent intent = getIntent();
        iceCreamId = intent.getIntExtra("ice_cream_id", -1);
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 0.0);
        String description = intent.getStringExtra("description");
        int imageResource = intent.getIntExtra("image_resource", R.drawable.blue);

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
        
        btnAddToCart.setOnClickListener(v -> addToCart());
    }

    private void addToCart() {
        String userEmail = UserManager.getInstance(this).getUserEmail();
        int userId = databaseHelper.getUserIdByEmail(userEmail);

        if (userId != -1 && iceCreamId != -1) {
            long result = databaseHelper.addToCart(userId, iceCreamId, 1, "");
            if (result != -1) {
                Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}