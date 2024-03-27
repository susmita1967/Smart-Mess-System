package com.example.registration_page;

import static com.example.registration_page.EncryptDecrypt.encrypt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Registration extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    private String Name, grn, degree, cls,p,cp;
    private String phn, rm;

    //private ProgressBar progressBar;
    FirebaseDatabase db;
    boolean pv,pv1;
    DatabaseReference reference;

    FirebaseAuth auth;

    //FirebaseAuth auth;
    String encryptedtpassword,encryptconpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        binding.progressBar5.setVisibility(View.INVISIBLE);
       // getSupportActionBar().setTitle("New Registration");
        binding.pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    if(event.getRawX()>=binding.pass.getRight()-binding.pass.getCompoundDrawables()[Right].getBounds().width())
                    {
                        int selection=binding.pass.getSelectionEnd();
                        if(pv1)
                        {
                            binding.pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            binding.pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pv1=false;
                        }
                        else
                        {
                            binding.pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            binding.pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pv1=true;
                        }
                        binding.pass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        binding.conpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    if(event.getRawX()>=binding.conpass.getRight()-binding.conpass.getCompoundDrawables()[Right].getBounds().width())
                    {
                        int selection=binding.conpass.getSelectionEnd();
                        if(pv)
                        {
                            binding.conpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            binding.conpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pv=false;
                        }
                        else
                        {
                            binding.conpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            binding.conpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pv=true;
                        }
                        binding.conpass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        binding.registerbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar5.setVisibility(View.VISIBLE);
                Name = binding.Name.getText().toString();
                grn = binding.GRN.getText().toString();
                degree = binding.Degree.getText().toString();
                cls = binding.Class.getText().toString();
                phn = binding.PhoneNumber.getText().toString();
                rm = binding.Roomnum.getText().toString();
                p=binding.pass.getText().toString();
                cp=binding.conpass.getText().toString();


                boolean check=validateInfo(Name, grn, degree, cls, phn, rm,p,cp);
                if(check==true)
                {
                    if (!Name.isEmpty() && !grn.isEmpty() && !degree.isEmpty() && !cls.isEmpty() && !phn.isEmpty() && !rm.isEmpty()&& !p.isEmpty()&& !cp.isEmpty()) {
                     //here check
                        try {
                           encryptedtpassword= encrypt(binding.pass.getText().toString());
                        }catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                            e.printStackTrace();
                        }
                        try {
                             encryptconpassword=encrypt(binding.conpass.getText().toString());
                        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                            e.printStackTrace();
                        }// to here

                        Students students = new Students(Name, grn, degree, cls, phn, rm,encryptedtpassword, encryptconpassword);
                        db = FirebaseDatabase.getInstance();
                        reference = db.getReference("Students");
                       reference.child(grn).setValue(students).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                binding.progressBar5.setVisibility(View.INVISIBLE);
                                binding.Name.setText("");
                                binding.GRN.setText("");
                                binding.Degree.setText("");
                                binding.Class.setText("");
                                binding.PhoneNumber.setText("");
                                binding.Roomnum.setText("");
                                binding.pass.setText("");
                                binding.conpass.setText("");

                                Toast.makeText(Registration.this, "Successfully Registered..!!", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(Registration.this,successsfully.class);//Login
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    else {
                        Toast.makeText(Registration.this, "All fields are required..!!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Registration.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

 /*@Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null)
        {
            Toast.makeText(Registration.this,"User with this GRN alredy exists..!!",Toast.LENGTH_SHORT).show();
            binding.GRN.requestFocus();
        }

    }*/

    private boolean validateInfo(String Name, String grn,String degree,String cls,String phn,String rm,String p,String cp)
    {
        if(Name.length()==0)
        {
            binding.Name.requestFocus();
            binding.Name.setError("Field cannot be Empty..!");
            return false;
        }
        if(!Name.isEmpty())
        {
            Pattern pattern=Pattern.compile(new String("^[a-zA-Z\\s]{2,}$"));
            Matcher matcher=pattern.matcher(Name);
            if(!matcher.matches())
            {
                binding.Name.requestFocus();
                binding.Name.setError("Only alphabates are allowed");
                return false;
            }
        }
        if (degree.length()==0)
        {
            binding.Degree.requestFocus();
            binding.Degree.setError("Field cannot be Empty..!");
            return false;
        }
        if (!degree.isEmpty())
        {
            if (!degree.equals("Engineering") && !degree.equals("Engineering ")&& !degree.equals("Pharmacy") && !degree.equals("YC") && !degree.equals("Polytechnic")&& !degree.equals("Pharmacy ") && !degree.equals("YC ") && !degree.equals("Polytechnic "))
            {
                binding.Degree.requestFocus();
                binding.Degree.setError("Please enter valid degree(Engineering,Pharmacy,YC,Polytechnic)..!");
                return false;
            }
        }
        if (grn.length()==0)
        {
            binding.GRN.requestFocus();
            binding.GRN.setError("Field cannot be Empty..!");
            return false;
        }
        if (!degree.isEmpty())
        {

            if(degree.equals("Engineering") || degree.equals("Engineering ")) {
                if (grn.length() != 11) {
                    binding.GRN.requestFocus();
                    binding.GRN.setError("GRN length must be 11");
                    return false;
                }
                else
                {
                    char g1 = grn.charAt(0);
                    char g2 = grn.charAt(1);
                    int g12=Integer.parseInt(String.valueOf(g1));
                    int g13=Integer.parseInt(String.valueOf(g2));
                    if (!(g12 >= 0 && g12 <= 9) || !(g13 >= 0 && g13 <= 9))
                    {
                        binding.GRN.requestFocus();
                        binding.GRN.setError("GRN is not valid for Engineering..!");
                        return false;

                    }
                    char g3 = grn.charAt(2);
                    char g4 = grn.charAt(3);
                    char g5 = grn.charAt(4);
                    char g6 = grn.charAt(5);
                    if ((g3 != 'U' && g3 != 'u') && (g3 != 'G' && g3 != 'g')||(g3 != 'U' && g3 != 'u') && (g3 != 'D' && g3 != 'd')) {
                        binding.GRN.requestFocus();
                        binding.GRN.setError("GRN is not valid for Engineering..!");
                        return false;
                    }
                    if (g5 != 'C' && g5 != 'c' && g5 != 'e' && g5 != 'E' && g5 != 'M' && g5 != 'm') {
                        binding.GRN.requestFocus();
                        binding.GRN.setError("GRN is not valid for Engineering..!");
                        return false;
                    }
                    if (g6 != 'S' && g6 != 's' && g6 != 'e' && g6 != 'E' && g6 != 'T' && g6 != 't' && g6 != 'H' && g6 != 'h') {
                        binding.GRN.requestFocus();
                        binding.GRN.setError("GRN is not valid for Engineering..!");
                        return false;
                    }
                    char g7 = grn.charAt(6);
                    char g8 = grn.charAt(7);
                    char g9 = grn.charAt(8);
                    char g10 = grn.charAt(9);
                    char g11 = grn.charAt(10);
                    int g14=Integer.parseInt(String.valueOf(g7));
                    int g15=Integer.parseInt(String.valueOf(g8));
                    int g16=Integer.parseInt(String.valueOf(g9));
                    int g17=Integer.parseInt(String.valueOf(g10));
                    int g18=Integer.parseInt(String.valueOf(g11));
                    if (!(g14 >= 0 && g14 <= 9) || !(g15 >= 0 && g15 <= 9) || !(g16 >= 0 && g16 <= 9) || !(g17 >= 0 && g17 <= 9) || !(g18 >= 0 && g18 <= 9)) {
                        binding.GRN.requestFocus();
                        binding.GRN.setError("GRN is not valid for Engineering..!");
                        return false;
                    }
                       /* if(cls=="FY")
                        {
                            if((g1!=2 && g2!=3))
                            {
                                binding.GRN.requestFocus();
                                binding.GRN.setError("GRN is incorrect..!");
                                return false;
                            }
                        }
                        else if (cls=="SY")
                        {
                            if((g1!=2 && g2!=2))
                            {
                                binding.GRN.requestFocus();
                                binding.GRN.setError("GRN is incorrect..!");
                                return false;
                            }
                        }
                        else if (cls=="TY")
                        {
                            if((g1!=2 && g2!=1))
                            {
                                binding.GRN.requestFocus();
                                binding.GRN.setError("GRN is incorrect..!");
                                return false;
                            }
                        }
                        else if (cls=="BE")
                        {
                            if((g1!=2 && g2!=0))
                            {
                                binding.GRN.requestFocus();
                                binding.GRN.setError("GRN is incorrect..!");
                                return false;
                            }
                        }*/
                }
            }
            if (degree.equals("Pharmacy")||degree.equals("Pharmacy "))
            {
                if(grn.length()!=10)
                {
                    binding.GRN.requestFocus();
                    binding.GRN.setError("Please enter valid GRN for Pharmacy..!");
                    return false;
                }
                else
                {
                    Pattern pattern=Pattern.compile(new String("^\\d{10}$"));
                    Matcher matcher=pattern.matcher(grn);
                    if(!matcher.matches())
                    {
                        binding.GRN.requestFocus();
                        binding.GRN.setError("Please enter valid GRN for Pharmacy..!");
                        return false;
                    }
                }
            }
            if (degree.equals("Polytechnic")||degree.equals("Polytechnic "))
            {
                if(grn.length()!=10)
                {
                    binding.GRN.requestFocus();
                    binding.GRN.setError("Please enter valid GRN for Polytechnic..!");
                    return false;
                }
                else
                {
                    Pattern pattern=Pattern.compile(new String("^\\d{10}$"));
                    Matcher matcher=pattern.matcher(grn);
                    if(!matcher.matches())
                    {
                        binding.GRN.requestFocus();
                        binding.GRN.setError("Please enter valid GRN for Polytechnic..!");
                        return false;
                    }


                }
            }
            if (degree.equals("YC"))
            {
                int y=Integer.parseInt(grn);
                if(!(y>0 && y<700))
                {
                    binding.GRN.requestFocus();
                    binding.GRN.setError("Please enter valid GRN for YC..!");
                    return false;
                }

            }


            // }
        }
        if (cls.length()==0)
        {
            binding.GRN.requestFocus();
            binding.GRN.setError("Field cannot be Empty..!");
            return false;
        }
        if (!cls.isEmpty())
        {
            if(degree.equals("Engineering") ||degree.equals("Engineering ") || degree.equals("Pharmacy ")|| degree.equals("Pharmacy")||degree.equals("Polytechnic")||degree.equals("Polytechnic"))
            {
                if(!cls.equals("FY") && !cls.equals("SY") && !cls.equals("TY") && !cls.equals("BE") )
                {
                    binding.Class.requestFocus();
                    binding.Class.setError("Please enter valid class..!");
                    return false;
                }
            }
            if (degree.equals("YC"))
            {
                if(!cls.equals("11") && !cls.equals("12"))
                {
                    binding.Class.requestFocus();
                    binding.Class.setError("Please enter valid class..!");
                    return false;
                }
            }
        }
        if (phn.length()==0)
        {
            binding.PhoneNumber.requestFocus();
            binding.PhoneNumber.setError("Phone number is not valid..!");
            return false;
        }
        if (!phn.isEmpty())
        {
            if(phn.length()!=10)
            {
                binding.PhoneNumber.requestFocus();
                binding.PhoneNumber.setError("Phone number must be of length 10..!");
                return false;
            }
            else {
                char y=phn.charAt(0);
                if (y=='0')
                {
                    binding.PhoneNumber.requestFocus();
                    binding.PhoneNumber.setError("Phone number does not start with 0..!");
                    return false;
                }
                Pattern pattern=Pattern.compile(new String("^\\d{10}$"));
                Matcher matcher=pattern.matcher(phn);
                if(!matcher.matches())

                {
                    binding.PhoneNumber.requestFocus();
                    binding.PhoneNumber.setError("Phone number is not valid..!");
                    return false;
                }
            }

        }
        if(rm.length()==0)
        {
            binding.Roomnum.requestFocus();
            binding.Roomnum.setError("Room Number Field cannot be Empty..!");
            return false;
        }
        if (!rm.isEmpty())
        {
            int r=Integer.parseInt(rm);
            if (!(r>0 && r<200)) {
                binding.Roomnum.requestFocus();
                binding.Roomnum.setError("Room number must be numeric..!");
                return false;
            }
            else {
                int x=Integer.parseInt(rm);
                if (!(x>0 && x<200))
                {
                    binding.Roomnum.requestFocus();
                    binding.Roomnum.setError("Room number should be between 1 to 200..!");
                    return false;
                }
            }
        }
        if (p.length()==0)
        {
            binding.pass.requestFocus();
            binding.pass.setError("Password Field cannot be Empty..!");
            return false;
        }
        if (p.length()<8)
        {
            binding.pass.requestFocus();
            binding.pass.setError("Password must contain minimum length (8)..!");
            return false;
        }
        if (p.length()>16)
        {
            binding.pass.requestFocus();
            binding.pass.setError("Password only contains maximum length (16)..!");
            return false;
        }
        if (cp.length()==0)
        {
            binding.conpass.requestFocus();
            binding.conpass.setError("Field cannot be Empty..!");
            return false;
        }
        if (!cp.isEmpty())
        {
            if(cp.length()<8 || cp.length()>16)
            {
                binding.conpass.requestFocus();
                binding.conpass.setError("Password must contains minimum 8 and max 16 length..!");
                return false;
            }
            else
            {
                if(!p.equals(cp))
                {
                    binding.conpass.requestFocus();
                    binding.conpass.setError("Password doesn't matches..!");
                    return false;
                }
            }
        }

        return true;
        /*else if(grn.length()!=11)
        {
             char g1=grn.charAt(0);
             char g2=
            if()
            binding.GRN.requestFocus();
            binding.GRN.setError("GRN length must be 11");
            return false;
        }*/

    }


}