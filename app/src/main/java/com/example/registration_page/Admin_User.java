package com.example.registration_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.registration_page.databinding.ActivityAdminUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Admin_User extends AppCompatActivity {

    ActivityAdminUserBinding  binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin_User.this, Admin_Login.class);
                startActivity(intent);
                //finish();
            }

        });

        binding.userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // User is already signed in, navigate to the desired screen.
                    Intent intent=new Intent(Admin_User.this,Main_Page.class);
                    startActivity(intent);
                    //finish();
                }
                else
                {
                    Intent intent = new Intent(Admin_User.this, Login.class);
                    startActivity(intent);
                  //  Toast.makeText(Admin_User.this, "Current user is null", Toast.LENGTH_SHORT).show();
                   // finish();
                }

            }

        });

    }

    }
