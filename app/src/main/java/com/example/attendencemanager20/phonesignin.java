package com.example.attendencemanager20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phonesignin extends AppCompatActivity {

    EditText phonenumber,otp;
    Button signup, verify;
    FirebaseAuth mAuth;
    LottieAnimationView anim;

    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonesignin);

        signup=findViewById(R.id.signup);
        otp=findViewById(R.id.opt);
        anim = findViewById(R.id.anim);
        anim.setVisibility(View.INVISIBLE);

        phonenumber=findViewById(R.id.phone_no);

        verify=findViewById(R.id.verify);
        mAuth=FirebaseAuth.getInstance();


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String votp=otp.getText().toString();
                verify.setVisibility(View.INVISIBLE);
                anim.setVisibility(View.VISIBLE);
                Handler handle=new Handler();
                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, votp);
                        signInWithPhoneAuthCredential(credential);
                    }
                },2000);
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber="+91"+phonenumber.getText().toString();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        phonesignin.this,               // Activity (for callback binding)
                        mCallbacks
                );

                // String vcode=otp.getText().toString();
                //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, vcode);
            }
        });



        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(getApplicationContext(),"Error in Verfication",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull final String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;
                // Save verification ID and resending token so we can use them later
            }


        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser=mAuth.getCurrentUser();
        if(currentuser!=null){
            Intent user=new Intent(phonesignin.this,MainActivity.class);
            startActivity(user);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            FirebaseUser user = task.getResult().getUser();

                            Intent intent=new Intent(phonesignin.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getApplicationContext(),"Sign In Failed",Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    void loadAnimation()
    {

    }
}
