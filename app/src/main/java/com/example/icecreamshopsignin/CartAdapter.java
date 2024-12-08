package com.example.icecreamshopsignin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icecreamshopsignin.Modules.CartItem;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final List<CartItem> cartItems;
    private final CartItemListener listener;

    public interface CartItemListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemDeleted(CartItem item);
    }

    public CartAdapter(List<CartItem> cartItems, CartItemListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    private static int getImageResourceForFlavor(String flavorName) {
        if (flavorName == null) return R.drawable.blue;
        
        switch (flavorName.toLowerCase()) {
            case "vanilla ice cream":
                return R.drawable.vanilla;
            case "chocolate ice cream":
                return R.drawable.chocolate;
            case "strawberry ice cream":
                return R.drawable.strawberry;
            case "orange ice cream":
                return R.drawable.orange;
            case "rice ice cream":
                return R.drawable.rice;
            case "blackberry ice cream":
                return R.drawable.blue;
            default:
                return R.drawable.blue;
        }
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView nameText;
        TextView priceText;
        TextView quantityText;
        ImageButton deleteButton;
        ImageButton incrementButton;
        ImageButton decrementButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            nameText = itemView.findViewById(R.id.product_name);
            priceText = itemView.findViewById(R.id.product_price);
            quantityText = itemView.findViewById(R.id.quantity_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
            incrementButton = itemView.findViewById(R.id.increment_button);
            decrementButton = itemView.findViewById(R.id.decrement_button);
        }

        void bind(CartItem item, CartItemListener listener) {
            // Set the product image
            productImage.setImageResource(getImageResourceForFlavor(item.getName()));
            
            nameText.setText(item.getName());

            // Show customizations if they exist
            if (item.getCustomizations() != null && !item.getCustomizations().isEmpty()) {
                String customText = item.getName() + "\n" + item.getCustomizations();
                nameText.setText(customText);
            }

            priceText.setText(String.format(Locale.getDefault(), "$%.2f",
                    item.getPrice() * item.getQuantity()));
            quantityText.setText(String.valueOf(item.getQuantity()));

            deleteButton.setOnClickListener(v -> listener.onItemDeleted(item));

            incrementButton.setOnClickListener(v ->
                    listener.onQuantityChanged(item, item.getQuantity() + 1));

            decrementButton.setOnClickListener(v -> {
                if (item.getQuantity() > 1) {
                    listener.onQuantityChanged(item, item.getQuantity() - 1);
                }
            });
        }
    }
}