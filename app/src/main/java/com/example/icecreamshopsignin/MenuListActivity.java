package com.example.icecreamshopsignin;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class MenuListActivity extends AppCompatActivity implements IceCreamAdapter.OnIceCreamClickListener {
    private RecyclerView recyclerView;
    private IceCreamAdapter adapter;
    private List<IceCream> iceCreamList;
    private DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Check if user is logged in
        String userEmail = UserManager.getInstance(this).getUserEmail();
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setupViews();
        loadIceCreamsFromDatabase();

        // If the database is empty, add initial data
        if (iceCreamList.isEmpty()) {
            addInitialMenuItems();
            loadIceCreamsFromDatabase();
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
    private void setupViews() {
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnCart = findViewById(R.id.btnCart);
        recyclerView = findViewById(R.id.rvIceCreams);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        btnCart.setOnClickListener(v -> startActivity(new Intent(MenuListActivity.this, CartActivity.class)));
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
        String userEmail = UserManager.getInstance(this).getUserEmail();
        int userId = databaseHelper.getUserIdByEmail(userEmail);

        if (userId != -1) {
            long result = databaseHelper.addToCart(userId, iceCream.getId(), 1, "");
            if (result != -1) {
                Toast.makeText(this, iceCream.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
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
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = prefs.getString("email", "");
        return databaseHelper.getUserId(userEmail);
    }
}