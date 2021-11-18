package com.example.frezzing.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.frezzing.R;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void GotoLoginActivity(View view)
    {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }

    public void GotoRegisterActivity(View view)
    {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox=preferences.getString("remember","");
        if (checkbox.equals("true"))
        {
            startActivity(new Intent(MainActivity.this, CategoriesActivity.class));
            finish();
        }

    }
}
