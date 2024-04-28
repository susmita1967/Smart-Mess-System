package com.example.registration_page;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityViewAttendanceBinding;
import com.example.registration_page.databinding.ActivityViewVotesBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class View_Votes extends AppCompatActivity {

    ActivityViewVotesBinding binding;

    Calendar calendar;
    String date;
    String mealtime = "";
    DatabaseReference studentsRef, databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewVotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = dateFormat.format(calendar.getTime());
        binding.displaydate2.setText(date);
        dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = dateFormat.format(calendar.getTime());
        binding.displayday2.setText(dayOfWeek);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 1 && hourOfDay < 14) {
            mealtime = "Afternoon";
            binding.mealtime2.setText(mealtime);
        } else if (hourOfDay >= 14 && hourOfDay <= 24) {
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
                Log.e("MainActivity", "Error fetching students: " + databaseError.getMessage());
            }
        });
        //databaseReference = FirebaseDatabase.getInstance().getReference("Votes");
        binding.downloadvotingreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndSavePdf();
            }
        });
        binding.downloadprevotereport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Votes.this, Previous_vote_reports.class);
                startActivity(intent);
            }
        });
    }
    private void generateAndSavePdf() {
        // Create a new file in internal storage
        File file = new File(getFilesDir(), "vote_report.pdf");

        try {
            // Create PDF document
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDocument);
            Paragraph heading=new Paragraph("Shree Warana Vibhag Shikshan Mandal Mess").setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(22);;
            document.add(heading);
            // Add Title
            Paragraph title = new Paragraph("Vote Report").setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(20);
            document.add(title);

            // Add Date
            SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");
            String currentDay = sdfDay.format(new Date());
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdfDate.format(new Date());
            Paragraph date1 = new Paragraph("Day: "+currentDay+"    Date: " + currentDate).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(12);
            document.add(date1);

            // Add a new line after the date
            document.add(new Paragraph("\n"));
            com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(3);

            // Add column headers
            table.addCell(new Cell().add(new Paragraph("GRN")));
            table.addCell(new Cell().add(new Paragraph("Name")));
            table.addCell(new Cell().add(new Paragraph("Vote")));

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Votes");
            databaseReference.child(date).child(mealtime).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate through the dataSnapshot to fetch data
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Fetch id, name, and vote
                        String id = snapshot.getKey(); // Assuming id is the key
                        String name = snapshot.child("nm").getValue(String.class);
                        Integer vote = snapshot.child("vote").getValue(Integer.class);
                        String x = (vote != null && vote == 1) ? "Veg" : "Non-Veg";

                        // Add fetched data to the table
                        table.addCell(new Cell().add(new Paragraph(id)));
                        table.addCell(new Cell().add(new Paragraph(name)));
                        table.addCell(new Cell().add((new Paragraph(x))));
                    }

                    // Add the table to the PDF document
                    document.add(table);

                    // Close the document after adding all data
                    document.close();

                    // Toast to indicate PDF generation
                    Toast.makeText(View_Votes.this, "PDF generated", Toast.LENGTH_SHORT).show();

                    // Upload the generated PDF to Firebase under "Vote Reports"
                    uploadPdfToFirebase(file);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void uploadPdfToFirebase(File file) {
        // Create a unique key based on date and mealtime
        String key = date + "_" + mealtime;

        // Upload PDF file to Firebase Storage under "Vote Reports" node with unique key
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Vote Reports").child(key + ".pdf");
        storageRef.putFile(Uri.fromFile(file))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // PDF upload success
                        Log.d(TAG, "PDF upload successful");
                        Toast.makeText(View_Votes.this, "PDF uploaded to Firebase", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // PDF upload failed
                        Log.e(TAG, "PDF upload failed: " + e.getMessage());
                        Toast.makeText(View_Votes.this, "Failed to upload PDF to Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /*private void generateAndSavePdf() {
        // Create a new file in internal storage
        File file = new File(getFilesDir(), "vote_report.pdf");

        try {
            // Create PDF document
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDocument);
             Paragraph heading=new Paragraph("Shree Warana Vibhag Shikshan Mandal Mess").setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(22);;
            document.add(heading);
            // Add Title
            Paragraph title = new Paragraph("Vote Report").setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(20);
        document.add(title);

        // Add Date
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");
            String currentDay = sdfDay.format(new Date());
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdfDate.format(new Date());
            Paragraph date1 = new Paragraph("Day: "+currentDay+"    Date: " + currentDate).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(12);
            document.add(date1);

        // Add a new line after the date
        document.add(new Paragraph("\n"));
            com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(3);

            // Add column headers
            table.addCell(new Cell().add(new Paragraph("GRN")));
            table.addCell(new Cell().add(new Paragraph("Name")));
            table.addCell(new Cell().add(new Paragraph("Vote")));

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Votes");
            databaseReference.child(date).child(mealtime).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate through the dataSnapshot to fetch data
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Fetch id, name, and vote
                        String id = snapshot.getKey(); // Assuming id is the key
                        String name = snapshot.child("nm").getValue(String.class);
                        Integer vote = snapshot.child("vote").getValue(Integer.class);
                        String x = (vote != null && vote == 1) ? "Veg" : "Non-Veg";

                        // Add fetched data to the table
                        table.addCell(new Cell().add(new Paragraph(id)));
                        table.addCell(new Cell().add(new Paragraph(name)));
                        table.addCell(new Cell().add((new Paragraph(x))));
                    }

                    // Add the table to the PDF document
                    document.add(table);

                    // Close the document after adding all data
                    document.close();

                    // Notify user about successful PDF generation
                    Toast.makeText(View_Votes.this, "PDF generated and saved to internal storage", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}