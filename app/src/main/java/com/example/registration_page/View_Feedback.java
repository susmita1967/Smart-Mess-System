package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityAdminDashboardBinding;
import com.example.registration_page.databinding.ActivityViewAttendanceBinding;
import com.example.registration_page.databinding.ActivityViewFeedbackBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

public class View_Feedback extends AppCompatActivity {
    Calendar calendar;
    String date1;
    DatabaseReference studentsRef;
    DatabaseReference feedbacksRef;
    ActivityViewFeedbackBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityViewFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar11.setVisibility(View.INVISIBLE);
        calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat;
        Date currentDate = new Date();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd", Locale.getDefault());
        String formattedDate = dateFormat1.format(currentDate);
        Date currentDate1 = new Date();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM", Locale.getDefault());
        String formattedDate1 = dateFormat2.format(currentDate1);
        Date currentDate2 = new Date();
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy", Locale.getDefault());
        String formattedDate2 = dateFormat3.format(currentDate2);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date1 = dateFormat.format(calendar.getTime());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = dayFormat.format(calendar.getTime());
       if(dayOfWeek.equals("Sunday"))
       {
           binding.feeddate.setText(date1);
           countFeedbackWeekly();
       }
       if(formattedDate.equals("30") || (formattedDate1.equals("02") && (formattedDate.equals("28") || formattedDate.equals("29"))))
       {
           binding.feeddate2.setText(date1);
           studentsRef = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(formattedDate2).child(formattedDate1);
           countFeedbackMonthly();
       }
    }
    private void countFeedbackWeekly() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Set to the first day of the week (Sunday)
        Date startDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String startDateString = dateFormat.format(startDate);
        feedbacksRef = FirebaseDatabase.getInstance().getReference().child("Feedbacks");
        binding.progressBar11.setVisibility(View.VISIBLE);
        feedbacksRef.orderByKey().startAt(startDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numberOfStudentsWithWorstFeedback = 0;
                int numberOfStudentsWithGoodFeedback = 0;
                int numberOfStudentsWithAverageFeedback = 0;

                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot feedbackSnapshot : dateSnapshot.getChildren()) {
                        Integer feedbackValue = feedbackSnapshot.child("f").getValue(Integer.class);
                        if (feedbackValue != null) {
                            switch (feedbackValue) {
                                case 1:
                                    numberOfStudentsWithGoodFeedback++;
                                    break;
                                case 2:
                                    numberOfStudentsWithAverageFeedback++;
                                    break;
                                case 3:
                                    numberOfStudentsWithWorstFeedback++;
                                    break;
                                // Handle other cases if needed
                            }
                        }
                    }
                }

                if(numberOfStudentsWithWorstFeedback>50)
                {
                    String weeklyFeedback = "Good: " + numberOfStudentsWithGoodFeedback +
                            "\nAverage: " + numberOfStudentsWithAverageFeedback +
                            "\nWorst: " + numberOfStudentsWithWorstFeedback+"\n\n\n The feedback is more worst. So please give the good food to students..!1";
                    binding.feedweektext.setText(weeklyFeedback);
                    binding.progressBar11.setVisibility(View.INVISIBLE);
                }
                else
                {
                    String weeklyFeedback = "Good: " + numberOfStudentsWithGoodFeedback +
                            "\nAverage: " + numberOfStudentsWithAverageFeedback +
                            "\nWorst: " + numberOfStudentsWithWorstFeedback;
                    binding.feedmonthtext.setText(weeklyFeedback);
                    binding.progressBar11.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    private void countFeedbackMonthly() {
        binding.progressBar11.setVisibility(View.VISIBLE);
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numberOfStudentsWithWorstFeedback = 0;
                int numberOfStudentsWithGoodFeedback = 0;
                int numberOfStudentsWithAverageFeedback = 0;

                for (DataSnapshot monthSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot studentSnapshot : monthSnapshot.getChildren()) {
                        Integer feed = studentSnapshot.child("f").getValue(Integer.class);

                        if (feed != null) {
                            switch (feed) {
                                case 1:
                                    numberOfStudentsWithGoodFeedback++;
                                    break;
                                case 2:
                                    numberOfStudentsWithAverageFeedback++;
                                    break;
                                case 3:
                                    numberOfStudentsWithWorstFeedback++;
                                    break;
                                // Handle other cases if needed
                            }
                        }
                    }
                }
                if(numberOfStudentsWithWorstFeedback>50)
                {
                    String monthlyFeedback = "Good: " + numberOfStudentsWithGoodFeedback +
                            "\nAverage: " + numberOfStudentsWithAverageFeedback +
                            "\nWorst: " + numberOfStudentsWithWorstFeedback+"\n\n\n The feedback is more worst. So please give the good food to students..!1";
                    binding.feedmonthtext.setText(monthlyFeedback);

                    Handler handler=new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String frommail="savitriwaranamess@gmail.com";
                            String tomail="susmitajanawade6@gmail.com";
                            sendMailUsingSendGrid(frommail,tomail,"Regarding mess food improvement","Dear Madam\n" +
                                    "\n" +
                                    "I trust this message finds you well. I am writing to bring to your attention some concerns regarding the quality of food in the mess that have been observed by several students, including myself.\n" +
                                    "\n" +
                                    "Recently, there has been a noticeable decline in the taste of the meals provided. Instances of undercooked food and cleanliness issues have been reported, impacting the overall satisfaction of the students relying on the mess.\n" +
                                    "\n" +
                                    "I kindly request your presence to assess the situation firsthand and address the matter appropriately. Your intervention will be highly appreciated in ensuring the well-being of the students and maintaining the standards we expect from our institution.\n" +
                                    "\n" +
                                    "Thank you for your prompt attention to this matter.\n" +
                                    "\n" +
                                    "Best regards, Students");
                            Toast.makeText(View_Feedback.this,"Sending mail...!!!!",Toast.LENGTH_SHORT).show();
                        }
                    });

                    binding.progressBar11.setVisibility(View.INVISIBLE);
                }
                else
                {
                    String monthlyFeedback = "Good: " + numberOfStudentsWithGoodFeedback +
                            "\nAverage: " + numberOfStudentsWithAverageFeedback +
                            "\nWorst: " + numberOfStudentsWithWorstFeedback;
                    binding.feedmonthtext.setText(monthlyFeedback);
                    binding.progressBar11.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }


        /*studentsRef.child(date1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numberOfStudentsWithWorstFeedback = 0;
                int numberOfStudentsWithGoodFeedback = 0;
                int numberOfStudentsWithAverageFeedback = 0;
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    Integer feed = studentSnapshot.child("f").getValue(Integer.class);
                    if (feed == 1) {
                        numberOfStudentsWithGoodFeedback++;
                    } else if (feed == 2){
                        numberOfStudentsWithAverageFeedback++;
                    }
                    else if (feed == 3)
                    {
                        numberOfStudentsWithWorstFeedback++;
                    }
                }
                String weeklyfeedback="Good :"+numberOfStudentsWithGoodFeedback+"\n Average : "+numberOfStudentsWithAverageFeedback+"\n Worst: "+numberOfStudentsWithWorstFeedback;
                binding.feedweektext.setText(weeklyfeedback);
                binding.progressBar11.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log.e("View_Attendance", "Error fetching students: " + databaseError.getMessage());
            }
        });*/
    /*--------------------------------------------------------------------------------------------------------------------*/
    /*private  void countFeedbackMonthly()
    {
        binding.progressBar11.setVisibility(View.VISIBLE);
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numberOfStudentsWithWorstFeedback = 0;
                int numberOfStudentsWithGoodFeedback = 0;
                int numberOfStudentsWithAverageFeedback = 0;
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot student1:studentSnapshot.getChildren())
                    {
                       // Integer feed = studentSnapshot.child("id").child("f").getValue(Integer.class);
                        Integer feed = student1.child("f").getValue(Integer.class);
                        if (feed == 1) {
                            numberOfStudentsWithGoodFeedback++;
                        } else if (feed == 2){
                            numberOfStudentsWithAverageFeedback++;
                        }
                        else if (feed == 3)
                        {
                            numberOfStudentsWithWorstFeedback++;
                        }
                    }
                    String monthlyfeedback="Good :"+numberOfStudentsWithGoodFeedback+"\n Average : "+numberOfStudentsWithAverageFeedback+"\n Worst: "+numberOfStudentsWithWorstFeedback;
                    binding.feedmonthtext.setText(monthlyfeedback);
                    binding.progressBar11.setVisibility(View.INVISIBLE);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log.e("View_Attendance", "Error fetching students: " + databaseError.getMessage());
            }
        });



    }*/
    private  void sendMailUsingSendGrid(String from,String to,String subject,String mailBody)
    {
        Hashtable<String,String>params=new Hashtable<>();
        params.put("to",to);
        params.put("from",from);
        params.put("subject",subject);
        params.put("text",mailBody);

        SendGridAsyncTask email=new SendGridAsyncTask();
        try{
            email.execute(params);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}