package com.mountreachsolutionpvtltd.healthsphere;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class OTPVerificationActivity extends AppCompatActivity {
    TextView tvMobileNo, tvResendOTP;
    EditText etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6;
    AppCompatButton btnVerify;
    ProgressDialog progressDialog;
    private String strVerificationCode, strName, strMobileNo, strEmailId, strUsername, strPassword;

    double latitude, longitude;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        if (ActivityCompat.checkSelfPermission(OTPVerificationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(OTPVerificationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OTPVerificationActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 199);
      }
//        else {
//            getUserCurrentLocation();
//        }

        tvResendOTP = findViewById(R.id.tvResendOTP);
        etOtp1 = findViewById(R.id.etOtp1);
        etOtp2 = findViewById(R.id.etOtp2);
        etOtp3 = findViewById(R.id.etOtp3);
        etOtp4 = findViewById(R.id.etOtp4);
        etOtp5 = findViewById(R.id.etOtp5);
        etOtp6 = findViewById(R.id.etOtp6);
        btnVerify = findViewById(R.id.btnVerify);

        strVerificationCode = getIntent().getStringExtra("verificationCode");
        strName = getIntent().getStringExtra("name");
        strMobileNo = getIntent().getStringExtra("mobileno");
        strEmailId = getIntent().getStringExtra("emailid");
        strUsername = getIntent().getStringExtra("username");
        strPassword = getIntent().getStringExtra("password");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOtp1.getText().toString().trim().isEmpty() || etOtp2.getText().toString().trim().isEmpty() ||
                        etOtp3.getText().toString().trim().isEmpty() || etOtp4.getText().toString().trim().isEmpty() ||
                        etOtp5.getText().toString().trim().isEmpty() || etOtp6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OTPVerificationActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                }
                String otpCode = etOtp1.getText().toString() + etOtp2.getText().toString() + etOtp3.getText().toString() +
                        etOtp4.getText().toString() + etOtp5.getText().toString() + etOtp6.getText().toString();

                if (strVerificationCode != null) {
                    progressDialog = new ProgressDialog(OTPVerificationActivity.this);
                    progressDialog.setTitle("Verifing OTP");
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(strVerificationCode, otpCode);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                getUserCurrentLocation();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(OTPVerificationActivity.this, "OTP Verification Failed", Toast.LENGTH_SHORT).show();
                            }
                        }


                    });
                } else {
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

                        Toast.makeText(OTPVerificationActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Toast.makeText(OTPVerificationActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
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

    private void getUserCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(OTPVerificationActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Geocoder geocoder = new Geocoder(OTPVerificationActivity.this);
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
                    address = addressList.get(0).getAddressLine(0);
                    userRegister(latitude,longitude,address);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OTPVerificationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void userRegister(double latitude,double longitude,String address){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name",strName);
        params.put("mobileno",strMobileNo);
        params.put("emailid",strEmailId);
        params.put("username",strUsername);
        params.put("password",strPassword);
        params.put("latitude",latitude);
        params.put("longitude",longitude);
        params.put("address",address);

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