
package com.example.registration_page;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.registration_page.databinding.ActivityViewAttendanceBinding;
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
import com.itextpdf.text.pdf.PdfPTable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class View_Attendance extends AppCompatActivity {

    DatabaseReference studentsRef,databaseReference;
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
        if (hourOfDay >= 1 && hourOfDay < 14) {
            mealtime = "Afternoon";
            binding.displaymealtime.setText(mealtime);
        } else if (hourOfDay >= 14 && hourOfDay <= 24) {
            mealtime = "Night";
            binding.displaymealtime.setText(mealtime);
        }
       // databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
        countAttendance();
        binding.downloadReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndSavePdf();
            }
        });
        binding.downloadrevreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(View_Attendance.this, View_Previous_Reports.class);
                startActivity(intent);
            }
        });
    }

    private void generateAndSavePdf() {
        File file = new File(getFilesDir(), "attendance_report.pdf");
        try {
            // Create PDF document
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDocument);
            Paragraph heading=new Paragraph("Shree Warana Vibhag Shikshan Mandal Mess").setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(22);;
            document.add(heading);
            // Add Title
            Paragraph title = new Paragraph("Attendance Report").setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(20);
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
            table.addCell(new Cell().add(new Paragraph("Attendance")));

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
            databaseReference.child(date).child(mealtime).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate through the dataSnapshot to fetch data
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Fetch id, name, and attendance
                        String id = snapshot.getKey(); // Assuming id is the key
                        String name = snapshot.child("nm").getValue(String.class);
                        Integer attendance = snapshot.child("p").getValue(Integer.class);
                        String x = (attendance != null && attendance == 1) ? "Yes" : "No";

                        // Add fetched data to the table
                        table.addCell(new Cell().add(new Paragraph(id)));
                        table.addCell(new Cell().add(new Paragraph(name)));
                        table.addCell(new Cell().add((new Paragraph(x))));
                    }

                    // Add the table to the PDF document
                    document.add(table);

                    // Close the document after adding all data
                    document.close();

                    // Convert PDF file to byte array
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while (true) {
                        try {
                            if (!((len = fileInputStream.read(buffer)) != -1)) break;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        byteArrayOutputStream.write(buffer, 0, len);
                    }
                    byte[] pdfBytes = byteArrayOutputStream.toByteArray();

                    // Upload PDF byte array to Firebase Storage
                    uploadPdfToFirebase(pdfBytes);

                    Toast.makeText(View_Attendance.this, "PDF generated and downloaded", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadPdfToFirebase(byte[] pdfBytes) {
        // Create a unique key based on date and mealtime
        String key = date + "_" + mealtime;

        // Upload PDF byte array to Firebase Storage under "Attendance Report" node with unique key
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Attendance Report").child(key + ".pdf");
        storageRef.putBytes(pdfBytes)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // PDF upload success
                        Log.d(TAG, "PDF upload successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // PDF upload failed
                        Log.e(TAG, "PDF upload failed: " + e.getMessage());
                    }
                });
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
                    if (attendance != null) {
                        // Access the integer value of attendance
                        int attendanceValue = attendance.intValue();
                        if (attendanceValue == 1) {
                            numberOfStudentsWithAttendanceYes++;
                        } else {
                            numberOfStudentsWithAttendanceNo++;
                        }
                    }
                }

                binding.totalcount.setText(String.valueOf(numberOfStudentsWithAttendanceYes));
                binding.ab.setText(String.valueOf(numberOfStudentsWithAttendanceNo));
                binding.progressBar9.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    /////////////////////////////code that will store the pdf in internal storage
   /* private void generateAndSavePdf() {
        // Create a new file in internal storage
        File file = new File(getFilesDir(), "attendance_report.pdf");

        try {
            // Create PDF document
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDocument);
             Paragraph heading=new Paragraph("Shree Warana Vibhag Shikshan Mandal Mess").setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(22);;
            document.add(heading);
            // Add Title
            Paragraph title = new Paragraph("Attendance Report").setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(20);
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
            table.addCell(new Cell().add(new Paragraph("Attendance")));

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
            databaseReference.child(date).child(mealtime).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate through the dataSnapshot to fetch data
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Fetch id, name, and attendance
                        String id = snapshot.getKey(); // Assuming id is the key
                        String name = snapshot.child("nm").getValue(String.class);
                        Integer attendance = snapshot.child("p").getValue(Integer.class);
                        String x = (attendance != null && attendance == 1) ? "Yes" : "No";

                        // Add fetched data to the table
                        table.addCell(new Cell().add(new Paragraph(id)));
                        table.addCell(new Cell().add(new Paragraph(name)));
                        table.addCell(new Cell().add((new Paragraph(x))));
                    }

                    // Add the table to the PDF document
                    document.add(table);

                    // Close the document after adding all data
                    document.close();

                    Toast.makeText(View_Attendance.this, "PDF generated and saved to internal storage", Toast.LENGTH_SHORT).show();
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





/////////////////This code will save the pdf in internal storage
   /*  private void generateAndSavePdf() {
         DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
         databaseReference.child(date).child(mealtime).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                createPDF(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void createPDF(DataSnapshot dataSnapshot) {
        try {
            // Get the internal storage directory for the app
            File internalStorageDir = getFilesDir();

            // Create a new file for the PDF
            File pdfFile = new File(internalStorageDir, "Attendance.pdf");

            // Create a PdfWriter instance with the file
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDocument);
            com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(3);

            // Add column headers
            table.addCell(new Cell().add(new Paragraph("GRN")));
            table.addCell(new Cell().add(new Paragraph("Name")));
            table.addCell(new Cell().add(new Paragraph("Attendance")));

            // Iterate through your fetched data and add content to the PDF
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                // Assuming each child represents a piece of data
                //String data = childSnapshot.getValue(String.class);
                String id = childSnapshot.getKey(); // Assuming id is the key
                String name = childSnapshot.child("nm").getValue(String.class);
                Integer attendance = childSnapshot.child("p").getValue(Integer.class);
                String x = (attendance != null && attendance == 1) ? "Yes" : "No";

                // Add fetched data to the table
                table.addCell(new Cell().add(new Paragraph(id)));
                table.addCell(new Cell().add(new Paragraph(name)));
                table.addCell(new Cell().add((new Paragraph(x))));
                // Add content to the PDF
                document.add(table);
            }

            document.close();
            Toast.makeText(View_Attendance.this, "PDF generated and saved to internal storage", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
*/

}
