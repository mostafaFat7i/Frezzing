package com.example.frezzing.Admins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frezzing.Holder.AdminOrderViewHolder;
import com.example.frezzing.Model.AdminOrders;
import com.example.frezzing.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CheckNewOrdersActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_new_orders);

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList=findViewById(R.id.orders_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options=new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef,AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder> adapter=
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int position, @NonNull final AdminOrders model)
                    {
                        holder.userName.setText("Name: "+model.getName());
                        holder.userPhone.setText("Phone: "+model.getPhone());
                        holder.userDateTime.setText("Order at: "+model.getDate()+" "+model.getTime());
                        holder.userShippingAddress.setText("Address: "+model.getAddress()+" , "+model.getCity());
                        holder.userTotalPrice.setText("Total Amount: $"+model.getTotalAmount());

                        holder.showOrdersButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String uID=getRef(position).getKey();
                                Intent intent=new Intent(CheckNewOrdersActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[]=new CharSequence[]{"Yes","No"};

                                AlertDialog.Builder alertDialog=new AlertDialog.Builder(CheckNewOrdersActivity.this);
                                alertDialog.setTitle("Have you shipped this order products ?");
                                alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (which==0)
                                        {
                                            String uID=getRef(position).getKey();
                                            RemoveOrder(uID);
                                        }
                                        else {
                                            finish();
                                        }

                                    }
                                });
                                alertDialog.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        AdminOrderViewHolder holder = new AdminOrderViewHolder(view);
                        return holder;
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();

    }
    private void RemoveOrder(String uID) {

        orderRef.child(uID).removeValue();
    }

}

