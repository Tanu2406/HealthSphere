package com.mountreachsolutionpvtltd.healthsphere.Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mountreachsolutionpvtltd.healthsphere.R;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_home);
        Toast.makeText(this, "Admin", Toast.LENGTH_SHORT).show();
    }
}