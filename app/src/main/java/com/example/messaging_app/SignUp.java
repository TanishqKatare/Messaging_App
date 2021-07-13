package com.example.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.Permission;
import java.util.concurrent.Executor;

public class SignUp extends AppCompatActivity {
    ShapeableImageView UserImg;
    FirebaseStorage storage;
    FirebaseAuth Auth;
    String downloadUrl;
    FirebaseFirestore UserDB;
    EditText Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        UserImg= findViewById(R.id.userImgView);
        storage= FirebaseStorage.getInstance();
        MaterialButton nextbtn =  findViewById(R.id.nextBtn);
        Name= findViewById(R.id.nameEt);
        Auth = FirebaseAuth.getInstance();
        UserDB= FirebaseFirestore.getInstance();
        UserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermissions();
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextbtn.setEnabled(false);
                String EnteredName = Name.getText().toString();
                if(EnteredName.isEmpty()){
                    Toast.makeText(SignUp.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
                }
                else if(downloadUrl.isEmpty()){
                    Toast.makeText(SignUp.this, "Image cannot be empty", Toast.LENGTH_LONG).show();
                }
                else{
                    User user1= new User(EnteredName, downloadUrl, downloadUrl, Auth.getUid().toString());
                    UserDB.collection("users").document(Auth.getUid().toString()).set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                                Intent i = new Intent(SignUp.this, login.class);
                                startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                                nextbtn.setEnabled(true);
                        }
                    });
                }

            }
        });
    }
    public void CheckPermissions(){
        if (ActivityCompat.checkSelfPermission(SignUp.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }else{
            pickimagefromgallery();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(galleryIntent, 1001);
                    pickimagefromgallery();
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                    Toast.makeText(SignUp.this,"Please Allow the permission to view the gallery.",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    public void pickimagefromgallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1000);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK && requestCode==1000){
            final Uri SelectedImgUri = data.getData();
            if(SelectedImgUri!=null){
                String path= SelectedImgUri.getPath();
                //Log.i(TAG, "Image Path : " + path);
                // Set the image in ImageView
                    UserImg.post(new Runnable() {
                    @Override
                    public void run() {
                        UserImg.setImageURI(SelectedImgUri);
                        uploadImage(SelectedImgUri);
                    }
                });
            }
        }
    }
    public void uploadImage(Uri ImgUri){
        MaterialButton nextbtn =  findViewById(R.id.nextBtn);
        nextbtn.setEnabled(false);
        StorageReference ref = storage.getReference().child("upload/"+ Auth.getUid().toString());
        UploadTask uploadtask = ref.putFile(ImgUri);
        Task<Uri> urlTask = uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUrl = task.getResult().toString();
                    nextbtn.setEnabled(true);
                } else {
                    // Handle failures
                    // ...
                    nextbtn.setEnabled(true);
                    Toast.makeText(SignUp.this,"Check your Internet Connection",Toast.LENGTH_LONG ).show();
                }
            }
        });
    }


}
    /*public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }



}*/