// OrderHistory.java
package com.example.icecreamshopsignin;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.icecreamshopsignin.Modules.Order;
import com.example.icecreamshopsignin.Modules.OrderItem;
import java.util.ArrayList;
import java.util.List;

public class OrderHistory extends AppCompatActivity implements OrderHistoryAdapter.OrderClickListener {
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<Order> orderList;
    private DatabaseHelper databaseHelper;
    private String userEmail;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Get logged in user email
        userEmail = UserManager.getInstance(this).getUserEmail();
        if (userEmail == null || userEmail.isEmpty()) {
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
        loadOrderHistory();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.order_history_recycler_view);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        orderList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(orderList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadOrderHistory() {
        orderList.clear();
        Cursor orderCursor = null;
        try {
            orderCursor = databaseHelper.getUserOrders(userId);
            if (orderCursor != null && orderCursor.getCount() > 0) {
                while (orderCursor.moveToNext()) {
                    Order order = extractOrderFromCursor(orderCursor);
                    List<OrderItem> items = loadOrderItems(order.getId());
                    order.setItems(items);
                    orderList.add(order);
                }
            }
        } finally {
            if (orderCursor != null) {
                orderCursor.close();
            }
        }
        updateEmptyState();
        adapter.notifyDataSetChanged();
    }

    private Order extractOrderFromCursor(Cursor cursor) {
        return new Order(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                userId,
                cursor.getString(cursor.getColumnIndexOrThrow("date")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("total_price")),
                cursor.getString(cursor.getColumnIndexOrThrow("status"))
        );
    }

    private List<OrderItem> loadOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        Cursor itemsCursor = null;
        try {
            itemsCursor = databaseHelper.getOrderItems(orderId);
            if (itemsCursor != null && itemsCursor.getCount() > 0) {
                while (itemsCursor.moveToNext()) {
                    items.add(extractOrderItemFromCursor(itemsCursor));
                }
            }
        } finally {
            if (itemsCursor != null) {
                itemsCursor.close();
            }
        }
        return items;
    }

    private OrderItem extractOrderItemFromCursor(Cursor cursor) {
        return new OrderItem(
                cursor.getInt(cursor.getColumnIndexOrThrow("menu_item_id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                cursor.getString(cursor.getColumnIndexOrThrow("customizations"))
        );
    }

    private void updateEmptyState() {
        View emptyView = findViewById(R.id.empty_state);
        if (orderList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onOrderClick(Order order) {
        OrderDetailsBottomSheet bottomSheet = OrderDetailsBottomSheet.newInstance(order);
        bottomSheet.show(getSupportFragmentManager(), "OrderDetails");
    }
}