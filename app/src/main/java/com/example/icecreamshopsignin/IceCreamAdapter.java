package com.example.icecreamshopsignin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icecreamshopsignin.Modules.IceCream;

import java.util.List;

public class IceCreamAdapter extends RecyclerView.Adapter<IceCreamAdapter.IceCreamViewHolder> {
    private Context context;
    private List<IceCream> iceCreamList;
    private OnIceCreamClickListener listener;

    public interface OnIceCreamClickListener {
        void onAddToCartClick(IceCream iceCream, int position);
        void onViewDetailsClick(IceCream iceCream, int position);
    }

    public IceCreamAdapter(Context context, List<IceCream> iceCreamList, OnIceCreamClickListener listener) {
        this.context = context;
        this.iceCreamList = iceCreamList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IceCreamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ice_cream, parent, false);
        return new IceCreamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IceCreamViewHolder holder, int position) {
        IceCream iceCream = iceCreamList.get(position);

        // Set ice cream image using resource ID
        holder.ivIceCreamImage.setImageResource(iceCream.getImageResourceId());
        holder.tvIceCreamName.setText(iceCream.getName());
        holder.tvIceCreamPrice.setText(String.format("$%.2f", iceCream.getPrice()));

        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClick(iceCream, position);
            }
        });

        holder.btnViewDetails.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewDetailsClick(iceCream, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iceCreamList.size();
    }

    public static class IceCreamViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIceCreamImage;
        TextView tvIceCreamName, tvIceCreamPrice;
        Button btnAddToCart, btnViewDetails;

        public IceCreamViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIceCreamImage = itemView.findViewById(R.id.ivIceCreamImage);
            tvIceCreamName = itemView.findViewById(R.id.tvIceCreamName);
            tvIceCreamPrice = itemView.findViewById(R.id.tvIceCreamPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}