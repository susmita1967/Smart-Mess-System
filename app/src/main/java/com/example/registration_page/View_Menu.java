package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityMainPageBinding;
import com.example.registration_page.databinding.ActivityViewMenuBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class View_Menu extends AppCompatActivity {
    ActivityViewMenuBinding binding;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatabaseReference menuRef;
    private String date,username1,profurl,grn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityViewMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String mealtime = "";
        calendar=Calendar.getInstance();
        Intent i=getIntent();
        grn=i.getStringExtra("passgrn");
        username1 = i.getStringExtra("username");
        profurl=i.getStringExtra("urlimg");
        dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        date=dateFormat.format(calendar.getTime());
        binding.editTextDate.setText(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = dateFormat.format(calendar.getTime());
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (username1 != null && profurl!=null) {//and ch or kelay
                    Intent intent = new Intent(View_Menu.this, Main_Page.class);
                    intent.putExtra("username", username1); // Pass the username back to Main_Page
                    intent.putExtra("urlimg",profurl);
                    intent.putExtra("passgrn",grn);
                    startActivity(intent);
                    finish();
               /* }
                else
                {
                    Toast.makeText(View_Menu.this,"something is null..!!",Toast.LENGTH_SHORT).show();

                }*/


            }
        });

        if (hourOfDay >= 5 && hourOfDay < 14) {
            mealtime= "Afternoon";
        } else if (hourOfDay >= 14 && hourOfDay <= 23) {
            mealtime= "Night";
        }
        menuRef = FirebaseDatabase.getInstance().getReference().child("Menus");

        String finalMealtime = mealtime;
        menuRef.child(date).child(mealtime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if((dayOfWeek.equals("Sunday") && finalMealtime=="Afternoon" )||( dayOfWeek.equals("Wednesday") && finalMealtime=="Night"))
                {
                    binding.item1.setText(finalMealtime);
                    String a=snapshot.child("roti").getValue(String.class);
                    String b=snapshot.child("rice").getValue(String.class);
                    String e=snapshot.child("specialdish").getValue(String.class);
                    String f=snapshot.child("sweetdish").getValue(String.class);
                    String c=snapshot.child("sider2").getValue(String.class);
                    String d=snapshot.child("sider1").getValue(String.class);
                    binding.item2.setText(a);
                    binding.item3.setText(b);
                    binding.item4.setText(c);
                    binding.item5.setText(d);
                    binding.item6.setText(e);
                    binding.item7.setText(f);

                }
               else
                {
                    binding.item1.setText(finalMealtime);
                    String a=snapshot.child("roti").getValue(String.class);
                    String b=snapshot.child("rice").getValue(String.class);
                    String c=snapshot.child("sabji1").getValue(String.class);
                    String d=snapshot.child("sabji2").getValue(String.class);
                    String e=snapshot.child("sider1").getValue(String.class);
                    String f=snapshot.child("dahitak").getValue(String.class);
                    binding.item2.setText(a);
                    binding.item3.setText(b);
                    binding.item4.setText(c);
                    binding.item5.setText(d);
                    binding.item6.setText(e);
                    binding.item7.setText(f);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
