package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityVotingPageBinding;
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
import java.util.Locale;

public class Voting_Page extends AppCompatActivity {
    ActivityVotingPageBinding binding;
    RadioButton radioButton;
    int n;
    String pg4;
    String nme,id,nm;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    String date;
    FirebaseDatabase db;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVotingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar7.setVisibility(View.INVISIBLE);
        Intent intent=getIntent();
        pg4=intent.getStringExtra("pg2");
        String mealtime = "";
        calendar=Calendar.getInstance();
        dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        date=dateFormat.format(calendar.getTime());
        binding.displaydate1.setText(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = dateFormat.format(calendar.getTime());
        binding.displayday1.setText(dayOfWeek);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 5 && hourOfDay < 13) {
            mealtime= "Afternoon";
          binding.mealtime1.setText(mealtime);
        } else if (hourOfDay >= 14 && hourOfDay <= 17) {
            mealtime= "Night";
           binding.mealtime1.setText(mealtime);
        }
        if(pg4!=null) {
            reference = FirebaseDatabase.getInstance().getReference().child("Students");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    nme = snapshot.child(pg4).child("name").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            Log.d("Object not found","null string");
        }
        String finalMealtime = mealtime;
        binding.submitvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar7.setVisibility(View.VISIBLE);
                int radioId=binding.radiovote.getCheckedRadioButtonId();
                if(!(hourOfDay >= 5 && hourOfDay < 14)&&!(hourOfDay >= 14 && hourOfDay < 17))
                {
                    Toast.makeText(Voting_Page.this,"Time exceeded for voting..!!",Toast.LENGTH_SHORT).show();
                }
                else{
                if(radioId==-1)
                {
                    Toast.makeText(Voting_Page.this,"Please mark your vote..!!",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (radioId == binding.radioveg.getId()) {
                        n = 1;
                    } else {
                        n = 0;
                    }
                    id = pg4;
                    nm = nme;
                    Votes vote = new Votes(id, nm, n, finalMealtime, date);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference();
                    reference.child("Votes").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.hasChild(id)) {
                                reference.child("Votes").child(date).child(finalMealtime).child(id).child("vote").setValue(n).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        binding.progressBar7.setVisibility(View.INVISIBLE);
                                        String username1 = getIntent().getStringExtra("username");
                                        String profurl = getIntent().getStringExtra("urlimg");
                                        if (username1 != null && profurl != null) {
                                            Intent intent = new Intent(Voting_Page.this, Main_Page.class);
                                            intent.putExtra("username", username1); // Pass the username back to Main_Page
                                            intent.putExtra("urlimg", profurl);
                                            Toast.makeText(Voting_Page.this, "Vote is marked!!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        binding.progressBar7.setVisibility(View.INVISIBLE);
                                        Toast.makeText(Voting_Page.this, "Failed to mark vote..!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                reference.child("Votes").child(date).child(finalMealtime).child(id).setValue(vote).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        binding.progressBar7.setVisibility(View.INVISIBLE);
                                        String username1 = getIntent().getStringExtra("username");
                                        String profurl = getIntent().getStringExtra("urlimg");
                                        if (username1 != null && profurl != null) {
                                            Intent intent = new Intent(Voting_Page.this, Main_Page.class);
                                            intent.putExtra("username", username1); // Pass the username back to Main_Page
                                            intent.putExtra("urlimg", profurl);
                                            Toast.makeText(Voting_Page.this, "Vote is marked!!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            binding.progressBar7.setVisibility(View.INVISIBLE);
                            Toast.makeText(Voting_Page.this, "Database error..!!" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                   /* reference.child(id).setValue(vote).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent=new Intent(Voting_Page.this, Main_Page.class);
                            Toast.makeText(Voting_Page.this,"Vote is marked..!!",Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }
                    });*/
                }


            }

        });
    }
}