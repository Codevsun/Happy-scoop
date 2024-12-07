// OrderHistoryAdapter.java
package com.example.icecreamshopsignin;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.icecreamshopsignin.Modules.Order;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    private final List<Order> orders;
    private final OrderClickListener clickListener;

    public interface OrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderHistoryAdapter(List<Order> orders, OrderClickListener clickListener) {
        this.orders = orders;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_history_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView orderIdText;
        private final TextView dateText;
        private final TextView statusText;
        private final TextView totalText;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.order_id);
            dateText = itemView.findViewById(R.id.order_date);
            statusText = itemView.findViewById(R.id.order_status);
            totalText = itemView.findViewById(R.id.order_total);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onOrderClick(orders.get(position));
                }
            });
        }

        void bind(Order order) {
            orderIdText.setText(String.format(Locale.getDefault(), "Order #%d", order.getId()));
            dateText.setText(order.getDate());
            statusText.setText(order.getStatus());
            totalText.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalPrice()));

            // Set background color based on status
            int backgroundColor;
            switch (order.getStatus().toLowerCase()) {
                case "completed":
                    backgroundColor = Color.parseColor("#E8F5E9"); // Light green
                    break;
                case "processing":
                    backgroundColor = Color.parseColor("#E3F2FD"); // Light blue
                    break;
                case "cancelled":
                    backgroundColor = Color.parseColor("#FFEBEE"); // Light red
                    break;
                default:
                    backgroundColor = Color.parseColor("#F5F5F5"); // Light gray
            }

            GradientDrawable statusBackground = (GradientDrawable) statusText.getBackground();
            statusBackground.setColor(backgroundColor);
        }
    }
}