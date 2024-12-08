package com.example.icecreamshopsignin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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

        // Initialize drawer components
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        // Set up menu button
        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Set up the hamburger menu icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, null, 
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set user email in navigation header
        View headerView = navigationView.getHeaderView(0);
        TextView userEmailView = headerView.findViewById(R.id.userEmail);
        String userEmail = UserManager.getInstance(this).getUserEmail();
        userEmailView.setText(userEmail);

        // Check login status
        if (userEmail.isEmpty()) {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_sign_out) {
            UserManager.getInstance(this).clearUserData();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}