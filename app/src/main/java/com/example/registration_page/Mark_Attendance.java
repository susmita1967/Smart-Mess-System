package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityMainPageBinding;
import com.example.registration_page.databinding.ActivityMarkAttendanceBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Mark_Attendance extends AppCompatActivity {

    int p1;
    String i,nme,nm;
    DatabaseReference reference;
    FirebaseDatabase db;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    String date;
     //String pg3;
    ActivityMarkAttendanceBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMarkAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar6.setVisibility(View.INVISIBLE);
        Intent intent=getIntent();
        //final String pg3=intent.getStringExtra("pg2");
        String mealtime = "";
        calendar=Calendar.getInstance();
        dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        date=dateFormat.format(calendar.getTime());
        binding.d.setText(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = dateFormat.format(calendar.getTime());
        binding.daydisplay.setText(dayOfWeek);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 5 && hourOfDay < 14) {
            mealtime= "Afternoon";
            binding.mealtime.setText(mealtime);
        } else if (hourOfDay >= 14 && hourOfDay <= 24) {
            mealtime= "Night";
            binding.mealtime.setText(mealtime);
        }
        final String pg3=intent.getStringExtra("passgrn");
        if(pg3!=null)
        {
            reference = FirebaseDatabase.getInstance().getReference().child("Students");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    nme=snapshot.child(pg3).child("name").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            Log.d("Null object found","here it stops");
        }


        String finalMealtime = mealtime;
        binding.submitatten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar6.setVisibility(View.VISIBLE);
                int radioId=binding.radiogrp.getCheckedRadioButtonId();
                if(radioId==-1)
                {
                    Toast.makeText(Mark_Attendance.this,"Please mark your attendance..!!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(radioId==binding.radioyes.getId())
                    {
                        p1=1;
                    }
                    else {
                        p1 = 0;
                    }

                    //final String id=pg3;
                    nm=nme;
                    Intent i1=getIntent();
                    String finalMealtime1 = finalMealtime;
                    //final String pg31=i1.getStringExtra("pg2");
                    if(i1.getStringExtra("passgrn")!=null)
                    {
                        Attendance attendance = new Attendance(pg3,nm,p1, finalMealtime,date);
                        db = FirebaseDatabase.getInstance();
                        reference = db.getReference();
                        reference.child("Attendance").child(date).child(finalMealtime).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists() && snapshot.hasChild(pg3))
                                {
                                    reference.child("Attendance").child(date).child(finalMealtime).child(pg3).child("p").setValue(p1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            binding.progressBar6.setVisibility(View.INVISIBLE);
                                            String iduser=getIntent().getStringExtra("passgrn");
                                            String username1 = getIntent().getStringExtra("username");
                                            String profurl=getIntent().getStringExtra("urlimg");
                                            //if (username1 != null || profurl!=null) {//and ch or kelay
                                                Intent intent = new Intent(Mark_Attendance.this, Main_Page.class);
                                                intent.putExtra("username", username1); // Pass the username back to Main_Page
                                                intent.putExtra("urlimg",profurl);
                                                intent.putExtra("passgrn", iduser);
                                                intent.putExtra("pg2", iduser);
                                                Toast.makeText(Mark_Attendance.this,"Attendance is marked!!",Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                                finish();
                                           // }
                                            /*else
                                            {
                                                //Toast.makeText(Mark_Attendance.this,"username is null!!",Toast.LENGTH_SHORT).show();
                                            }*/

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            binding.progressBar6.setVisibility(View.INVISIBLE);
                                            Toast.makeText(Mark_Attendance.this,"Failed to mark attendance..!!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else
                                {
                                    //id=pg3;
                                    if(pg3!=null)
                                    {
                                        String finalMealtime2 = finalMealtime;
                                        reference.child("Attendance").child(date).child(finalMealtime).child(pg3).setValue(attendance).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                binding.progressBar6.setVisibility(View.INVISIBLE);
                                                String iduser=getIntent().getStringExtra("pg2");
                                                String username1 = getIntent().getStringExtra("username");
                                                String profurl=getIntent().getStringExtra("urlimg");
                                                //if (username1 != null && profurl!=null) {
                                                    Intent intent = new Intent(Mark_Attendance.this, Main_Page.class);
                                                    intent.putExtra("username", username1); // Pass the username back to Main_Page
                                                    intent.putExtra("urlimg",profurl);
                                                     intent.putExtra("passgrn", iduser);
                                                intent.putExtra("pg2", iduser);
                                                    Toast.makeText(Mark_Attendance.this,"Attendance is marked!!",Toast.LENGTH_SHORT).show();
                                                    startActivity(intent);
                                                    finish();
                                               // }
                                                }
                                        });
                                    }
                                    else
                                    {
                                        Log.d("Null object found","here it stops 2");

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                binding.progressBar6.setVisibility(View.INVISIBLE);
                                Toast.makeText(Mark_Attendance.this,"Database error..!!"+error.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Mark_Attendance.this,"id is null on mark_attendance page",Toast.LENGTH_SHORT).show();

                    }


                }


            }

            });

    }
}