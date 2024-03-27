package com.example.registration_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.example.registration_page.databinding.ActivityRegistrationBinding;
import com.example.registration_page.databinding.ActivitySuccesssfullyBinding;

public class successsfully extends AppCompatActivity {

    ActivitySuccesssfullyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuccesssfullyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(successsfully.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
