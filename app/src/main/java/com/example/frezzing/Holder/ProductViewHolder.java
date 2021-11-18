package com.example.frezzing.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frezzing.Imterface.ItemClickListener;
import com.example.frezzing.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView productItemName,productItemDescription,productItemPrice;
    public ImageView productItemImage;
    public ItemClickListener itemClickListener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productItemImage=itemView.findViewById(R.id.product_item_image);
        productItemName=itemView.findViewById(R.id.product_item_name);
        productItemDescription=itemView.findViewById(R.id.product_item_description);
        productItemPrice=itemView.findViewById(R.id.product_item_price);

    }

    public void setItemClickListener(ItemClickListener listener){
        this.itemClickListener=listener;
    }


    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);
    }


}
