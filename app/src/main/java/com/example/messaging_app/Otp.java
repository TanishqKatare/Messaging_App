package com.example.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity {
    String phone_number,mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    TextView verifytv,waitingTv,counterTv;
    EditText sentcodeEt;
    MaterialButton resentBtn,verificationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Intent thatstartedthis = getIntent();
        phone_number = thatstartedthis.getStringExtra("phone_number");
        verifytv= findViewById(R.id.verifyTv);
        waitingTv = findViewById(R.id.waitingTv);
        counterTv = findViewById(R.id.counterTv);
        sentcodeEt = findViewById(R.id.sentcodeEt);
        verificationBtn=findViewById(R.id.verificationBtn);
        String s= phone_number+"?";
        verifytv.setText( s);
        setSpannableString();
        resentBtn = findViewById(R.id.resendBtn);
       // showTimer(60000);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                //Log.d(TAG, "onVerificationCompleted:" + credential);

                //signInWithPhoneAuthCredential(credential);
                String smsCode = credential.getSmsCode();
                if(!(smsCode.isEmpty())) {
                    sentcodeEt.setText(smsCode);
                }
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
               // Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
               // Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
        verificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = sentcodeEt.getText().toString();
                if(!code.isEmpty() && !mVerificationId.isEmpty()){
                    Toast.makeText(Otp.this,"Please Wait",Toast.LENGTH_LONG).show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });
        resentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mResendToken!=null){
                    showTimer(60000);
                    Toast.makeText(Otp.this,"Please Wait",Toast.LENGTH_LONG).show();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(phone_number)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(Otp.this)                 // Activity (for callback binding)
                                    .setCallbacks(mCallbacks)
                                    .setForceResendingToken(mResendToken)
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }
        });
        verifyphonenumber();

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Otp.this,"Success",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Otp.this, SignUp.class);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(Otp.this,"Failed",Toast.LENGTH_LONG).show();

                    }
            }
        });
    }

    public void verifyphonenumber(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone_number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        showTimer(60000);
    }
    public void setSpannableString(){
        SpannableString sps= new SpannableString("Waiting to automatically detect a code sent to " + phone_number + ". Wrong Number?");
        ClickableSpan Wrong_Number = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent i  = new Intent( Otp.this, MainActivity.class );
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                //ds.isUnderlineText(false);
                ds.setColor(ds.linkColor);
                ds.isUnderlineText();

            }
        };
        sps.setSpan(Wrong_Number, sps.length()-13, sps.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        waitingTv.setMovementMethod(LinkMovementMethod.getInstance());
        waitingTv.setText(sps);
    }

    @Override
    public void onBackPressed() {
    }
    public void showTimer(long miliseconds){
        resentBtn.setEnabled(false);
        CountDownTimer ct = new CountDownTimer(miliseconds,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                counterTv.setVisibility(View.VISIBLE);
                long x= millisUntilFinished/1000;
                counterTv.setText("Time Remaining : " + Long.toString(x));



            }

            @Override
            public void onFinish() {
                    resentBtn.setEnabled(true);
                    counterTv.setVisibility(View.INVISIBLE);
            }
        };
        ct.start();

    }

}