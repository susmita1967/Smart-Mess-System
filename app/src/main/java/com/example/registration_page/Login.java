package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityLoginBinding;
import com.example.registration_page.databinding.ActivityRegistrationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseReference reference;

    String loginusername,loginpassword;
    String decryptedtpassword;
    boolean pv;
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, navigate to the desired screen.
            Intent intent=new Intent(Login.this,Main_Page.class);
            startActivity(intent);
            finish();
        }
        else
        {
           // Toast.makeText(Login.this, "Current user is null", Toast.LENGTH_SHORT).show();
        }
        // If the user is not signed in, they will stay on the Forget_Pasword activity.
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar2.setVisibility(View.INVISIBLE);
        binding.Password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    if(event.getRawX()>=binding.Password.getRight()-binding.Password.getCompoundDrawables()[Right].getBounds().width())
                    {
                        int selection=binding.Password.getSelectionEnd();
                        if(pv)
                        {
                            binding.Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            binding.Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pv=false;
                        }
                        else
                        {
                            binding.Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            binding.Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pv=true;
                        }
                        binding.Password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        binding.registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });
        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar2.setVisibility(View.VISIBLE);
                String val=binding.Password.getText().toString();
                String val1=binding.Username.getText().toString();
                if(val.isEmpty()||val1.isEmpty())
                {
                    binding.Username.setError("Fields cannot be empty..!!");
                }
                else
                {
                    checkUser();

                }

            }
        });

        binding.forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, Forget_Pasword.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    public void checkUser()
    {
        loginusername = binding.Username.getText().toString();// trim ghatl nahi
        loginpassword = binding.Password.getText().toString();
        reference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://smart-mess-e2e8a-default-rtdb.firebaseio.com/");//
        reference.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())//.hasChild(loginusername))
                {
                    binding.Username.setError(null);
                    final String passwordFromDB=snapshot.child(loginusername).child("p").getValue(String.class);
                    try {
                        if(passwordFromDB!=null)
                        {
                            decryptedtpassword = EncryptDecrypt.decrypt(passwordFromDB);
                        }
                        else{  Log.d("Decrypted Password", "decryptedtpassword");}
                        //Toast.makeText(Login.this, decryptedtpassword, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(decryptedtpassword!=null && decryptedtpassword.equals(loginpassword))
                    {
                        binding.progressBar2.setVisibility(View.INVISIBLE);
                        binding.Username.setError(null);
                        Toast.makeText(Login.this, "Successfully Logined..!!", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Login.this, Main_Page.class);
                        intent.putExtra("passgrn",loginusername);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        binding.Password.setError("Invalid Credentials..!!");
                        binding.Password.requestFocus();
                        binding.progressBar2.setVisibility(View.INVISIBLE);
                    }
                }
                else {
                    binding.Username.setError("User does not exist..!!");
                    binding.Username.requestFocus();
                    binding.progressBar2.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}