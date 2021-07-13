package com.example.messaging_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {
    EditText enteredNumber;
    Button next;
    String ph,country_code;
    CountryCodePicker CCP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enteredNumber= findViewById(R.id.phoneNumber);
        next= findViewById(R.id.nextBtn);
        enteredNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        next.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(enteredNumber.length() >= 10 ){
                            next.setEnabled(true);
                        }
                        else next.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(enteredNumber.length() >= 10 ){
                    next.setEnabled(true);
                }
                else next.setEnabled(false);
            }

        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumber();
            }
        });

    }
    public void checkNumber(){
        CCP = findViewById(R.id.ccp);
        country_code = CCP.getSelectedCountryCodeWithPlus();
        ph = country_code + enteredNumber.getText().toString();
        notifyuser();
    }
    public void notifyuser(){
       MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
       builder.setTitle("Confirm?");
       builder.setMessage("We will be verifying the mobile number you entered. Are you sure you want to proceed ?");
       builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
                OtpActivity();
           }
       });
       builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
           }
       });
       builder.show();

    }
    public void OtpActivity(){
        Intent i  = new Intent( MainActivity.this, Otp.class );
        i.putExtra("phone_number",ph);
        startActivity(i);
    }

}