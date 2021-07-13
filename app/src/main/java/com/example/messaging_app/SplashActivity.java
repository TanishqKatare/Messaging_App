package com.example.messaging_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Auth = FirebaseAuth.getInstance();
       // Toast.makeText(SplashActivity.this, Auth.getCurrentUser().toString() , Toast.LENGTH_LONG).show();
        if(Auth.getCurrentUser() == null){
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
        }else{
            Intent i = new Intent(SplashActivity.this, login.class);
            startActivity(i);
        }
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
    }
}