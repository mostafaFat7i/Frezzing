package com.example.frezzing.Admins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frezzing.Buyer.CategoriesActivity;
import com.example.frezzing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private ImageView pImage;
    private EditText maintanProductName,maintanProductPrice,maintanProductDescription;
    private String productId;
    private DatabaseReference proRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);
        productId= getIntent().getStringExtra("pid");

        Initialization();
        DisplaySpecificProductInfo();
    }

    private void Initialization() {

        proRef= FirebaseDatabase.getInstance().getReference().child("products").child(productId);
        maintanProductName=findViewById(R.id.maintain_product_item_name);
        maintanProductPrice=findViewById(R.id.maintain_product_item_price);
        maintanProductDescription=findViewById(R.id.maintain_product_item_description);
        pImage=findViewById(R.id.maintain_product_item_image);
    }

    private void DisplaySpecificProductInfo() {

        proRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()){
                    String name=dataSnapshot.child("name").getValue().toString();
                    String description=dataSnapshot.child("description").getValue().toString();
                    String price=dataSnapshot.child("price").getValue().toString();
                    String image=dataSnapshot.child("image").getValue().toString();

                    maintanProductName.setText(name);
                    maintanProductDescription.setText(description);
                    maintanProductPrice.setText(price);
                    Picasso.get().load(image).placeholder(R.drawable.juicee).into(pImage);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void ApplyAdminChanges(View view) {

        String newName=maintanProductName.getText().toString();
        String newPrice=maintanProductPrice.getText().toString();
        String newDescription=maintanProductDescription.getText().toString();

        if (newName.equals("")){
            maintanProductName.setError("Required");
            Toast.makeText(this, "Please,enter product name", Toast.LENGTH_SHORT).show();
        }
        else if (newPrice.equals("")){
            maintanProductPrice.setError("Required");
            Toast.makeText(this, "Please,enter product price", Toast.LENGTH_SHORT).show();
        }
        else if (newDescription.equals("")){
            maintanProductDescription.setError("Required");
            Toast.makeText(this, "Please,enter product description", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String,Object> productMap=new HashMap<>();
            productMap.put("pid",productId);
            productMap.put("description",newDescription);
            productMap.put("price",newPrice);
            productMap.put("name",newName);

            proRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied successfully...", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(AdminMaintainProductsActivity.this, CategoriesActivity.class);
                        intent.putExtra("admin","admin");
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }


    }

    public void DeleteProduct(View view)
    {
        proRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AdminMaintainProductsActivity.this, "The product is deleted successfully...", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AdminMaintainProductsActivity.this, CategoriesActivity.class);
                    intent.putExtra("admin","admin");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}

