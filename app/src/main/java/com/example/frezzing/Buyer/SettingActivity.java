package com.example.frezzing.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frezzing.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, addressEditText;
    private TextView profileChangeTextBtn, closeTextBtn, saveTextButton;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    String UID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Initialization();
        UserInformationDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText);

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity(imageUri).setAspectRatio(1, 1)
                        .start(SettingActivity.this);
            }
        });
    }
    private void Initialization() {
        profileImageView = findViewById(R.id.setting_profile_image);
        fullNameEditText = findViewById(R.id.settings_full_name);
        userPhoneEditText = findViewById(R.id.settings_profile_number);
        addressEditText = findViewById(R.id.settings_profile_address);
        profileChangeTextBtn = findViewById(R.id.profile_image_change_btn);
        closeTextBtn = findViewById(R.id.close_settings_btn);
        saveTextButton = findViewById(R.id.update_settings_btn);
        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        UID= FirebaseAuth.getInstance().getUid();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error,try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingActivity.this, SettingActivity.class));
            finish();
        }

    }

    private void UserInformationDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText) {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("image").exists()) {
                    String image = dataSnapshot.child("image").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String phone="";
                    if (dataSnapshot.hasChild("phoneOrder")){
                        phone = dataSnapshot.child("phoneOrder").getValue().toString();
                    }
                    else {
                        phone = dataSnapshot.child("phone").getValue().toString();
                    }

                    String address = dataSnapshot.child("address").getValue().toString();

                    Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImageView);
                    fullNameEditText.setText(name);
                    userPhoneEditText.setText(phone);
                    addressEditText.setText(address);
                }
                else {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String phone="";

                    phone = dataSnapshot.child("phone").getValue().toString();

                    String address = dataSnapshot.child("address").getValue().toString();

                    fullNameEditText.setText(name);
                    userPhoneEditText.setText(phone);
                    addressEditText.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void CloseSettingActivity(View view) {
        startActivity(new Intent(SettingActivity.this, CategoriesActivity.class));
        finish();
    }

    public void SaveUpdateProfileInfo(View view) {

        if (checker.equals("clicked")) {
            if (TextUtils.isEmpty(fullNameEditText.getText().toString())) {
                Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(addressEditText.getText().toString())) {
                Toast.makeText(this, "Name is address.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(userPhoneEditText.getText().toString())) {
                Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
            } else {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Update Profile");
                progressDialog.setMessage("Please wait, while we are updating your account information");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if (imageUri != null) {
                    final StorageReference fileRef = storageProfilePrictureRef
                            .child(UID + ".jpg");

                    uploadTask = fileRef.putFile(imageUri);

                    uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            return fileRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("name", fullNameEditText.getText().toString());
                                userMap.put("address", addressEditText.getText().toString());
                                userMap.put("phone", userPhoneEditText.getText().toString());
                                userMap.put("image", myUrl);
                                ref.child(UID).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(SettingActivity.this, CategoriesActivity.class));
                                Toast.makeText(SettingActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SettingActivity.this, "Error:" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
                }
            }

        } else {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("name", fullNameEditText.getText().toString());
            userMap.put("address", addressEditText.getText().toString());
            userMap.put("phoneOrder", userPhoneEditText.getText().toString());
            ref.child(UID).updateChildren(userMap);


            startActivity(new Intent(SettingActivity.this, CategoriesActivity.class));
            Toast.makeText(SettingActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
            finish();

        }
    }

}
