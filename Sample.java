package com.example.Herald;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Sample extends AppCompatActivity {

    FirebaseAuth auth;

    String id=FirebaseAuth.getInstance().getUid();
    public static final String my="MYPREF";
    // PhoneAuthCredential credential;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String verification_code;

    EditText otpnum,phone;
    Button verifybut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        otpnum = (EditText) findViewById(R.id.otpnum);
        phone=(EditText)findViewById(R.id.phone);
        verifybut = (Button) findViewById(R.id.verifybut);
        auth=FirebaseAuth.getInstance();
        mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(), "Some error", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification_code=s;
                Toast.makeText(getApplicationContext(), "Code sended", Toast.LENGTH_SHORT).show();
            }
        };
    }
    public void send_sms(View v)
    {
        String phno=phone.getText().toString();
        int phnolen=phno.length();
        if(phnolen<10)
        {
            Toast.makeText(getApplicationContext(),"Enter perfect number",Toast.LENGTH_SHORT).show();
        }
        else {
            phno="+91"+phno;
            PhoneAuthProvider.getInstance().verifyPhoneNumber(phno, 60, TimeUnit.SECONDS, this, mCallback);
        }
    }
    public void verifyy(View v)
    {
        String input_code=otpnum.getText().toString();
        if(verification_code!=null) {
            verifyPhoneNumbers(verification_code, input_code);
        }
        else if(verification_code==null&&verification_code.length()<6)
        {
            Toast.makeText(getApplicationContext(), "Enter 6 digit OTP", Toast.LENGTH_SHORT).show();
        }
    }
    private void verifyPhoneNumbers(String verifyCode,String input_code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyCode, input_code);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Verification Failed, Invalid credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
