package com.example.registration_page;

import static com.example.registration_page.EncryptDecrypt.encrypt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityNewPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class New_Password extends AppCompatActivity {
    ActivityNewPasswordBinding binding;
    FirebaseUser firebaseUser;
    int newp,conp;
    DatabaseReference reference;

    FirebaseDatabase db;
    String np,cnp;
    String fetch_grn;
    String encryptconpassword,encryptedtpassword;
    String grn_fetch;
    boolean pv,pv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar3.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        fetch_grn=intent.getStringExtra("grn_fetch");
        //getSupportActionBar().setTitle("New Password");
        //binding.progressBar3.setVisibility(View.INVISIBLE);
        binding.newpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    if(event.getRawX()>=binding.newpass.getRight()-binding.newpass.getCompoundDrawables()[Right].getBounds().width())
                    {
                        int selection=binding.newpass.getSelectionEnd();
                        if(pv1)
                        {
                            binding.newpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            binding.newpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pv1=false;
                        }
                        else
                        {
                            binding.newpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            binding.newpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pv1=true;
                        }
                        binding.newpass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        binding.connewpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    if(event.getRawX()>=binding.connewpass.getRight()-binding.connewpass.getCompoundDrawables()[Right].getBounds().width())
                    {
                        int selection=binding.connewpass.getSelectionEnd();
                        if(pv)
                        {
                            binding.connewpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            binding.connewpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pv=false;
                        }
                        else
                        {
                            binding.connewpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            binding.connewpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pv=true;
                        }
                        binding.connewpass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });



        binding.resetbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {
                np=binding.newpass.getText().toString();
                cnp=binding.connewpass.getText().toString();
                binding.progressBar3.setVisibility(View.VISIBLE);
                if (np.length() == 0) {
                    binding.newpass.requestFocus();
                    binding.newpass.setError("Password Field cannot be Empty..!");
                }
                if (np.length() < 8) {
                    binding.newpass.requestFocus();
                    binding.newpass.setError("Password must contain minimum length (8)..!");
                }
                if (np.length() > 16) {
                    binding.newpass.requestFocus();
                    binding.newpass.setError("Password only contains maximum length (16)..!");
                }
                if (cnp.length() == 0) {
                    binding.connewpass.requestFocus();
                    binding.connewpass.setError("Field cannot be Empty..!");
                }
                if (!cnp.isEmpty()) {
                    if (cnp.length() < 8 || cnp.length() > 16) {
                        binding.connewpass.requestFocus();
                        binding.connewpass.setError("Password must contains minimum 8 and max 16 length..!");
                    } else {
                        if (!np.equals(cnp)) {
                            binding.connewpass.requestFocus();
                            binding.connewpass.setError("Password doesn't matches..!");
                        }
                    }
                }
                db=FirebaseDatabase.getInstance();
                reference =db.getReference("Students");
                try {
                    encryptedtpassword= encrypt(binding.newpass.getText().toString());
                }catch (NoSuchPaddingException | NoSuchAlgorithmException |
                        InvalidAlgorithmParameterException | InvalidKeyException |
                        IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
                try {
                    encryptconpassword=encrypt(binding.connewpass.getText().toString());
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(fetch_grn).exists())
                        {
                            reference.child(fetch_grn).child("p").setValue(encryptedtpassword);
                            reference.child(fetch_grn).child("cp").setValue(encryptconpassword);
                            binding.progressBar3.setVisibility(View.INVISIBLE);
                            Intent intent1 = new Intent(New_Password.this, Login.class);
                            Toast.makeText(New_Password.this, "Password successfully reset..!!", Toast.LENGTH_SHORT).show();
                            startActivity(intent1);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                /*reference.child(fetch_grn).child("p").setValue(encryptedtpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                            newp=1;
                    }
                });
                reference.child(fetch_grn).child("cp").setValue(encryptconpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        conp=1;
                    }
                });*/
               /* binding.progressBar3.setVisibility(View.INVISIBLE);
                if(newp==1 && conp==1)
                {
                    Toast.makeText(New_Password.this, "Password successfully reset..!!", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(New_Password.this, Login.class);
                    startActivity(intent1);
                    finish();
                }*/
            }});
        binding.cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("Info","here it stops");
                Intent intent2=new Intent(New_Password.this, Login.class);
                Toast.makeText(New_Password.this,"Cancelled..!",Toast.LENGTH_SHORT).show();
                startActivity(intent2);
               // Log.i("Info","here it stops1");
                finish();
            }
        });

    }
}