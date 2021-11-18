package com.example.frezzing.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frezzing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText registName, registPassword, registPhone, registEmail, registAddress;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialization();
    }
    public void initialization() {

        registName = findViewById(R.id.Registusername);
        registPassword = findViewById(R.id.Registpassword);
        registPhone = findViewById(R.id.Registphone);
        registEmail = findViewById(R.id.Registemail);
        registAddress = findViewById(R.id.Registaddress);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
    }


    public void ToLoginActicity(View view)
    {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    public void RegisterUser(View view)
    {
        final String name=registName.getText().toString();
        final String phone=registPhone.getText().toString();
        final String email=registEmail.getText().toString();
        final String password=registPassword.getText().toString();
        final String address=registAddress.getText().toString();

        if (!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals(""))
        {
            progressDialog.setTitle("Creating Account");
            progressDialog.setMessage("Please waite,while we are checking the credentials");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful()) {

                    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
                    String uID = mAuth.getCurrentUser().getUid();

                        HashMap<String,Object> userData=new HashMap<>();
                        userData.put("sid",uID);
                        userData.put("name",name);
                        userData.put("phone",phone);
                        userData.put("password",password);
                        userData.put("email",email);
                        userData.put("address",address);

                        userRef.child("Users").child(uID).updateChildren(userData)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this, "you are register successfully...", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, CategoriesActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                }

                }
            });

        }
        else {
            Toast.makeText(this, "Please, make sure you are full all data in the form.", Toast.LENGTH_LONG).show();
        }

    }
}
