
package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.registration_page.databinding.ActivityViewAttendanceBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class View_Attendance extends AppCompatActivity {

    DatabaseReference studentsRef;
   // DatabaseReference databaseReference;
    FirebaseDatabase db;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    String date;
    String mealtime = "";
    ActivityViewAttendanceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar9.setVisibility(View.INVISIBLE);
        // Initialize date and time
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = dateFormat.format(calendar.getTime());
        binding.displaydate.setText(date);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = dayFormat.format(calendar.getTime());
        binding.displayday.setText(dayOfWeek);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 7 && hourOfDay < 14) {
            mealtime = "Afternoon";
            binding.displaymealtime.setText(mealtime);
        } else if (hourOfDay >= 14 && hourOfDay <= 22) {
            mealtime = "Night";
            binding.displaymealtime.setText(mealtime);
        }

        // Initialize Firebase references
      //  FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
       // databaseReference = FirebaseDatabase.getInstance().getReference().child("Attendance");
        // Count attendance and update UI
        countAttendance();
    }

    private void countAttendance() {
        db = FirebaseDatabase.getInstance();
        studentsRef=db.getReference("Attendance");
        binding.progressBar9.setVisibility(View.VISIBLE);
        studentsRef.child(date).child(mealtime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numberOfStudentsWithAttendanceYes = 0;
                int numberOfStudentsWithAttendanceNo = 0;
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    Integer attendance = studentSnapshot.child("p").getValue(Integer.class);
                    //Log.d("View_Attendance", "Attendance value: " + attendance);
                    if (attendance == 1) {
                        numberOfStudentsWithAttendanceYes++;
                    } else {
                        numberOfStudentsWithAttendanceNo++;
                    }
                }

                binding.totalcount.setText(String.valueOf(numberOfStudentsWithAttendanceYes));
                binding.ab.setText(String.valueOf(numberOfStudentsWithAttendanceNo));
                binding.progressBar9.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               // Log.e("View_Attendance", "Error fetching students: " + databaseError.getMessage());
            }
        });
    }

    /*public void onDownloadReportClick(View view) {
        generateAndDownloadPdf();
    }
    // ... (existing code)

    // ... (existing code)

    // ... (existing code)

    private void generateAndDownloadPdf() {
        fetchDataFromFirebase(new FirebaseCallback() {
            @Override
            public void onDataReceived(List<Attendance> firebaseData) {
                try {
                    // Specify the directory where you want to save the PDF file
                    String customDirectoryName = "MyAttendanceReports";
                    File customDirectory = new File(getFilesDir(), customDirectoryName);

                    if (!customDirectory.exists() && !customDirectory.mkdirs()) {
                        // Handle directory creation failure
                        Toast.makeText(View_Attendance.this, "Failed to create directory", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create a new PDF document
                    File pdfFile = createPdfFile(customDirectory);  // Updated this line

                    // Save the PDF file path for future use
                    String pdfFilePath = pdfFile.getAbsolutePath();  // New line

                    FileOutputStream outputStream = new FileOutputStream(pdfFile);
                    PdfWriter writer = new PdfWriter(outputStream);
                    PdfDocument pdf = new PdfDocument(writer);
                    Document document = new Document(pdf);

                    // Iterate through Firebase data and add it to the PDF
                    for (Attendance data : firebaseData) {
                        String studentName = data.getNm();
                        int attendanceMark = data.getP();
                        String at = (attendanceMark == 1) ? "Yes" : "No";

                        // Add data to the PDF
                        document.add(new Paragraph("Student Name: " + studentName));
                        document.add(new Paragraph("Attendance Mark: " + at));
                        document.add(new Paragraph("\n")); // Add space between entries
                    }

                    // Close the document
                    document.close();
                    outputStream.close();

                    MediaScannerConnection.scanFile(
                            View_Attendance.this,
                            new String[]{pdfFile.getAbsolutePath()},
                            null,
                            (path, uri) -> {
                                Log.i("MediaScanner", "Scanned " + path + ":");
                                Log.i("MediaScanner", "-> uri=" + uri);
                            }
                    );

// Notify user and handle further actions if needed
                    Toast.makeText(View_Attendance.this, "Report downloaded successfully. File saved at: " + pdfFilePath, Toast.LENGTH_SHORT).show();

// Now you can use 'pdfFilePath' to access the downloaded PDF file path
                    Log.d("PDF Path", "File saved at: " + pdfFilePath);

                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception
                    Toast.makeText(View_Attendance.this, "PDF downloading(generation) failed!", Toast.LENGTH_SHORT).show();
                    Log.e("PDF Generation", "Error: " + e.getMessage());
                }
            }
        });
    }



    private File createPdfFile(File directory) {
        File pdfFile = new File(directory, "Attendance_Report.pdf");
        try {
            pdfFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfFile;
    }

    private void fetchDataFromFirebase(final FirebaseCallback callback) {
        final List<Attendance> firebaseData = new ArrayList<>();

        databaseReference.child(date).child(mealtime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Attendance data = snapshot.getValue(Attendance.class);
                        if (data != null) {
                            firebaseData.add(data);
                        }
                    }
                }
                callback.onDataReceived(firebaseData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                Log.e("View_Attendance", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }

    private File createPdfFile() {
        File pdfFile = new File(getExternalFilesDir(null), "Attendance_Report.pdf");
        try {
            pdfFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfFile;
    }

    private interface FirebaseCallback {
        void onDataReceived(List<Attendance> firebaseData);
    }*/
}