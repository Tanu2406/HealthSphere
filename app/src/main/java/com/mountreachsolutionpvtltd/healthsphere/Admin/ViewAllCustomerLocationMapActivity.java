package com.mountreachsolutionpvtltd.healthsphere.Admin;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolutionpvtltd.healthsphere.R;
import com.mountreachsolutionpvtltd.healthsphere.Urls;
import com.mountreachsolutionpvtltd.healthsphere.databinding.ActivityViewAllCustomerLocationMapBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ViewAllCustomerLocationMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityViewAllCustomerLocationMapBinding binding;
    double latitude,longitude;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAllCustomerLocationMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // ActivityCompat => check permission is granted or not and send request for permission
        if (ActivityCompat.checkSelfPermission(ViewAllCustomerLocationMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ViewAllCustomerLocationMapActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ViewAllCustomerLocationMapActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },101);
        }else {
            getMyCurrentLocation();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @SuppressLint("MissingPermission")
    private void getMyCurrentLocation() {
        // create FusedLocationProviderClient class to get the user current location
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ViewAllCustomerLocationMapActivity.this);
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
                Geocoder geocoder = new Geocoder(ViewAllCustomerLocationMapActivity.this);
                try {
                    List<Address>  addressList = geocoder.getFromLocation(latitude,longitude,1);
                    address = addressList.get(0).getAddressLine(0);
                    LatLng currentlocation = new LatLng(latitude,longitude);
                    mMap.addMarker(new MarkerOptions().position(currentlocation).title(address));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlocation,16),5000,null);
                    //move camera => animateCamera move all earth left to right and focus on user use newLatLngZoom method ofCameraUpdateFactory class for zoom camera => Movecamera only focus on user
                    Toast.makeText(ViewAllCustomerLocationMapActivity.this, address, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewAllCustomerLocationMapActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getAllCustomerLocation();
        // Add a marker in Sydney and move the camera

    }

    private void getAllCustomerLocation() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.getAllCustomerLocationWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray("getCustomerCurrentLocation");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strId = jsonObject.getString("id");
                        String strName = jsonObject.getString("name");
                        double strLatitude = Double.parseDouble(jsonObject.getString("latitude"));
                        double strLongitude = Double.parseDouble(jsonObject.getString("longitude"));
                        String strAddress = jsonObject.getString("address");
                        String strUsername = jsonObject.getString("username");

                        LatLng customerLocation = new LatLng(strLatitude,strLongitude);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(customerLocation).title(strAddress);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.userlocation));
                        mMap.addMarker(markerOptions);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ViewAllCustomerLocationMapActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}