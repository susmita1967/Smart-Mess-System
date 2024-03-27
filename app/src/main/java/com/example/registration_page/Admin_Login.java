package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityAdminLoginBinding;
import com.example.registration_page.databinding.ActivityLoginBinding;
import com.example.registration_page.databinding.ActivityRegistrationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Admin_Login extends AppCompatActivity {
    ActivityAdminLoginBinding binding;
    DatabaseReference reference;

    String loginusername, loginpassword;
    boolean pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar4.setVisibility(View.INVISIBLE);
        binding.AdPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= binding.AdPassword.getRight() - binding.AdPassword.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = binding.AdPassword.getSelectionEnd();
                        if (pv) {
                            binding.AdPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);
                            binding.AdPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pv = false;
                        } else {
                            binding.AdPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0);
                            binding.AdPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pv = true;
                        }
                        binding.AdPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        binding.adloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = binding.AdPassword.getText().toString();
                String val1 = binding.AdUsername.getText().toString();
                if (val.isEmpty() || val1.isEmpty()) {
                    binding.AdUsername.setError("Fields cannot be empty..!!");
                }
                else{
                    loginusername = binding.AdUsername.getText().toString();// trim ghatl nahi
                    loginpassword = binding.AdPassword.getText().toString();
                    if((loginusername.equals("TKIETMESS")||(loginusername.equals("TKIETMESS "))&&loginpassword.equals("TKIET@MESS")))
                    {
                        binding.progressBar4.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(Admin_Login.this, Admin_Dashboard.class);
                        Toast.makeText(Admin_Login.this, "Successfully Logined..!!", Toast.LENGTH_SHORT).show();
                        binding.progressBar4.setVisibility(View.INVISIBLE);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        binding.AdPassword.setError("Invalid Credentials..!!");
                        binding.AdPassword.requestFocus();
                        binding.progressBar4.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });


    }

   /* @Override
    protected void onStart () {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, navigate to the desired screen.
            startActivity(new Intent(this, Admin_Dashboard.class));
            finish();
        }
        // If the user is not signed in, they will stay on the Forget_Pasword activity.
    }*/

}