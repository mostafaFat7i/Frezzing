package com.example.frezzing.Buyer;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frezzing.Model.Cart;
import com.example.frezzing.Holder.CartViewHolder;
import com.example.frezzing.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcess;
    private TextView totalPrice,message1;
    private int overTotalPrice=0;
    private String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Initialization();

        nextProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    private void Initialization() {
        recyclerView = findViewById(R.id.card_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        UID= FirebaseAuth.getInstance().getUid();
        message1=findViewById(R.id.msg1);
        nextProcess = findViewById(R.id.cart_next_process_btn);
        totalPrice = findViewById(R.id.cart_total_price);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        cartListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild("User View")){
                    message1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(UID)
                        .child("Products"), Cart.class).build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final Cart model) {

                        holder.productQuantity.setText("Quantity: "+model.getQuantaty());
                        holder.productName.setText(model.getPname());
                        holder.productPrice.setText("Price: "+model.getPrice()+" $");

                        int oneTypeProductPrice=((Integer.valueOf(model.getPrice())))*Integer.valueOf(model.getQuantaty());
                        overTotalPrice += oneTypeProductPrice;
                        totalPrice.setText("Total Price: "+overTotalPrice+" $");


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence options[]=new CharSequence[]{"Edite","Remove"};
                                AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("cart options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (which==0){
                                            Intent intent=new Intent(CartActivity.this, ProductDetailsActivity.class);
                                            intent.putExtra("pid",model.getPid());
                                            startActivity(intent);
                                        }
                                        if (which==1){
                                            cartListRef.child("User View").child(UID)
                                                    .child("Products").child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){

                                                        Toast.makeText(CartActivity.this, "item removed successfully..", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(CartActivity.this, CategoriesActivity.class));
                                                    }
                                                }
                                            });
                                        }

                                    }
                                });
                                builder.show();
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
