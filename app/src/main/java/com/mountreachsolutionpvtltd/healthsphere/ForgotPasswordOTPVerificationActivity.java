package com.mountreachsolutionpvtltd.healthsphere;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ForgotPasswordOTPVerificationActivity extends AppCompatActivity {
    TextView tvResendOTP;
    EditText etOtp1,etOtp2,etOtp3,etOtp4,etOtp5,etOtp6;
    AppCompatButton btnVerify;
    ProgressDialog progressDialog;
    private String strVerificationCode,strMobileNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otpverification);
        tvResendOTP =findViewById(R.id.tvResendOTP);
        etOtp1=findViewById(R.id.etOtp1);
        etOtp2=findViewById(R.id.etOtp2);
        etOtp3=findViewById(R.id.etOtp3);
        etOtp4=findViewById(R.id.etOtp4);
        etOtp5=findViewById(R.id.etOtp5);
        etOtp6=findViewById(R.id.etOtp6);
        btnVerify=findViewById(R.id.btnVerify);

        strVerificationCode = getIntent().getStringExtra("verificationCode");

        strMobileNo = getIntent().getStringExtra("mobileno");


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etOtp1.getText().toString().trim().isEmpty() || etOtp2.getText().toString().trim().isEmpty() ||
                        etOtp3.getText().toString().trim().isEmpty() || etOtp4.getText().toString().trim().isEmpty()||
                        etOtp5.getText().toString().trim().isEmpty() || etOtp6.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(ForgotPasswordOTPVerificationActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                }
                String otpCode = etOtp1.getText().toString()+etOtp2.getText().toString()+etOtp3.getText().toString()+
                        etOtp4.getText().toString()+etOtp5.getText().toString()+etOtp6.getText().toString();

                if(strVerificationCode!=null){
                    progressDialog = new ProgressDialog(ForgotPasswordOTPVerificationActivity.this);
                    progressDialog.setTitle("Verifing OTP");
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(strVerificationCode,otpCode);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();

                                Intent i = new Intent(ForgotPasswordOTPVerificationActivity.this,SetupNewPasswordActivity.class);
                                i.putExtra("mobileno",strMobileNo);
                                startActivity(i);

                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(ForgotPasswordOTPVerificationActivity.this, "OTP Verification Failed", Toast.LENGTH_SHORT).show();
                            }
                        }


                    });
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPasswordOTPVerificationActivity.this, "OTP not received", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + strMobileNo, 60, TimeUnit.SECONDS, ForgotPasswordOTPVerificationActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        Toast.makeText(ForgotPasswordOTPVerificationActivity.this,"Verification Completed",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Toast.makeText(ForgotPasswordOTPVerificationActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String newVerificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        strVerificationCode = newVerificationCode;
                    }
                });
            }
        });




        setUpOTP();
    }
    private void setUpOTP(){
        etOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(! s.toString().trim().isEmpty()){
                    etOtp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(! s.toString().trim().isEmpty()){
                    etOtp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(! s.toString().trim().isEmpty()){
                    etOtp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(! s.toString().trim().isEmpty()){
                    etOtp5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etOtp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(! s.toString().trim().isEmpty()){
                    etOtp6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}