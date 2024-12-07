// OrderItemsAdapter.java
package com.example.icecreamshopsignin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.icecreamshopsignin.Modules.OrderItem;
import java.util.List;
import java.util.Locale;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder> {
    private final List<OrderItem> items;

    public OrderItemsAdapter(List<OrderItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_detail, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView quantityText;
        private final TextView priceText;
        private final TextView customizationsText;

        OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.item_name);
            quantityText = itemView.findViewById(R.id.item_quantity);
            priceText = itemView.findViewById(R.id.item_price);
            customizationsText = itemView.findViewById(R.id.item_customizations);
        }

        void bind(OrderItem item) {
            nameText.setText(item.getName());
            quantityText.setText(String.format(Locale.getDefault(), "x%d", item.getQuantity()));
            // Calculate total price for the item (price * quantity)
            double itemTotal = item.getPrice() * item.getQuantity();
            priceText.setText(String.format(Locale.getDefault(), "$%.2f", itemTotal));

            // Show customizations if they exist
            if (item.getCustomizations() != null && !item.getCustomizations().isEmpty()) {
                customizationsText.setVisibility(View.VISIBLE);
                customizationsText.setText(item.getCustomizations());
            } else {
                customizationsText.setVisibility(View.GONE);
            }
        }
    }
}