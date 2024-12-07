// OrderDetailsBottomSheet.java
package com.example.icecreamshopsignin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icecreamshopsignin.Modules.Order;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderDetailsBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_ORDER = "order";
    private Order order;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());

    public static OrderDetailsBottomSheet newInstance(Order order) {
        OrderDetailsBottomSheet fragment = new OrderDetailsBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order = (Order) getArguments().getSerializable(ARG_ORDER);
        }
        if (order == null) {
            dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_order_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
    }

    private void initializeViews(View view) {
        TextView orderIdText = view.findViewById(R.id.order_id);
        TextView dateText = view.findViewById(R.id.order_date);
        TextView statusText = view.findViewById(R.id.order_status);
        TextView totalText = view.findViewById(R.id.order_total);
        RecyclerView itemsRecyclerView = view.findViewById(R.id.items_recycler_view);

        orderIdText.setText(String.format(Locale.getDefault(), "Order #%d", order.getId()));
        setFormattedDate(dateText);
        statusText.setText(order.getStatus());
        totalText.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalPrice()));

        // Setup recycler view for order items
        OrderItemsAdapter adapter = new OrderItemsAdapter(order.getItems());
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        itemsRecyclerView.setAdapter(adapter);
    }

    private void setFormattedDate(TextView dateText) {
        try {
            Date date = inputFormat.parse(order.getDate());
            if (date != null) {
                dateText.setText(outputFormat.format(date));
            }
        } catch (ParseException e) {
            dateText.setText(order.getDate());
        }
    }
}