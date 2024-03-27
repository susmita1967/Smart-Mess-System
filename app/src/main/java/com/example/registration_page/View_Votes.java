package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.registration_page.databinding.ActivityViewAttendanceBinding;
import com.example.registration_page.databinding.ActivityViewVotesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class View_Votes extends AppCompatActivity {

    ActivityViewVotesBinding binding;

    Calendar calendar;
    String date;
    DatabaseReference studentsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewVotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String mealtime = "";
        calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = dateFormat.format(calendar.getTime());
        binding.displaydate2.setText(date);
        dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = dateFormat.format(calendar.getTime());
        binding.displayday2.setText(dayOfWeek);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 7 && hourOfDay < 14) {
            mealtime = "Afternoon";
            binding.mealtime2.setText(mealtime);
        } else if (hourOfDay >= 14 && hourOfDay <= 22) {
            mealtime = "Night";
            binding.mealtime2.setText(mealtime);
        }
        //  t.setText(date);
        studentsRef = FirebaseDatabase.getInstance().getReference().child("Votes");

        // Add listener to count number of students with attendance = "yes"
        // Add listener to count number of students with attendance = "yes"
        studentsRef.child(date).child(mealtime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numberOfStudentsWithAttendanceVeg = 0;
                int numberOfStudentsWithAttendanceNonveg = 0;
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    Integer vote = studentSnapshot.child("vote").getValue(Integer.class);
                    if (vote == 1) {
                        numberOfStudentsWithAttendanceVeg++;
                    } else {
                        numberOfStudentsWithAttendanceNonveg++;
                    }
                }
                binding.vegcount.setText(String.valueOf(numberOfStudentsWithAttendanceVeg));
                binding.nonvegcount.setText(String.valueOf(numberOfStudentsWithAttendanceNonveg));
                //  Log.d("MainActivity", "Number of students with attendance yes: " + numberOfStudentsWithAttendanceYes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
              //  Log.e("MainActivity", "Error fetching students: " + databaseError.getMessage());
            }
        });

    }
}