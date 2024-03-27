package com.example.registration_page;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/*
public class AlarmReceiver extends BroadcastReceiver {
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatabaseReference menuRef;
    FirebaseUser currentUser;
    String date, mealtime, a, b, c, d, e, f;
    private static final int NOTIFICATION_ID = 100;

    @Override
    public void onReceive(Context context, Intent intent) {

        //
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = dateFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = dateFormat.format(calendar.getTime());
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 12 && hourOfDay < 14) {
            mealtime = "Afternoon";
        } else if (hourOfDay >= 14 && hourOfDay <= 23) {
            mealtime = "Night";
        }


        menuRef = FirebaseDatabase.getInstance().getReference().child("Menus");

        String finalMealtime = mealtime;
        menuRef.child(date).child(mealtime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((dayOfWeek.equals("Sunday") && finalMealtime.equals("Afternoon")) || (dayOfWeek.equals("Wednesday") && finalMealtime.equals("Night"))) {
                    a = snapshot.child("roti").getValue(String.class);
                    b = snapshot.child("rice").getValue(String.class);
                    e = snapshot.child("specialdish").getValue(String.class);
                    f = snapshot.child("sweetdish").getValue(String.class);
                    c = snapshot.child("sider2").getValue(String.class);
                    d = snapshot.child("sider1").getValue(String.class);
                } else {
                    a = snapshot.child("roti").getValue(String.class);
                    b = snapshot.child("rice").getValue(String.class);
                    c = snapshot.child("sabji1").getValue(String.class);
                    d = snapshot.child("sabji2").getValue(String.class);
                    e = snapshot.child("sider1").getValue(String.class);
                    f = snapshot.child("dahitak").getValue(String.class);
                }
                showNotification(context);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showNotification(Context context) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.largenotification, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeicon = bitmapDrawable.getBitmap();


       Intent notificationIntent = new Intent(context, Login.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Attendance")
                .setLargeIcon(largeicon)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Reminder")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        // Build the expandable content text
        String contentText = "Mark Your Attendance\n" +
                "Today's Menu : " +
                mealtime +"\n"+
                "1." + a + "\n" +
                "2." + b + "\n" +
                "4." + c + "\n" +
                "5." + d + "\n" +
                "6." + e + "\n" +
                "7." + f + "\n";

        // Create the BigTextStyle object
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(contentText);

        // Set the style to the builder
        builder.setStyle(bigTextStyle);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
*/
public class AlarmReceiver extends BroadcastReceiver {
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatabaseReference menuRef;
    FirebaseUser currentUser;
    String contentText;
    String date, mealtime, a, b, c, d, e, f;
    private static final int NOTIFICATION_ID = 100;
    String dayOfWeek;

    @Override
    public void onReceive(Context context, Intent intent) {
        //
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = dateFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        dayOfWeek = dateFormat.format(calendar.getTime());
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 7 && hourOfDay < 14) {
            mealtime = "Afternoon";
        } else if (hourOfDay >= 14 && hourOfDay <= 23) {
            mealtime = "Night";
        }
        /**/

        menuRef = FirebaseDatabase.getInstance().getReference().child("Menus");

        String finalMealtime = mealtime;
        menuRef.child(date).child(mealtime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((dayOfWeek.equals("Sunday") && finalMealtime.equals("Afternoon")) || (dayOfWeek.equals("Wednesday") && finalMealtime.equals("Night"))) {
                    a = snapshot.child("roti").getValue(String.class);
                    b = snapshot.child("rice").getValue(String.class);
                    e = snapshot.child("specialdish").getValue(String.class);
                    f = snapshot.child("sweetdish").getValue(String.class);
                    c = snapshot.child("sider2").getValue(String.class);
                    d = snapshot.child("sider1").getValue(String.class);
                } else {
                    a = snapshot.child("roti").getValue(String.class);
                    b = snapshot.child("rice").getValue(String.class);
                    c = snapshot.child("sabji1").getValue(String.class);
                    d = snapshot.child("sabji2").getValue(String.class);
                    e = snapshot.child("sider1").getValue(String.class);
                    f = snapshot.child("dahitak").getValue(String.class);
                }

                // Redirect the user based on login status
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Intent notificationIntent;
                if (currentUser != null) {
                    // User is already signed in, navigate to the desired screen.
                    notificationIntent = new Intent(context, Mark_Attendance.class);
                } else {
                    notificationIntent = new Intent(context, Login.class);
                }

                // Set the flag for new task
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                showNotification(context, pendingIntent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void showNotification(Context context, PendingIntent pendingIntent) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.largenotification, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeicon = bitmapDrawable.getBitmap();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Attendance")
                .setLargeIcon(largeicon)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Reminder")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        // Build the expandable content text

        if(dayOfWeek.equals("Sunday") || (dayOfWeek.equals("Wednesday") && mealtime.equals("Night")))
        {
             contentText = "Mark Your Vote\n" +
                    "Today's Menu : " +
                    mealtime + "\n" +
                    "1." + a + "\n" +
                    "2." + b + "\n" +
                    "4." + c + "\n" +
                    "5." + d + "\n" +
                    "6." + e + "\n" +
                    "7." + f + "\n";
        }
        else
        {
             contentText = "Mark Your Attendance\n" +
                    "Today's Menu : " +
                    mealtime + "\n" +
                    "1." + a + "\n" +
                    "2." + b + "\n" +
                    "4." + c + "\n" +
                    "5." + d + "\n" +
                    "6." + e + "\n" +
                    "7." + f + "\n";
        }


        // Create the BigTextStyle object
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(contentText);

        // Set the style to the builder
        builder.setStyle(bigTextStyle);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
