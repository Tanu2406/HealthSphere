package com.mountreachsolutionpvtltd.healthsphere;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyProfileActivity extends  AppCompatActivity {
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    ImageView ivProfilePhoto;
    TextView tvName,tvMobileNo,tvEmail,tvUsername;
    AppCompatButton btnEditProfile,btnSignOut;

    SharedPreferences preferences;
    
    String strUsername;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(MyProfileActivity.this);
        
        strUsername=preferences.getString("username","");
        
        setContentView(R.layout.activity_my_profile);
        ivProfilePhoto=findViewById(R.id.ivMyProfilePhoto);
        btnEditProfile=findViewById(R.id.acbtnMyProfileEditProfile);
        tvName=findViewById(R.id.tvMyProfileName);
        tvMobileNo=findViewById(R.id.tvMyProfileMobileNo);
        tvEmail=findViewById(R.id.tvMyProfileEmailId);
        tvUsername=findViewById(R.id.tvMyProfileUsername);
        btnSignOut=findViewById(R.id.acbtnMyProfileSignOut);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(MyProfileActivity.this,googleSignInOptions);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(MyProfileActivity.this);
        if(googleSignInAccount!=null){
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();
            tvName.setText(name);
            tvEmail.setText(email);
            btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent i = new Intent(MyProfileActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                }
            });

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(MyProfileActivity.this);
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        
        getMyDetails();
    }

    private void getMyDetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",strUsername);
        client.post(Urls.myDetailsWebService,params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();


                try {
                    JSONArray jsonArray = response.getJSONArray("getMyDetails");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String strId = jsonObject.getString("id");
                        String strImage = jsonObject.getString("image");
                         String strName = jsonObject.getString("name");
                        String strMobileno = jsonObject.getString("mobileno");
                        String strEmailid = jsonObject.getString("emailid");
                        String strUsername = jsonObject.getString("username");

                        tvName.setText(strName);
                        tvMobileNo.setText(strMobileno);
                        tvEmail.setText(strEmailid);
                        tvUsername.setText(strUsername);

                        Glide.with(MyProfileActivity.this)
                                .load("http://192.168.152.74:80/UserAPI/images/"+strImage)
                                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk caching
                                .skipMemoryCache(true)// Disable memory caching
                                .placeholder(R.drawable.icon_home_account_)
                                .error(R.drawable.icon_profile_photo)
                                .into(ivProfilePhoto);

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(MyProfileActivity.this, "Server Issue", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
