package com.example.registration_page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityPreviousVoteReportsBinding;
import com.example.registration_page.databinding.ActivityViewPreviousReportsBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Previous_vote_reports extends AppCompatActivity {

    ActivityPreviousVoteReportsBinding binding;
    private String selectedDate;
    private String selectedMealtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviousVoteReportsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        selectedDate = dateFormat.format(calendar.getTime());
        binding.selectedDateTextView1.setText(selectedDate);

        // Initialize mealtime spinner
        binding.mealTimeSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMealtime = parent.getItemAtPosition(position).toString();
                binding.selectedMealTimeTextView1.setText("Mealtime:"+selectedMealtime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Initialize DatePicker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.datePicker1.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                selectedDate = String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, monthOfYear + 1, year);
                binding.selectedDateTextView1.setText("Date:"+selectedDate);
            });
        }

        // Handle View Reports button click
        binding.viewReportButton1.setOnClickListener(v -> downloadReport());
    }

    private void downloadReport() {
        String day = selectedDate.substring(8, 10);
        String fileName = day + "_" + selectedMealtime + ".pdf";
        String year = selectedDate.substring(0, 4);
        String month = selectedDate.substring(5, 7);
        StorageReference reportRef = FirebaseStorage.getInstance().getReference()
                .child("Vote Reports")
                .child(year)
                .child(month)
                .child(fileName);

        File localFile = new File(getFilesDir(), fileName);

        reportRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getApplicationContext(), "Report downloaded successfully", Toast.LENGTH_SHORT).show();
            // Now you can open or view the downloaded report file
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Report does not exist : " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
}
