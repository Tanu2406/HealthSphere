package com.mountreachsolutionpvtltd.healthsphere;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText etName,etMobileNo,etEmailId,etUsername;
    AppCompatButton btnUpdaterofile;
    ProgressDialog progressDialog;

    String strName,strMobileNo,strEmailId,strUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        etName=findViewById(R.id.etUpdateProfilrName);
        etMobileNo=findViewById(R.id.etUpdateProfilrMobileNo);
        etEmailId=findViewById(R.id.etUpdateProfilrEmail);
        etUsername=findViewById(R.id.etUpdateProfileUser);

        btnUpdaterofile=findViewById(R.id.acbtnUpdateProfile);

        strName = getIntent().getStringExtra("name");
        strMobileNo = getIntent().getStringExtra("mobileno");
        strEmailId = getIntent().getStringExtra("emailid");
        strUsername = getIntent().getStringExtra("username");

        etName.setText(strName);
        etMobileNo.setText(strMobileNo);
        etEmailId.setText(strEmailId);
        etUsername.setText(strUsername);

        btnUpdaterofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(UpdateProfileActivity.this);
                progressDialog.setTitle("Update Profile");
                progressDialog.setMessage("Please Wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                updateProfile();
            }
        });


    }

    private void updateProfile() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name",etName.getText().toString());
        params.put("mobileno",etMobileNo.getText().toString());
        params.put("emailid",etEmailId.getText().toString());
        params.put("username",etUsername.getText().toString());

        client.post(Urls.UpdateProfileWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("success");

                    if (status.equals("1")) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this, "Profile Update Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateProfileActivity.this,MyProfileActivity.class);
                        startActivity(i);
                    }else {
                        Toast.makeText(UpdateProfileActivity.this, "Update Not Done", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}