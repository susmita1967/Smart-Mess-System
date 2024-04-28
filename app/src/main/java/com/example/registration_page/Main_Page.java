package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.registration_page.databinding.ActivityForgetPaswordBinding;
import com.example.registration_page.databinding.ActivityMainPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Main_Page extends AppCompatActivity {
    DatabaseReference reference;
    ActivityMainPageBinding binding;
    FirebaseDatabase db;
    private SimpleDateFormat dateFormat;

    Calendar calendar;

    String pg,username,un,profurl,prof,iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        calendar=Calendar.getInstance();
        dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        String dayOfWeek = dateFormat.format(calendar.getTime());
        Intent intent=getIntent();
        pg=intent.getStringExtra("passgrn");
        iduser=getIntent().getStringExtra("pg2");
        db = FirebaseDatabase.getInstance();
        username = getIntent().getStringExtra("username");
        if (username != null) {
            binding.usernamemain.setText(username);
        }
        else
        {
            reference=db.getReference("Students");
            if(pg!=null) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(pg).exists())
                        {
                            String username1 = snapshot.child(pg).child("name").getValue(String.class);
                            un=username1;
                            binding.usernamemain.setText(username1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        prof=getIntent().getStringExtra("urlimg");
        if(prof!=null)
        {
            Glide.with(Main_Page.this)
                    .load(prof)
                    .into(binding.profileuser);
        }
        else
        {
            if(pg!=null)
            {
                db=FirebaseDatabase.getInstance();
                DatabaseReference userRef =db.getReference("Students");
                userRef.child(pg).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Retrieve the download URL from the Realtime Database
                            String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                            profurl=profileImageUrl;
                            // Use an image loading library like Glide or Picasso to load the image into the ImageView
                            Glide.with(Main_Page.this)
                                    .load(profileImageUrl)
                                    .into(binding.profileuser);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors
                    }
                });
            }
        }


        binding.profileuser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent10=new Intent(Main_Page.this, User_Profile.class);
                Intent i=getIntent();
                String pg11=i.getStringExtra("passgrn");
                String un1=i.getStringExtra("username");
                if(pg11!=null)
                {
                    intent10.putExtra("pg2",pg11);
                    intent10.putExtra("username", un1);
                    intent10.putExtra("urlimg",prof);
                }
                else
                {
                    Toast.makeText(Main_Page.this,"errorrrrrrrrrrr...!!!",Toast.LENGTH_SHORT).show();

                }
                startActivity(intent10);
                return false;
            }
        });
        binding.usermainlogoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Main_Page.this,"Successfully logged out...!!!",Toast.LENGTH_SHORT).show();
                Intent intent3=new Intent(Main_Page.this, Admin_User.class);
                startActivity(intent3);
                finish();
            }
        });
        binding.viewmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Page.this, View_Menu.class);
                Intent i1=getIntent();
                String pg11=i1.getStringExtra("passgrn");
                String un2=i1.getStringExtra("username");
                String prof1=i1.getStringExtra("urlimg");
                intent.putExtra("passgrn",pg11);
                intent.putExtra("username", un2); // Pass the username obtained from Firebase
                intent.putExtra("urlimg",prof1);
                startActivity(intent);
               /* Intent intent4=new Intent(Main_Page.this, View_Menu.class);
               // Toast.makeText(Main_Page.this,"Successfully logged out...!!!",Toast.LENGTH_SHORT).show();
                startActivity(intent4);*/
            }
        });
        binding.markattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mealtime = "";
                calendar=Calendar.getInstance();
                dateFormat=new SimpleDateFormat("yyyy/MM/dd");
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                if (!(hourOfDay >= 5 && hourOfDay < 14) && !(hourOfDay >= 14 && hourOfDay <= 24))
                {
                    Toast.makeText(Main_Page.this,"Time is exceeded...You cannot mark attendance now..!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent=new Intent(Main_Page.this,Mark_Attendance.class);
                    Intent i=getIntent();
                    String pg12=i.getStringExtra("passgrn");
                    String un3=i.getStringExtra("username");
                    String prof2=getIntent().getStringExtra("urlimg");
                    intent.putExtra("username", un3);
                    intent.putExtra("passgrn",pg12);
                    intent.putExtra("urlimg",prof2);
                    startActivity(intent);
                }


            }
        });
           binding.vegnonveg.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(dayOfWeek.equals("Wednesday")||dayOfWeek.equals("Sunday"))
                   {
                       Intent intent=new Intent(Main_Page.this,Voting_Page.class);
                   Intent i=getIntent();
                   String pg13=i.getStringExtra("passgrn");
                   String un4=i.getStringExtra("username");
                   String prof3=getIntent().getStringExtra("urlimg");
                   intent.putExtra("username", un4);
                       intent.putExtra("pg2",pg13);
                       intent.putExtra("urlimg",prof3);
                       startActivity(intent);
                   }
                   else
                   {
                       Toast.makeText(Main_Page.this,"You cannot go for voting on other day",Toast.LENGTH_SHORT).show();
                   }
                   }
           });

        binding.feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main_Page.this,Feedback_Page.class);
                Intent i=getIntent();
                String pg14=i.getStringExtra("passgrn");
                String un5=i.getStringExtra("username");
                String prof4=getIntent().getStringExtra("urlimg");
                intent.putExtra("username", un5);
                intent.putExtra("pg2",pg14);
                intent.putExtra("urlimg",prof4);
                startActivity(intent);
            }
        });
        //  //here i have to fetch the student name from database

        if(pg!=null)
        {
            String date,m="";
            date=dateFormat.format(calendar.getTime());
            String dayOfWeek1 = dateFormat.format(calendar.getTime());
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            if (hourOfDay >= 5 && hourOfDay < 13) {
                m= "Afternoon";
            }
            else if (hourOfDay >= 14 && hourOfDay <= 17) {
                m= "Night";
            }
            DatabaseReference rf=FirebaseDatabase.getInstance().getReference("Attendance");
            rf.child(date).child(m).child(pg).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        int a=snapshot.child("p").getValue(Integer.class);
                        if(a==1)
                       binding.yourRecentAttendance.setText("Yes");
                        else
                            binding.yourRecentAttendance.setText("No");

                    }
                    else
                    {
                        binding.yourRecentAttendance.setText("Attendance not marked yet");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                }
            });
        }

    }
}