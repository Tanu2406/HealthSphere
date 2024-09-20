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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class OTPVerificationActivity extends AppCompatActivity {
    TextView tvMobileNo,tvResendOTP;
EditText etOtp1,etOtp2,etOtp3,etOtp4,etOtp5,etOtp6;
AppCompatButton btnVerify;
ProgressDialog progressDialog;
private String strVerificationCode,strName,strMobileNo,strEmailId,strUsername,strPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otpverification);
        tvResendOTP=findViewById(R.id.tvResendOTP);
        etOtp1=findViewById(R.id.etOtp1);
        etOtp2=findViewById(R.id.etOtp2);
        etOtp3=findViewById(R.id.etOtp3);
        etOtp4=findViewById(R.id.etOtp4);
        etOtp5=findViewById(R.id.etOtp5);
        etOtp6=findViewById(R.id.etOtp6);
        btnVerify=findViewById(R.id.btnVerify);

        strVerificationCode = getIntent().getStringExtra("verificationCode");
        strName = getIntent().getStringExtra("name");
        strMobileNo = getIntent().getStringExtra("mobileno");
        strEmailId = getIntent().getStringExtra("emailid");
        strUsername = getIntent().getStringExtra("username");
        strPassword = getIntent().getStringExtra("password");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etOtp1.getText().toString().trim().isEmpty() || etOtp2.getText().toString().trim().isEmpty() ||
                etOtp3.getText().toString().trim().isEmpty() || etOtp4.getText().toString().trim().isEmpty()||
                etOtp5.getText().toString().trim().isEmpty() || etOtp6.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(OTPVerificationActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                }
                String otpCode = etOtp1.getText().toString()+etOtp2.getText().toString()+etOtp3.getText().toString()+
                          etOtp4.getText().toString()+etOtp5.getText().toString()+etOtp6.getText().toString();

                if(strVerificationCode!=null){
                    progressDialog = new ProgressDialog(OTPVerificationActivity.this);
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
                                userRegister();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(OTPVerificationActivity.this, "OTP Verification Failed", Toast.LENGTH_SHORT).show();
                            }
                        }


                    });
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(OTPVerificationActivity.this, "OTP not received", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + strMobileNo, 60, TimeUnit.SECONDS, OTPVerificationActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        Toast.makeText(OTPVerificationActivity.this,"Verification Successful",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Toast.makeText(OTPVerificationActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
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
    public void userRegister(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name",strName);
        params.put("mobileno",strMobileNo);
        params.put("emailid",strEmailId);
        params.put("username",strUsername);
        params.put("password",strPassword);

        client.post(Urls.registerUserWebServices,params,new JsonHttpResponseHandler(){

            public void onSuccess(int  statusCode, Header[] headers, JSONObject response){
                super.onSuccess(statusCode,headers,response);
                try{
                    String status = response.getString("success");
                    if (status.equals("1")){
                        progressDialog.dismiss();
                        Toast.makeText(OTPVerificationActivity.this,"Registration Successfully Done!",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(OTPVerificationActivity.this,LoginActivity.class);
                        startActivity(i);

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(OTPVerificationActivity.this,"Already data exist",Toast.LENGTH_SHORT).show();

                    }}catch(JSONException e){
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(OTPVerificationActivity.this,"Could not connect",Toast.LENGTH_SHORT).show();

            }


        });
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