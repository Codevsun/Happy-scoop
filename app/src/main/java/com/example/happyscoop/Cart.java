package com.example.happyscoop;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.icecreamshopsignin.DatabaseHelper;

public class Cart extends AppCompatActivity {

    private TextView productName, productPrice, totalPrice, quantityText;
    private ImageView productImage;
    private ImageButton backButton, deleteButton, incrementButton, decrementButton;
    private Button checkoutButton;
    private int quantity = 1; // Default quantity
    private double pricePerItem = 0.0; // Price of the item (initially set to 0.0)
    private double total = pricePerItem; // Initial total price

    private DatabaseHelper databaseHelper;
    private int menuItemId = 1; // Example item id. You may get this dynamically, based on selected item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity); // Ensure this matches your XML file name

        // Initialize UI components
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        totalPrice = findViewById(R.id.total_price);
        quantityText = findViewById(R.id.quantity_text);
        productImage = findViewById(R.id.product_image);
        backButton = findViewById(R.id.back);
        deleteButton = findViewById(R.id.delete_button);
        incrementButton = findViewById(R.id.imageButton4);
        decrementButton = findViewById(R.id.imageButton5);
        checkoutButton = findViewById(R.id.checkout_button);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        // Fetch item details from the database based on the item ID
        loadProductDetails(menuItemId);

        // Back button click listener
        backButton.setOnClickListener(v -> {
            // Go back to the previous activity
            finish();
        });

        // Increment button click listener
        incrementButton.setOnClickListener(v -> {
            quantity++;
            updateQuantityAndTotal();
        });

        // Decrement button click listener
        decrementButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityAndTotal();
            } else {
                Toast.makeText(this, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete button click listener
        deleteButton.setOnClickListener(v -> {
            // Handle item deletion (e.g., clear from cart and update UI)
            quantity = 0;
            updateQuantityAndTotal();
            Toast.makeText(this, "Item removed from cart", Toast.LENGTH_SHORT).show();
        });

        // Checkout button click listener
        checkoutButton.setOnClickListener(v -> {
            if (quantity > 0) {
                Toast.makeText(this, "Checkout successful! Total: $" + total, Toast.LENGTH_SHORT).show();
                // Clear the cart or navigate to a confirmation screen
                quantity = 0;
                updateQuantityAndTotal();
            } else {
                Toast.makeText(this, "Cart is empty. Please add items to checkout.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch product details from the database
    private void loadProductDetails(int menuItemId) {
        // Query the database to get the product details (name, price, etc.)
        Cursor cursor = databaseHelper.getMenuItemById(menuItemId);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            pricePerItem = cursor.getDouble(cursor.getColumnIndex("price"));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
            // Assuming you have a method to set the image from bytes (if needed)
            // setProductImage(imageBytes);

            productName.setText(name);
            productPrice.setText("$" + pricePerItem);
            totalPrice.setText("Total: $" + pricePerItem); // Initial total

            cursor.close();
        }
    }

    // Update quantity and total price
    private void updateQuantityAndTotal() {
        quantityText.setText(String.valueOf(quantity));
        total = quantity * pricePerItem;
        totalPrice.setText("Total: $" + total);

        // Hide the item UI if quantity is 0
        ConstraintLayout itemDetails = findViewById(R.id.itemDetails);
        CardView items = findViewById(R.id.items);
        if (quantity == 0) {
            itemDetails.setVisibility(View.GONE);
            items.setVisibility(View.GONE);
        } else {
            itemDetails.setVisibility(View.VISIBLE);
            items.setVisibility(View.VISIBLE);
        }
    }
}
