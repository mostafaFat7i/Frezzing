package com.example.frezzing.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frezzing.Admins.AdminHomeActivity;
import com.example.frezzing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rey.material.widget.CheckBox;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button signin;
    private TextView tosignup;
    private CheckBox rememberMe;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked())
                {
                    SharedPreferences preferences =getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                }
                else if (!buttonView.isChecked())
                {
                    SharedPreferences preferences =getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                }
            }
        });
    }

    private void initialization() {
        username = findViewById(R.id.Loginusername);
        password = findViewById(R.id.Loginpassword);
        signin = findViewById(R.id.signin);
        tosignup = findViewById(R.id.tosignup);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        rememberMe=findViewById(R.id.remember_me_chkb);
    }

    public void GoToRegistration(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void LoginUser(View view) {
        final String email = username.getText().toString().trim();
        final String pass = password.getText().toString().trim();

        if (!email.equals("") && !pass.equals("")) {
            progressDialog.setTitle("Account Login");
            progressDialog.setMessage("Please waite,while we are checking the credentials");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            if (email.equals("admin") && pass.equals("admin123"))
            {
                SharedPreferences preferences =getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("remember","false");
                editor.apply();
                startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                finish();
            }
            else {
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "you are login successfully...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, CategoriesActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Make sure email and password are correct", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(this, "Please, make sure you are write email and password.", Toast.LENGTH_LONG).show();

        }


    }

    public void GoToResetPassActivity(View view)
    {
        startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
    }
}
