package com.mountreachsolutionpvtltd.healthsphere;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolutionpvtltd.healthsphere.Admin.AdminHomeActivity;
import com.mountreachsolutionpvtltd.healthsphere.DoctorRole.DoctorLoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    ImageView ivLoginLogo;
    TextView tvLoginTitle,tvNewUser,tvForgotPassword,tvDoctorRole;
    EditText etLoginUser,etLoginPass;
    CheckBox cbLoginShow;
    AppCompatButton btnLoginLogin;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    GoogleSignInOptions googleSignInOptions;//show opt on gmail
    GoogleSignInClient googleSignInClient;//selected gmail opt store
    AppCompatButton btnSignInWithGoogle;

    SharedPreferences preferences;//temp data store
    SharedPreferences.Editor editor;//data put sharedpreference
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor = preferences.edit();

        ivLoginLogo=findViewById(R.id.ivLoginLogo);
        tvLoginTitle=findViewById(R.id.tvLoginText);
        tvNewUser=findViewById(R.id.tvNewUser);
        etLoginUser=findViewById(R.id.etLoginUser);
        etLoginPass=findViewById(R.id.etLoginPass);
        tvForgotPassword=findViewById(R.id.tvForgotPassword);
        cbLoginShow=findViewById(R.id.cbLoginShow);
        btnLoginLogin=findViewById(R.id.btnLogin);
        tvDoctorRole=findViewById(R.id.tvDoctorRole);
        btnSignInWithGoogle=findViewById(R.id.acbtnLoginSigninWithGoogle);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);

        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }

        });

        tvDoctorRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, DoctorLoginActivity.class);
                startActivity(i);
            }
        });


        cbLoginShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    etLoginPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    etLoginPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etLoginUser.getText().toString().isEmpty()){
                    etLoginUser.setError("Please enter your user name");
                } else if (etLoginPass.getText().toString().isEmpty()) {
                    etLoginPass.setError("Please enter your user password");
                }else if (etLoginUser.getText().toString().length()<8) {
                    etLoginUser.setError("Username must be greater than 8 character");
                }else if (etLoginPass.getText().toString().length()<8) {
                    etLoginPass.setError("Password must be greater than 8 character");
                }else if (!etLoginUser.getText().toString().matches(".*[A-Z].*")) {
                    etLoginUser.setError(" Must be contain one uppercase letter");
                }else if (!etLoginUser.getText().toString().matches(".*[a-z].*")) {
                    etLoginUser.setError(" Must be contain one lowercase letter");
                }else if (!etLoginUser.getText().toString().matches(".*[0-9].*")) {
                    etLoginUser.setError(" Must be contain one digit");
                }else if (!etLoginUser.getText().toString().matches(".*[@,#,$,!,&].*")) {
                    etLoginUser.setError(" Must be contain one special symbol");
                }else if (!etLoginPass.getText().toString().matches(".*[A-Z].*")) {
                    etLoginPass.setError(" Must be contain one uppercase letter");
                }else if (!etLoginPass.getText().toString().matches(".*[a-z].*")) {
                    etLoginPass.setError(" Must be contain one lowercase letter");
                }else if (!etLoginPass.getText().toString().matches(".*[0-9].*")) {
                    etLoginPass.setError(" Must be contain one digit");
                }else if (!etLoginPass.getText().toString().matches(".*[@,#,$,!,&].*")) {
                    etLoginPass.setError(" Must be contain one special symbol");
                }else {
//                    Intent i = new Intent(LoginActivity.this,HomePageActivity.class);
//                    startActivity(i);
//                    Toast.makeText(LoginActivity.this, "Login Successfully Done", Toast.LENGTH_SHORT).show();

                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Login...");
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    userLogin();
                }
            }

        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ConfirmRegisterMobileNoActivity.class);
                startActivity(i);
            }
        });
        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });


    }
    public void userLogin(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",etLoginUser.getText().toString());
        params.put("password",etLoginPass.getText().toString());

        client.post(Urls.loginUserWebServices,params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("success");
                    String strUserrole = response.getString("userrole");
                    if(status.equals("1") && strUserrole.equals("user")){
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        editor.putString("username",etLoginUser.getText().toString()).commit();
                        startActivity(i);

                    } else if (status.equals("1") && strUserrole.equals("admin")) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        startActivity(i);

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Invalid Username and Password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Server Issue", Toast.LENGTH_SHORT).show();
            }

        });
    }



    private void signIn() {
        Intent i = googleSignInClient.getSignInIntent();
        startActivityForResult(i,999);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==999){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                editor.putString("username",etLoginUser.getText().toString()).commit();
                startActivity(i);
                finish();
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }
}