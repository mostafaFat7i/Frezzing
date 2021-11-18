package com.example.frezzing.Admins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.frezzing.Buyer.CategoriesActivity;
import com.example.frezzing.Buyer.MainActivity;
import com.example.frezzing.R;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }


    public void GoToMaintainProducts(View view)
    {
        Intent intent=new Intent(AdminHomeActivity.this, CategoriesActivity.class);
        intent.putExtra("admin","admin");
        startActivity(intent);
    }

    public void GoToAdminNewOrders(View view)
    {
        startActivity(new Intent(AdminHomeActivity.this, CheckNewOrdersActivity.class));

    }

    public void GoToAddNewProducts(View view)
    {
        startActivity(new Intent(AdminHomeActivity.this, AdminAddNewProductActivity.class));
    }
    public void Logout(View view)
    {
        Intent intent2 = new Intent(AdminHomeActivity.this, MainActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent2);
        finish();
    }

}
