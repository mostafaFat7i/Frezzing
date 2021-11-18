package com.example.frezzing.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.frezzing.Admins.AdminMaintainProductsActivity;
import com.example.frezzing.Holder.ProductViewHolder;
import com.example.frezzing.Model.Products;
import com.example.frezzing.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private DatabaseReference ProductRef,userRef;
    private RecyclerView recyclerView;
    CircleImageView userImageView;
    private TextView productTitle;
    RecyclerView.LayoutManager layoutManager;
    private String type="",UID,person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Initialization();

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle !=null){
            type=getIntent().getStringExtra("Type");
            person=getIntent().getStringExtra("person");
        }

        if (type.equals("juice")){

            productTitle.setText("Juices");
        }
        else if (type.equals("icecream")){

            productTitle.setText("Ice Cream");
        }



    }

    private void Initialization() {
        ProductRef= FirebaseDatabase.getInstance().getReference().child("products");
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");
        UID=FirebaseAuth.getInstance().getUid();
        userImageView=findViewById(R.id.acountimage);
        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        productTitle=findViewById(R.id.products_title);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (person!=null &&person.equals("admin")){
            userImageView.setVisibility(View.GONE);
        }
        else {
            userRef.child(UID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists()){
                        String UserImage=dataSnapshot.child("image").getValue().toString();

                        Picasso.get().load(UserImage).placeholder(R.drawable.profile).into(userImageView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductRef.orderByChild("category").equalTo(type), Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.productItemName.setText(model.getName());
                        holder.productItemDescription.setText(model.getDescription());
                        holder.productItemPrice.setText("Price: " + model.getPrice() + " $");
                        Picasso.get().load(model.getImage()).into(holder.productItemImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (person !=null && person.equals("admin")){
                                    Intent intent = new Intent(ProductsActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else {
                                    Intent intent = new Intent(ProductsActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public void showMenu(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.pop_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pop_menu_Setting:
                startActivity(new Intent(ProductsActivity.this,SettingActivity.class));
                return true;
            case R.id.pop_menu_LogOut:
                logout();
                return true;
            default:
                return false;
        }
    }
    public void logout()
    {
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent2 = new Intent(ProductsActivity.this, MainActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent2);
        finish();
    }

    public void GoToCategoryActiity(View view) {
        if (person!=null &&person.equals("admin")){
            Intent intent=new Intent(ProductsActivity.this, CategoriesActivity.class);
            intent.putExtra("admin","admin");
            startActivity(intent);
            finish();
        }
        else {
            startActivity(new Intent(ProductsActivity.this,CategoriesActivity.class));
            finish();
        }


    }
}
