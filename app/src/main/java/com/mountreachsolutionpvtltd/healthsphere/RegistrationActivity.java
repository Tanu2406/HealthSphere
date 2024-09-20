package com.mountreachsolutionpvtltd.healthsphere;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    TextView tvRegisterTitle;
    ImageView tvRegisterLogo;
    EditText etRegisterName,etRegisterMobileNo,etRegisterEmail,etRegisterUser,etRegisterPass;
    CheckBox cbRegisterShowPass;
    AppCompatButton btnregister;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        tvRegisterTitle=findViewById(R.id.tvRegisterText);
        etRegisterName=findViewById(R.id.etRegisterName);
        etRegisterMobileNo=findViewById(R.id.etRegisterMobileNo);
        etRegisterEmail=findViewById(R.id.etRegisterEmail);
        etRegisterUser=findViewById(R.id.etRegisterUser);
        etRegisterPass=findViewById(R.id.etRegisterPass);
        cbRegisterShowPass=findViewById(R.id.cbRegisterShow);
        btnregister=findViewById(R.id.btnRegister);

        cbRegisterShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    etRegisterPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    etRegisterPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etRegisterName.getText().toString().isEmpty()){
                    etRegisterName.setError("Please enter your name");
                }else if(etRegisterUser.getText().toString().isEmpty()){
                    etRegisterUser.setError("Please enter your user name");
                } else if (etRegisterMobileNo.getText().toString().length()!=10) {
                    etRegisterMobileNo.setError("Please enter valid mobile number");
                } else if (etRegisterPass.getText().toString().isEmpty()) {
                    etRegisterName.setError("Please enter your password");
                } else if (!etRegisterEmail.getText().toString().contains("@")&&!etRegisterEmail.getText().toString().contains(".com")) {
                    etRegisterEmail.setError("Please enter valid email id");
                } else if (etRegisterUser.getText().toString().length()<8) {
                    etRegisterUser.setError("User name must be greater than 8 character");
                }else if (etRegisterPass.getText().toString().length()<8) {
                    etRegisterPass.setError("Password must be greater than 8 character");
                } else if (!etRegisterUser.getText().toString().matches(".*[A-Z].*")) {
                    etRegisterUser.setError("Must be contain one uppercase letter");
                }else if (!etRegisterUser.getText().toString().matches(".*[a-z].*")) {
                    etRegisterUser.setError("Must be contain one lowercase letter");
                }else if (!etRegisterUser.getText().toString().matches(".*[0-9].*")) {
                    etRegisterUser.setError("Must be contain one digit");
                }else if (!etRegisterUser.getText().toString().matches(".*[@,#,$,&,!].*")) {
                    etRegisterUser.setError("Must be contain one special symbol");
                } else if (!etRegisterPass.getText().toString().matches(".*[A-Z].*")) {
                    etRegisterPass.setError("Must be contain one uppercase letter");
                }else if (!etRegisterPass.getText().toString().matches(".*[a-z].*")) {
                    etRegisterPass.setError("Must be contain one lowercase letter");
                }else if (!etRegisterPass.getText().toString().matches(".*[0-9].*")) {
                    etRegisterPass.setError("Must be contain one digit");
                }else if (!etRegisterPass.getText().toString().matches(".*[@,#,$,&,!].*")) {
                    etRegisterPass.setError("Must be contain one special symbol");
                }else {
                    progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setTitle("Registering...");
                    progressDialog.setMessage("Please wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    //Verify mobile number
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + etRegisterMobileNo.getText().toString(), 60, TimeUnit.SECONDS, RegistrationActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this,"Verification Successful",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            Intent i = new Intent(RegistrationActivity.this,OTPVerificationActivity.class);
                            i.putExtra("verificationCode",verificationCode);
                            i.putExtra("name",etRegisterEmail.getText().toString());
                            i.putExtra("mobileno",etRegisterMobileNo.getText().toString());
                            i.putExtra("emailid",etRegisterEmail.getText().toString());
                            i.putExtra("username",etRegisterUser.getText().toString());
                            i.putExtra("password",etRegisterPass.getText().toString());
                            startActivity(i);
                        }
                    });
                }
            }



        });

    }
}