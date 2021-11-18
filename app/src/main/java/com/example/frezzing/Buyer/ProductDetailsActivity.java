package com.example.frezzing.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.frezzing.Model.Products;
import com.example.frezzing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private Button addToCard;
    private TextView productName,productDescription,productPrice;
    private ElegantNumberButton elegantNumberButton;
    private String productId,UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId= getIntent().getStringExtra("pid");
        Initialization();
        GetProductDetails(productId);

        addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AddToCardList();
            }
        });
    }
    private void Initialization() {

        addToCard=findViewById(R.id.details_add_to_card_btn);
        productImage=findViewById(R.id.product_image_details);
        productName=findViewById(R.id.details_product_name);
        productPrice=findViewById(R.id.details_product_price);
        productDescription=findViewById(R.id.details_product_description);
        elegantNumberButton=findViewById(R.id.number_btn);
        UID= FirebaseAuth.getInstance().getUid();

    }
    private void GetProductDetails(String productId) {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("products");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Products products=dataSnapshot.getValue(Products.class);
                productName.setText(products.getName());
                productPrice.setText(products.getPrice());
                productDescription.setText(products.getDescription());

                Picasso.get().load(products.getImage()).placeholder(R.drawable.select_product_image).into(productImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void AddToCardList() {

        String saveCurrentTime,saveCurrentDate;
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        final DatabaseReference cardListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cardMap=new HashMap<>();
        cardMap.put("pid",productId);
        cardMap.put("Pname",productName.getText().toString());
        cardMap.put("price",productPrice.getText().toString());
        cardMap.put("date",saveCurrentDate);
        cardMap.put("time",saveCurrentTime);
        cardMap.put("quantaty",elegantNumberButton.getNumber());


        cardListRef.child("User View").child(UID).child("Products")
                .child(productId).updateChildren(cardMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    cardListRef.child("Admin View").child(UID).child("Products")
                            .child(productId).updateChildren(cardMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ProductDetailsActivity.this, "Added to card list.", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(ProductDetailsActivity.this, CategoriesActivity.class);
                                        startActivity(intent);
                                    }

                                }
                            });
                }
            }
        });


    }
}
