package com.example.frezzing.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.frezzing.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private DatabaseReference userRef;
    private String UID,personVisit;
    private CircleImageView profileImageView;
    private FloatingActionButton fab;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);



        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoriesActivity.this, CartActivity.class));
            }
        });

        title=findViewById(R.id.products_title);
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        UID=FirebaseAuth.getInstance().getUid();
        profileImageView=findViewById(R.id.acountimage);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle !=null){
            personVisit=getIntent().getStringExtra("admin");
            if (personVisit.equals("admin"))
            {
                profileImageView.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                title.setText("Admin Bar");
            }
        }


        else {
            userRef.child(UID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("image")){
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImageView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void logout()
    {
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        mAuth.signOut();
        SharedPreferences preferences =getSharedPreferences("checkbox",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("remember","false");
        editor.apply();
        Intent intent2 = new Intent(CategoriesActivity.this, MainActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent2);
        finish();
    }

    public void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.pop_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pop_menu_Setting:
                startActivity(new Intent(CategoriesActivity.this, SettingActivity.class));
                return true;
            case R.id.pop_menu_LogOut:
                logout();
                return true;
            default:
                return false;
        }
    }

    public void GoToJuiceMenu(View view)
    {
        if (personVisit!=null && personVisit.equals("admin")){
            Intent intent=new Intent(CategoriesActivity.this, ProductsActivity.class);
            intent.putExtra("Type","juice");
            intent.putExtra("person","admin");
            startActivity(intent);
        }
        else {
            Intent intent=new Intent(CategoriesActivity.this,ProductsActivity.class);
            intent.putExtra("Type","juice");
            startActivity(intent);
        }


    }

    public void GoToIcecreameMenu(View view)
    {
        if (personVisit!=null && personVisit.equals("admin")){
            Intent intent=new Intent(CategoriesActivity.this,ProductsActivity.class);
            intent.putExtra("Type","icecream");
            intent.putExtra("person","admin");
            startActivity(intent);
        }
        else {
        Intent intent=new Intent(CategoriesActivity.this,ProductsActivity.class);
        intent.putExtra("Type","icecream");
        startActivity(intent);
        }
    }
}
