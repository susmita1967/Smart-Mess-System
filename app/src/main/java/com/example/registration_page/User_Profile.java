
package com.example.registration_page;

import static com.example.registration_page.EncryptDecrypt.encrypt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.registration_page.databinding.ActivityUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class User_Profile extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    String pg4;
    String cl, rn, pass, phn, cnp,urlprofimg,img;
    Bitmap bitmap;
    FirebaseDatabase db;
    String encryptconpassword,encryptedtpassword;
    DatabaseReference reference;
    FirebaseStorage storage;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar12.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        pg4 = intent.getStringExtra("pg2");
        img=intent.getStringExtra("urlimg");
        db=FirebaseDatabase.getInstance();
        reference=db.getReference("Students");
       if(pg4!=null)
       {
           reference.child(pg4).addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot.child("profileImageUrl").exists())
                   {
                       String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                       // Use an image loading library like Glide or Picasso to load the image into the ImageView
                       Glide.with(User_Profile.this)
                               .load(profileImageUrl)
                               .into(binding.imageView4);
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
       }
       else
       {
           Toast.makeText(User_Profile.this, "id is null", Toast.LENGTH_SHORT).show();
       }
        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();

        // Initialize the ActivityResultLauncher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        binding.imageView4.setVisibility(View.INVISIBLE);
                        binding.profileImage.setImageBitmap(bitmap);

                        // Upload the image to Firebase Storage
                        uploadImageToFirebaseStorage();
                    } catch (IOException e) {
                        binding.imageView4.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                }
            }
        });

        binding.textView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // binding.textView15.setVisibility(View.INVISIBLE);
                openImagePicker();
            }
        });

        binding.updateprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar12.setVisibility(View.VISIBLE);
                cl=binding.updateclass.getText().toString();
                phn=binding.updatephone.getText().toString();
                rn=binding.updateroom.getText().toString();
                pass=binding.updatepass.getText().toString();
                cnp=binding.updateconpass.getText().toString();
                db=FirebaseDatabase.getInstance();
                reference=db.getReference("Students");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       /* Intent intent2=new Intent(User_Profile.this,Main_Page.class);
                        intent2.putExtra("img",bitmap);*/
                        if (snapshot.child(pg4).exists()) {
                            if(!pass.isEmpty())
                            {
                                if(!cnp.isEmpty())
                                {
                                    //if(!(pass.length()<8 || pass.length()>16)&&!(cnp.length()<8||cnp.length()>16))
                                     //   {
                                        if(pass.equals(cnp))
                                        {
                                            try {
                                                encryptedtpassword= encrypt(binding.updatepass.getText().toString());
                                            }catch (NoSuchPaddingException | NoSuchAlgorithmException |
                                                    InvalidAlgorithmParameterException | InvalidKeyException |
                                                    IllegalBlockSizeException | BadPaddingException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                encryptconpassword=encrypt(binding.updateconpass.getText().toString());
                                            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                                                e.printStackTrace();
                                            }
                                            reference.child(pg4).child("p").setValue(encryptedtpassword);
                                            reference.child(pg4).child("cp").setValue(encryptconpassword);
                                        }
                                        else
                                        {
                                            binding.updateconpass.requestFocus();
                                            binding.updateconpass.setError("Password doesn't matches");

                                        }

                                }
                                else
                                {
                                    binding.updateconpass.requestFocus();
                                    binding.updateconpass.setError("Confirm Password field cannot be empty ..!!");
                                }
                            }
                            if(!cl.isEmpty())
                            {
                                reference.child(pg4).child("cls").setValue(cl);
                            }
                            // The user exists in the database
                            if(!rn.isEmpty())
                            {
                                reference.child(pg4).child("rm").setValue(rn);
                            }

                            if(!phn.isEmpty())
                            {
                                if(phn.length()!=10)
                                {
                                    binding.updatephone.requestFocus();
                                    binding.updatephone.setError("Phone number must be of length 10..!");
                                }
                                else
                                {
                                    char y=phn.charAt(0);
                                    if (y=='0')
                                    {
                                        binding.updatephone.requestFocus();
                                        binding.updatephone.setError("Phone number does not start with 0..!");
                                    }
                                    Pattern pattern=Pattern.compile(new String("^\\d{10}$"));
                                    Matcher matcher=pattern.matcher(phn);
                                    if(!matcher.matches())
                                    {
                                        binding.updatephone.requestFocus();
                                        binding.updatephone.setError("Phone number is not valid..!");
                                    }
                                }
                                reference.child(pg4).child("phn").setValue(phn);
                            }
                            binding.progressBar12.setVisibility(View.INVISIBLE);
                            String username1 = getIntent().getStringExtra("username");
                            if(urlprofimg!=null)
                            {
                                img=urlprofimg;
                            }
                            if(username1!=null || img!=null) {//and ch or kelay
                                Intent intent11 = new Intent(User_Profile.this, Main_Page.class);
                                intent11.putExtra("username", username1); // Pass the username back to Main_Page
                                intent11.putExtra("urlimg", img);
                                Toast.makeText(User_Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                startActivity(intent11);
                            }
                            else
                            {
                                Toast.makeText(User_Profile.this, "please upload profile photo..!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // The user doesn't exist in the database
                            Toast.makeText(User_Profile.this, "User not found in the database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors
                        Toast.makeText(User_Profile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // Start the image picker using the launcher
        imagePickerLauncher.launch(intent);
    }

    private void uploadImageToFirebaseStorage() {
        String filename = UUID.randomUUID().toString();
        StorageReference storageReference = storage.getReference().child("profile_images").child(filename);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        storeImageUrlInDatabase(downloadUri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void storeImageUrlInDatabase(String imageUrl) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Students").child(pg4);
        userRef.child("profileImageUrl").setValue(imageUrl);
        urlprofimg=imageUrl;
        Toast.makeText(User_Profile.this, "Image uploaded...", Toast.LENGTH_SHORT).show();
    }
}