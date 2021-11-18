package com.example.frezzing.Holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frezzing.R;

public class AdminOrderViewHolder extends RecyclerView.ViewHolder {

    public TextView userName,userPhone,userTotalPrice,userDateTime,userShippingAddress;
    public Button showOrdersButton;

    public AdminOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        userName=itemView.findViewById(R.id.order_user_name);
        userPhone=itemView.findViewById(R.id.order_phone);
        userDateTime=itemView.findViewById(R.id.order_date_time);
        userTotalPrice=itemView.findViewById(R.id.order_total_price);
        userShippingAddress=itemView.findViewById(R.id.order_address);
        showOrdersButton=itemView.findViewById(R.id.order_show_all_products_btn);

    }
}
