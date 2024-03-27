package com.example.registration_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.registration_page.databinding.ActivityAdminDashboardBinding;
import com.example.registration_page.databinding.ActivityMainPageBinding;

public class Admin_Dashboard extends AppCompatActivity {

    ActivityAdminDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.adminlogoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Admin_User.class));
                finish();
            }
        });
        binding.updatemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Update_Menu.class));
                //finish();
            }
        });
        binding.Viewattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Admin_Dashboard", "Viewattendance clicked");
                Intent intent = new Intent(Admin_Dashboard.this, View_Attendance.class);
                startActivity(intent);
            }
        });
        
        binding.vegnonveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), View_Votes.class));
               // finish();
            }
        });
        binding.feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), View_Feedback.class));
               // finish();
            }
        });
        //finish();
    }
}