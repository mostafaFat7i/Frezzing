package com.example.frezzing.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frezzing.Imterface.ItemClickListener;
import com.example.frezzing.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName,productPrice,productQuantity;
    public ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        productName=itemView.findViewById(R.id.cart_item_product_name);
        productPrice=itemView.findViewById(R.id.cart_item_product_price);
        productQuantity=itemView.findViewById(R.id.cart_item_product_quantity);

    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }
}
