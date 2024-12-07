package com.example.icecreamshopsignin;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icecreamshopsignin.Modules.CartItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartItemListener {
    private TextView totalPriceText;
    private Button checkoutButton;
    private ImageButton backButton;
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private DatabaseHelper databaseHelper;
    private String userEmail;
    private int userId;
    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        userEmail = UserManager.getInstance(this).getUserEmail();
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseHelper = new DatabaseHelper(this);
        userId = databaseHelper.getUserIdByEmail(userEmail);
        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        loadCartItems();
    }

    private void initializeViews() {
        totalPriceText = findViewById(R.id.total_price);
        checkoutButton = findViewById(R.id.checkout_button);
        backButton = findViewById(R.id.back);
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
    }

    private void setupRecyclerView() {
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItems, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        checkoutButton.setOnClickListener(v -> processCheckout());
    }

    private void loadCartItems() {
        cartItems.clear();
        try (Cursor cursor = databaseHelper.getCartItems(userId)) {
            while (cursor != null && cursor.moveToNext()) {
                CartItem item = new CartItem(
                        userId,
                        cursor.getInt(cursor.getColumnIndexOrThrow("menu_item_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("customizations"))
                );
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                item.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                cartItems.add(item);
            }
        }

        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
        updateEmptyState();
    }

    private void updateTotalPrice() {
        totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        totalPriceText.setText(String.format(Locale.getDefault(), "Total: $%.2f", totalPrice));
    }

    private void updateEmptyState() {
        if (cartItems.isEmpty()) {
            findViewById(R.id.empty_cart_view).setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            checkoutButton.setEnabled(false);
        } else {
            findViewById(R.id.empty_cart_view).setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(true);
        }
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        if (newQuantity > 0) {
            databaseHelper.updateCartItemQuantity(userId, item.getMenuItemId(), newQuantity);
            item.setQuantity(newQuantity);
        } else {
            databaseHelper.removeCartItem(userId, item.getMenuItemId());
            cartItems.remove(item);
        }
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
        updateEmptyState();
    }

    @Override
    public void onItemDeleted(CartItem item) {
        databaseHelper.removeCartItem(userId, item.getMenuItemId());
        cartItems.remove(item);
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
        updateEmptyState();
    }

    private void processCheckout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        long orderId = databaseHelper.createOrder(userId, currentDate, totalPrice, "Pending");
        if (orderId != -1) {
            for (CartItem item : cartItems) {
                databaseHelper.addOrderItem((int)orderId, item.getMenuItemId(),
                        item.getQuantity(), item.getCustomizations());
            }
            databaseHelper.clearCart(userId);
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
        }
    }
}