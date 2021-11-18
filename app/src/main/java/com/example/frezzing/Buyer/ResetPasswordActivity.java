package com.example.frezzing.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frezzing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button sendEmailbtn;
    private EditText resetEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetEmail = findViewById(R.id.email_reset);
        sendEmailbtn = findViewById(R.id.send_email_reset);
        mAuth = FirebaseAuth.getInstance();

        sendEmailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = resetEmail.getText().toString();

                if (TextUtils.isEmpty(userName))
                {
                    Toast.makeText(ResetPasswordActivity.this, "Please, Enter Email First", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(userName).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, "Please, Check Your Email Inbox...", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(ResetPasswordActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
