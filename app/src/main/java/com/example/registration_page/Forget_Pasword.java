package com.example.registration_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.registration_page.databinding.ActivityForgetPaswordBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Forget_Pasword extends AppCompatActivity {

    FirebaseAuth Fauth;
    //private String phnNum, enteredOTP;
    String userphn;
    DatabaseReference reference;

    private ProgressBar progressBar;
    String enteredgrn,enteredphn;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    String verificationId;
    ActivityForgetPaswordBinding binding;
    PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgetPaswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
      //  getSupportActionBar().setTitle("Foreget Password");
        binding.progressBar.setVisibility(View.INVISIBLE);
        Fauth = FirebaseAuth.getInstance();
        binding.resendotpbtn.setEnabled(false);

        binding.progressBar.setVisibility(View.INVISIBLE);

        binding.otpbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                if (binding.regphn.getText().toString().length() != 13) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    binding.regphn.setError("Please enter phone number with country code +91..!");
                    binding.regphn.requestFocus();
                } else
                {
                    enteredgrn = binding.regrn.getText().toString();
                    enteredphn = binding.regphn.getText().toString();
                    /*if(!enteredgrn.isEmpty()&&!enteredphn.isEmpty())
                    {
                        binding.progressBar.setVisibility(View.VISIBLE);
                    }*/
                    reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smart-mess-e2e8a-default-rtdb.firebaseio.com/");//
                    reference.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(enteredgrn))//.exists())//.hasChild(loginusername))
                            {
                                binding.regrn.setError(null);
                                final String phoneFromDB = snapshot.child(enteredgrn).child("phn").getValue(String.class);
                                String entph = enteredphn.substring(3);
                                if (phoneFromDB.equals(entph)) {
                                    binding.progressBar.setVisibility(View.VISIBLE);
                                    binding.regrn.setError(null);
                                    Toast.makeText(Forget_Pasword.this, "User Exists..!!", Toast.LENGTH_SHORT).show();
                                    userphn=binding.regphn.getText().toString();
                                    verifyPhone(userphn);
                                } else {
                                    //binding.progressBar.setVisibility(View.GONE);
                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                    binding.regphn.setError("Phone number does not match with GRN..!!");
                                    binding.regphn.requestFocus();
                                }
                            } else {
                               // binding.progressBar.setVisibility(View.GONE);
                                binding.progressBar.setVisibility(View.INVISIBLE);
                                binding.regrn.setError("User does not exist..!!");
                                binding.regrn.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                if (binding.regphn.getText().toString().length()!=13) {
                    binding.regphn.setError("Please enter valid  registered phone number..!");
                    binding.regphn.requestFocus();
                }
                userphn=binding.regphn.getText().toString();
                verifyPhone(userphn);
                binding.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
       binding.resendotpbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               binding.progressBar.setVisibility(View.VISIBLE);
               verifyPhone(userphn);
               binding.progressBar.setVisibility(View.INVISIBLE);
               binding.resendotpbtn.setEnabled(false);
           }
       });
        binding.verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  enteredOTP = binding.OTP.getText().toString();
                if (binding.OTP.getText().toString().isEmpty()) {
                   // binding.progressBar.setVisibility(View.GONE);
                    binding.OTP.setError("Please enter OTP first..!");
                    binding.OTP.requestFocus();
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, binding.OTP.getText().toString());
                    authenticateUser(credential);
                }
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                authenticateUser(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(Forget_Pasword.this,"Please enter phone number '+91--'", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
               binding.progressBar.setVisibility(View.INVISIBLE);
                super.onCodeSent(s, forceResendingToken);

                verificationId = s;
                token=forceResendingToken;

                binding.regrn.setVisibility(View.GONE);
                binding.regphn.setVisibility(View.GONE);
                binding.otpbtn2.setVisibility(View.GONE);

                binding.OTP.setVisibility(View.VISIBLE);
                binding.verifybtn.setVisibility(View.VISIBLE);
                binding.resendotpbtn.setVisibility(View.VISIBLE);
                binding.resendotpbtn.setEnabled(false);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                binding.resendotpbtn.setEnabled(true);
            }
        };


    }

    public void verifyPhone(String phnnum) {
        // Send OTP
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(Fauth)
                .setActivity(this)
                .setPhoneNumber(phnnum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void authenticateUser(PhoneAuthCredential credential) {
        Fauth.signInWithCredential(credential)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                      // startActivity(new Intent(getApplicationContext(), New_Password.class));
                        Log.i("Info","Here it stops");
                        String g=binding.regrn.getText().toString();
                      // Intent intent=new Intent(getApplicationContext(), New_Password.class);
                        Intent intent=new Intent(Forget_Pasword.this, New_Password.class);
                        intent.putExtra("grn_fetch",g);
                        startActivity(intent);///----Here i have made changes*/
                        Toast.makeText(Forget_Pasword.this, "Successfully Verified...!!", Toast.LENGTH_SHORT).show();
                        Log.i("Info","Here it stops1");
                        finish();
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Forget_Pasword.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}