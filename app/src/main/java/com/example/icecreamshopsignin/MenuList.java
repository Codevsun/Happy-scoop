package com.example.icecreamshopsignin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icecreamshopsignin.Modules.IceCream;

import java.util.ArrayList;
import java.util.List;

public class MenuList extends AppCompatActivity implements IceCreamAdapter.OnIceCreamClickListener {
    private RecyclerView recyclerView;
    private IceCreamAdapter adapter;
    private List<IceCream> iceCreamList;
    private DatabaseHelper databaseHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list);
        ImageView img = findViewById(R.id.btnBack);
        img.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rvIceCreams);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load ice creams from database
        loadIceCreamsFromDatabase();

        // If the database is empty, add some initial data
        if (iceCreamList.isEmpty()) {
            addInitialMenuItems();
            loadIceCreamsFromDatabase(); // Reload after adding items
        }
    }

    private void loadIceCreamsFromDatabase() {
        iceCreamList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllMenuItems();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int imageResourceId = getImageResourceForFlavor(name);

                IceCream iceCream = new IceCream(name, price, description, imageResourceId);
                iceCream.setId(id);
                iceCreamList.add(iceCream);
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new IceCreamAdapter(this, iceCreamList, this);
        recyclerView.setAdapter(adapter);
    }

    private void addInitialMenuItems() {
        databaseHelper.addMenuItem("Vanilla Ice Cream", 3.99, "Classic vanilla flavor", null);
        databaseHelper.addMenuItem("Chocolate Ice Cream", 4.49, "Rich chocolate flavor", null);
        databaseHelper.addMenuItem("Strawberry Ice Cream", 4.99, "Fresh strawberry flavor", null);
    }

    private int getImageResourceForFlavor(String flavorName) {
        // Map flavor names to drawable resources
        switch (flavorName.toLowerCase()) {
            case "vanilla ice cream":
                return R.drawable.img;
            case "chocolate ice cream":
                return R.drawable.img_1;
            case "strawberry ice cream":
                return R.drawable.img_3;
            default:
                return R.drawable.img; // Default image
        }
    }

    @Override
    public void onAddToCartClick(IceCream iceCream, int position) {
        // Get current user ID from SharedPreferences or your session management
        int userId = getCurrentUserId();

        if (userId != -1) {
            long result = databaseHelper.addToCart(userId, iceCream.getId(), 1, ""); // Default quantity 1, no customizations
            if (result != -1) {
                Toast.makeText(this, iceCream.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            // Optionally redirect to login screen
            // startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onViewDetailsClick(IceCream iceCream, int position) {
        Intent intent = new Intent(this, IceCreamDetailActivity.class);
        intent.putExtra("ice_cream_id", iceCream.getId());
        intent.putExtra("name", iceCream.getName());
        intent.putExtra("price", iceCream.getPrice());
        intent.putExtra("description", iceCream.getDescription());
        intent.putExtra("image_resource", iceCream.getImageResourceId());
        startActivity(intent);
    }


    private int getCurrentUserId() {
        // Implement your user session management here
        // For example, using SharedPreferences:
        return getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getInt("current_user_id", -1);
    }
}