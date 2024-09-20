package com.mountreachsolutionpvtltd.healthsphere;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

       bottomNavigationView = findViewById(R.id.homeBottomNevView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeBottomNevView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.homeMenuMyOffers){

        } else if (item.getItemId() == R.id.homeMenuMyCart) {

        } else if (item.getItemId() == R.id.homeMenuMyProfile) {
            Intent i = new Intent(HomeActivity.this,MyProfileActivity.class);
            startActivity(i);
        }
        return true;
    }

    HomeFragment homeFragment = new HomeFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    AccountFragment accountFragment = new AccountFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.homeButtonNevMenuHome){
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFramelayout,homeFragment).commit();
        } else if (item.getItemId() == R.id.homeButtonNevMenuCtegory) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFramelayout,categoryFragment).commit();
        } else if (item.getItemId() == R.id.homeButtonNevMenuAccount) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFramelayout,accountFragment).commit();
        }
        return true;
    }
}