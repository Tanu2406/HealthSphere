package com.mountreachsolutionpvtltd.healthsphere.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolutionpvtltd.healthsphere.Admin.AdapterClass.AdapterGetAllCategoryDetailsRV;
import com.mountreachsolutionpvtltd.healthsphere.POJOGetAllCategoryDetails;
import com.mountreachsolutionpvtltd.healthsphere.POJOCategorywiseProduct;
import com.mountreachsolutionpvtltd.healthsphere.POJOGetAllCategoryDetails;
import com.mountreachsolutionpvtltd.healthsphere.R;
import com.mountreachsolutionpvtltd.healthsphere.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    RecyclerView rvGetAllCategory;
    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;
    AdapterGetAllCategoryDetailsRV adapterGetAllCategoryDetailsRV;

    CardView cvAllCustomerLocationInMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        rvGetAllCategory = findViewById(R.id.rvCategoryFragmentShowMultipleCategory);
        rvGetAllCategory.setLayoutManager(new GridLayoutManager(AdminHomeActivity.this,2,GridLayoutManager.HORIZONTAL,false));

        cvAllCustomerLocationInMap = findViewById(R.id.cvAdminHomeCustomerLocation);

        cvAllCustomerLocationInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, ViewAllCustomerLocationMapActivity.class);
                startActivity(intent);
            }
        });

        pojoGetAllCategoryDetails = new ArrayList<>();
        adapterGetAllCategoryDetailsRV = new AdapterGetAllCategoryDetailsRV(pojoGetAllCategoryDetails,this);
        rvGetAllCategory.setAdapter(adapterGetAllCategoryDetailsRV);

        getAllCategoryRV();

    }

    private void getAllCategoryRV() {
        RequestQueue requestQueue = Volley.newRequestQueue(AdminHomeActivity.this);//client server communication & create new request

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.getAllCategoryDetailsWebService,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //data and response in the form of json object => get jsonArray => get jsonObj => String bcz pojo require string data
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("getAllCategory");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String strId = jsonObject1.getString("id");
                                String strCategoryImage = jsonObject1.getString("categoryimage");
                                String strCategoryName = jsonObject1.getString("categoryname");

                                pojoGetAllCategoryDetails.add(new POJOGetAllCategoryDetails(strId,strCategoryImage,strCategoryName));

                            }

                            adapterGetAllCategoryDetailsRV.notifyDataSetChanged();//data change tell to adapter and then adapter tell to recyclerview


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }); //data get and put into RequestQueue
        requestQueue.add(stringRequest);

    }
}