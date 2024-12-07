package com.example.icecreamshopsignin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check login status using UserManager
        String userEmail = UserManager.getInstance(this).getUserEmail();
        if (userEmail.isEmpty()) {
            // User not logged in, redirect to login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setupButtons();
    }

    private void setupButtons() {
        Button makeIceCreamButton = findViewById(R.id.makeIceCreamButton);
        Button exploreMenuButton = findViewById(R.id.exploreMenuButton);
        Button orderHistoryButton = findViewById(R.id.orderHistoryButton);
        Button contactUsButton = findViewById(R.id.contactUsButton);

        // Using lambda expressions for cleaner code
        makeIceCreamButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MakeIceCream.class);
            startActivity(intent);
        });

        exploreMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuListActivity.class);
            startActivity(intent);
        });

        contactUsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SupportCenterActivity.class);
            startActivity(intent);
        });

        orderHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderHistory.class);
            startActivity(intent);
        });
    }
}