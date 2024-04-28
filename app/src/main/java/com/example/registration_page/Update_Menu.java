package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.widget.ProgressBar;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.registration_page.databinding.ActivityAdminDashboardBinding;
import com.example.registration_page.databinding.ActivityAdminUserBinding;
import com.example.registration_page.databinding.ActivityUpdateMenuBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Update_Menu extends AppCompatActivity {

    ActivityUpdateMenuBinding binding;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    //ProgressBar progressBar;
    final int c[]={0};
    final int c1[]={0};
    final int c2[]={0};
    final int c3[]={0};
    final int c4[]={0};
    final int c5[]={0};
    final int c6[]={0};
    final int c7[]={0};
    final int c8[]={0};
    DatabaseReference reference;

    String roti,meal, sabji1, sabji2, sweetdish, rice, dahitak, papad, specialdish, sider1, sider2;
    private ArrayList<String> selectedCheckboxValues = new ArrayList<>();
    private ArrayList<String> selectedCheckboxValues1 = new ArrayList<>();
    private ArrayList<String> selectedCheckboxValues2 = new ArrayList<>();
    private ArrayList<String> selectedCheckboxValues3 = new ArrayList<>();
    private ArrayList<String> selectedCheckboxValues4 = new ArrayList<>();
    private ArrayList<String> selectedCheckboxValues5 = new ArrayList<>();
    private ArrayList<String> selectedCheckboxValues6 = new ArrayList<>();
    private ArrayList<String> selectedCheckboxValues7 = new ArrayList<>();
    private ArrayList<String> selectedCheckboxValues8 = new ArrayList<>();
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityUpdateMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar8.setVisibility(View.INVISIBLE);
        calendar= Calendar.getInstance();
        dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        date=dateFormat.format(calendar.getTime());
        binding.editTextDate.setText(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = dateFormat.format(calendar.getTime());
        createNotificationChannel();
        final int[] p = {0};
        binding.afternoon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection1(isChecked, "Afternoon");

            }
        });
        binding.night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection1(isChecked, "Night");
            }
        });
        binding.radiochapati.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection2(isChecked, "Chapati");
            }
        });
        binding.radioparatha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection2(isChecked, "Paratha");
            }
        });
        binding.radiopuri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection2(isChecked, "Poori");
            }
        });
        binding.radioplainrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection3(isChecked, "Plain-Rice");
            }
        });
        binding.radiojirarice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection3(isChecked, "Jeera-Rice");
            }
        });
        binding.masur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Masur");
            }
        });

        binding.kobi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Kobi");
            }
        });

        binding.mug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Mug");
            }
        });

        binding.dodka.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Dodka");
            }
        });

        binding.bhendi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Bhendi");
            }
        });

        binding.dalbhaji.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Dal Bhaji");
            }
        });

        binding.batata.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Batata Bhaji");
            }
        });

        binding.vangi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Vangi Bhaji");
            }
        });

        binding.matki.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Matki");
            }
        });

        binding.chana.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Chana");
            }
        });

        binding.flower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Flower");
            }
        });

        binding.benas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Benas");
            }
        });

        binding.methi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection(isChecked, "Methi Bhaji");
            }
        });
        binding.lonche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection4(isChecked, "Lonache");
            }
        });
        binding.kharda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection4(isChecked, "Kharada");
            }
        });
        binding.chatani.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection4(isChecked, "Chatani");
            }
        });

            binding.saladyes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection4(isChecked, "Salad");

                }
            });
            binding.chiken.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection7(isChecked, "Chicken");
                }
            });
            binding.paneer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection7(isChecked, "Paneer");
                }
            });
            binding.egg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection7(isChecked, "Egg-Curry");
                }
            });
            binding.pulav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection7(isChecked, "Pulav");

                }
            });
            binding.biryani.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection7(isChecked, "Biryani");
                }
            });
            binding.kurma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection7(isChecked, "Kurma");
                }
            });
            binding.shrikhand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection8(isChecked, "Shri-Khand");

                }
            });
            binding.amrkhand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection8(isChecked, "Amr-Khand");
                }
            });
            binding.rasMalai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection8(isChecked, "Ras-Malai");

                }
            });
            binding.gulabjamun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection8(isChecked, "Gulab-Jamun");
                }
            });
            binding.kheer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection8(isChecked, "Kheer");
                }
            });
            binding.basundi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleCheckboxSelection8(isChecked, "Basundi");
                }
            });


        binding.dahi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection5(isChecked, "Dahi");
            }
        });
        binding.tak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection5(isChecked, "Tak");
            }
        });
        binding.kadhi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection5(isChecked, "Kadhi");
            }
        });
        binding.papad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection6(isChecked, "Papad");
            }
        });
        binding.bobis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection6(isChecked, "Bobi's");
            }
        });
        binding.trikintalan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckboxSelection6(isChecked, "Trikon-Papad");
            }
        });
        int finalP = p[0];
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar8.setVisibility(View.VISIBLE);
                if(c1[0]!=1)
                {
                    showSnackbar(v, "Please select meal for only one time..!");
                    binding.progressBar8.setVisibility(View.INVISIBLE);
                } else if (c2[0]!=1) {
                    showSnackbar(v, "Please select one type of roti..!");
                    binding.progressBar8.setVisibility(View.INVISIBLE);
                }else if (c3[0]!=1) {
                    showSnackbar(v, "Please select one type of rice ..!");//
                    binding.progressBar8.setVisibility(View.INVISIBLE);
                }
                else {
                    meal = selectedCheckboxValues1.get(0);
                    if (meal.equals("Afternoon")) {
                        setAlarm(7, 20); // Set alarm at 7.20 AM
                    } else if (meal.equals("Night")) {
                        setAlarm(18,20 ); // Set alarm at 4.50 PM (17:00)
                    }
                    roti=selectedCheckboxValues2.get(0);
                    rice=selectedCheckboxValues3.get(0);
                    sider1=selectedCheckboxValues4.get(0);
                    if ((dayOfWeek.equals("Sunday") && meal.equals("Afternoon")) || (dayOfWeek.equals("Wednesday") && meal.equals("Night"))) {
                        // Special day logic
                        if (c[0] != 0) {
                            binding.progressBar8.setVisibility(View.INVISIBLE);
                            showSnackbar(v, "You cannot select sabji's on special day");
                        } else {
                            if (c6[0]!=1) {
                                showSnackbar(v, "Please select one type of Papad..!");
                                binding.progressBar8.setVisibility(View.INVISIBLE);
                            }else if (c7[0]!=1) {
                                showSnackbar(v, "Please select one type of Special Menu..!");
                                binding.progressBar8.setVisibility(View.INVISIBLE);
                            }else if (c8[0]!=1) {
                                showSnackbar(v, "Please select one type of Sweet-Dish..!");
                                binding.progressBar8.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                sider2=selectedCheckboxValues4.get(0);
                                papad=selectedCheckboxValues6.get(0);
                                specialdish=selectedCheckboxValues7.get(0);
                                sweetdish=selectedCheckboxValues8.get(0);
                                Menus m=new Menus(date,meal,roti, sabji1, sabji2,sider1, sider2, sweetdish, rice, dahitak, papad, specialdish);
                                db = FirebaseDatabase.getInstance();
                                reference = db.getReference();

                                reference.child("Menus").child(date).child(meal).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        binding.progressBar8.setVisibility(View.INVISIBLE);
                                        Toast.makeText(Update_Menu.this, "Menu saved Successfully..!!"+meal, Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(Update_Menu.this,Admin_Dashboard.class);//Login
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }

                        }
                    } else {

                        // Regular day logic
                        if (c6[0]!=0||c7[0]!=0||c8[0]!=0) {
                            binding.progressBar8.setVisibility(View.INVISIBLE);
                            showSnackbar(v, "You cannot select special menus on other days");
                            binding.progressBar8.setVisibility(View.INVISIBLE);
                        } else if (c[0] != 2) {
                            binding.progressBar8.setVisibility(View.INVISIBLE);
                            showSnackbar(v, "Please select 2 sabji's");
                            binding.progressBar8.setVisibility(View.INVISIBLE);
                        }else if (c5[0]!=1) {
                            showSnackbar(v, "Please select one type of Dahi-Tak..!");
                            binding.progressBar8.setVisibility(View.INVISIBLE);
                        } else {
                            dahitak=selectedCheckboxValues5.get(0);
                            sabji1=selectedCheckboxValues.get(0);
                            sabji2=selectedCheckboxValues.get(1);
                            c[0]=0;
                            Menus m=new Menus(date,meal,roti, sabji1, sabji2,sider1, sider2, sweetdish, rice, dahitak, papad, specialdish);
                            db = FirebaseDatabase.getInstance();
                            reference = db.getReference();
                            reference.child("Menus").child(date).child(meal).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Update_Menu.this, "Menu saved Successfully..!!"+meal, Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(Update_Menu.this,Admin_Dashboard.class);//Login
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }
                }

            }
        });
    }
            // Method to handle checkbox selection
            private void handleCheckboxSelection(boolean isChecked, String checkboxValue) {
                if (isChecked) {
                    // If checkbox is checked, add its value to the ArrayList
                    selectedCheckboxValues.add(checkboxValue);
                    c[0]++;
                } else {
                    // If checkbox is unchecked, remove its value from the ArrayList
                    selectedCheckboxValues.remove(checkboxValue);
                    c[0]--;
                }
            }
    private void handleCheckboxSelection1(boolean isChecked, String checkboxValue) {
        if (isChecked) {
            // If checkbox is checked, add its value to the ArrayList
            selectedCheckboxValues1.add(checkboxValue);
            c1[0]++;
        } else {
            // If checkbox is unchecked, remove its value from the ArrayList
            selectedCheckboxValues1.remove(checkboxValue);
            c1[0]--;
        }
    }
    private void handleCheckboxSelection2(boolean isChecked, String checkboxValue) {
        if (isChecked) {
            // If checkbox is checked, add its value to the ArrayList
            selectedCheckboxValues2.add(checkboxValue);
            c2[0]++;
        } else {
            // If checkbox is unchecked, remove its value from the ArrayList
            selectedCheckboxValues2.remove(checkboxValue);
            c2[0]--;
        }
    }
    private void handleCheckboxSelection3(boolean isChecked, String checkboxValue) {
        if (isChecked) {
            // If checkbox is checked, add its value to the ArrayList
            selectedCheckboxValues3.add(checkboxValue);
            c3[0]++;
        } else {
            // If checkbox is unchecked, remove its value from the ArrayList
            selectedCheckboxValues3.remove(checkboxValue);
            c3[0]--;
        }
    }
    private void handleCheckboxSelection4(boolean isChecked, String checkboxValue) {
        if (isChecked) {
            // If checkbox is checked, add its value to the ArrayList
            selectedCheckboxValues4.add(checkboxValue);
            c4[0]++;
        } else {
            // If checkbox is unchecked, remove its value from the ArrayList
            selectedCheckboxValues4.remove(checkboxValue);
            c4[0]--;
        }
    }
    private void handleCheckboxSelection5(boolean isChecked, String checkboxValue) {
        if (isChecked) {
            // If checkbox is checked, add its value to the ArrayList
            selectedCheckboxValues5.add(checkboxValue);
            c5[0]++;
        } else {
            // If checkbox is unchecked, remove its value from the ArrayList
            selectedCheckboxValues5.remove(checkboxValue);
            c5[0]--;
        }
    }
    private void handleCheckboxSelection6(boolean isChecked, String checkboxValue) {
        if (isChecked) {
            // If checkbox is checked, add its value to the ArrayList
            selectedCheckboxValues6.add(checkboxValue);
            c6[0]++;
        } else {
            // If checkbox is unchecked, remove its value from the ArrayList
            selectedCheckboxValues6.remove(checkboxValue);
            c6[0]--;
        }
    }
    private void handleCheckboxSelection7(boolean isChecked, String checkboxValue) {
        if (isChecked) {
            // If checkbox is checked, add its value to the ArrayList
            selectedCheckboxValues7.add(checkboxValue);
            c7[0]++;
        } else {
            // If checkbox is unchecked, remove its value from the ArrayList
            selectedCheckboxValues7.remove(checkboxValue);
            c7[0]--;
        }
    }
    private void handleCheckboxSelection8(boolean isChecked, String checkboxValue) {
        if (isChecked) {
            // If checkbox is checked, add its value to the ArrayList
            selectedCheckboxValues8.add(checkboxValue);
            c8[0]++;
        } else {
            // If checkbox is unchecked, remove its value from the ArrayList
            selectedCheckboxValues8.remove(checkboxValue);
            c8[0]--;
        }
    }
    private void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);

        snackbar.setAction("Action", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // Show the Snackbar
        snackbar.show();
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            CharSequence name = "AttendanceReminderChannel";
            String description = "Channel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Attendance",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm(int hourOfDay, int minute) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay); // Set the hour
        calendar.set(Calendar.MINUTE, minute); // Set the minute

        // If the alarm time has already passed, add one day to it
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}