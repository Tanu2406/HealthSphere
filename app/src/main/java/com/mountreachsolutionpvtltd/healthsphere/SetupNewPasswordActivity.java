package com.mountreachsolutionpvtltd.healthsphere;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SetupNewPasswordActivity extends AppCompatActivity {
EditText etnewPassword,etConfirmPassword;
AppCompatButton acbtnSetPasssword;
ProgressDialog progressDialog;
String strMobileNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup_new_password);

        etnewPassword = findViewById(R.id.etSetupPasswordNewPassword);
        etConfirmPassword = findViewById(R.id.etSetupPasswordConfirmPassword);
        acbtnSetPasssword = findViewById(R.id.acbtnSetupPassword);
        strMobileNo=getIntent().getStringExtra("mobileno");

 acbtnSetPasssword.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         if(etnewPassword.getText().toString().isEmpty()){
             etnewPassword.setError("Please enter a new password");
         }else if(etConfirmPassword.getText().toString().isEmpty()){
             etConfirmPassword.setError("Please enter a confirm password");
         } else if (etnewPassword.getText().toString().length() < 8) {
             etnewPassword.setError("password should be greater than 8 characters.");
         }else if (etConfirmPassword.getText().toString().length() < 8) {
             etConfirmPassword.setError("password should be greater than 8 characters.");
         } else if (!etnewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
             etConfirmPassword.setError("Password does not exist");
         }else {
             progressDialog = new ProgressDialog(SetupNewPasswordActivity.this);
             progressDialog.setTitle("Changing Password");
             progressDialog.setMessage("Please Wait...");
             progressDialog.setCanceledOnTouchOutside(false);
             progressDialog.show();
             
             forgetPassword();
         }
     }

     private void forgetPassword() {
         AsyncHttpClient client = new AsyncHttpClient();
         RequestParams params = new RequestParams();

         params.put("mobileno",strMobileNo);
         params.put("password",etnewPassword.getText().toString());

         client.post(Urls.forgetPasswordWebServices,params,new JsonHttpResponseHandler(){
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 super.onSuccess(statusCode, headers, response);
                 try {
                     String status = response.getString("success");
                     if(status.equals("1")){
                         progressDialog.dismiss();
                         Toast.makeText(SetupNewPasswordActivity.this, "Password Change Successfully", Toast.LENGTH_SHORT).show();
                         Intent i = new Intent(SetupNewPasswordActivity.this,LoginActivity.class);
                         startActivity(i);
                     }else {
                         progressDialog.dismiss();
                         Toast.makeText(SetupNewPasswordActivity.this, "Password does not change", Toast.LENGTH_SHORT).show();
                     }
                 } catch (JSONException e) {
                     throw new RuntimeException(e);
                 }
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                 super.onFailure(statusCode, headers, throwable, errorResponse);
                 progressDialog.show();
                 Toast.makeText(SetupNewPasswordActivity.this, "Server Issue", Toast.LENGTH_SHORT).show();
             }
         });
     }
 });

    }
}