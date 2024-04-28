package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityFeedbackPageBinding;
import com.example.registration_page.databinding.ActivityMarkAttendanceBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Feedback_Page extends AppCompatActivity {

    int f;
    String pg4,nme,id,nm;
    DatabaseReference reference;
    Calendar calendar;
    String date;
    FirebaseDatabase db;
    ActivityFeedbackPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityFeedbackPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat;
        Date currentDate = new Date();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = dateFormat.format(calendar.getTime());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        binding.feedbackdatedisplay.setText(date);
        binding.progressBar10.setVisibility(View.INVISIBLE);
        Intent intent=getIntent();
        pg4=intent.getStringExtra("pg2");
        reference = FirebaseDatabase.getInstance().getReference().child("Students");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(pg4!=null)
                {
                    nme=snapshot.child(pg4).child("name").getValue(String.class);
                }
                else
                {
                    Toast.makeText(Feedback_Page.this,"id is null!!",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.feedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar10.setVisibility(View.VISIBLE);
                int radioId=binding.radiofeed.getCheckedRadioButtonId();
                if(radioId==-1)
                {
                    Toast.makeText(Feedback_Page.this,"Please mark your attendance..!!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(radioId==binding.radiogood.getId())
                    {
                        f=1;
                    }
                    else if(radioId==binding.radioavg.getId()){
                        f = 2;
                    }
                    else
                    {
                        f=3;
                    }
                    id=pg4;
                    nm=nme;
                    Feedbacks feedback = new Feedbacks(id,nm,f,date);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference();
                    reference.child("Feedbacks").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(id))
                            {
                                reference.child("Feedbacks").child(date).child(id).child("f").setValue(f).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        binding.progressBar10.setVisibility(View.INVISIBLE);
                                        String username1 = getIntent().getStringExtra("username");
                                        String profurl=getIntent().getStringExtra("urlimg");
                                        if (username1 != null || profurl!=null)  {//and ch or kelay
                                            Intent intent=new Intent(Feedback_Page.this, Main_Page.class);
                                            /*intent.putExtra("username", username1); // Pass the username back to Main_Page
                                            intent.putExtra("urlimg",profurl);*/
                                            Toast.makeText(Feedback_Page.this,"Feedback submitted!!",Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        binding.progressBar10.setVisibility(View.INVISIBLE);
                                        Toast.makeText(Feedback_Page.this,"Failed to submit the feedback..!!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                            {
                                reference.child("Feedbacks").child(date).child(id).setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        binding.progressBar10.setVisibility(View.INVISIBLE);
                                        String username1 = getIntent().getStringExtra("username");
                                        String profurl=getIntent().getStringExtra("urlimg");
                                        if (username1 != null && profurl!=null) {
                                            Intent intent=new Intent(Feedback_Page.this, Main_Page.class);
                                            intent.putExtra("username", username1); // Pass the username back to Main_Page
                                            intent.putExtra("urlimg",profurl);
                                            Toast.makeText(Feedback_Page.this,"Feedback submitted!!",Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            binding.progressBar10.setVisibility(View.INVISIBLE);
                            Toast.makeText(Feedback_Page.this,"Database error..!!"+error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                   /* reference.child("Feedbacks").child(id).setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent=new Intent(Feedback_Page.this, Main_Page.class);
                            Toast.makeText(Feedback_Page.this,"Feedback is submitted..!!",Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }
                    });*/
                }

            }
        });

    }
}