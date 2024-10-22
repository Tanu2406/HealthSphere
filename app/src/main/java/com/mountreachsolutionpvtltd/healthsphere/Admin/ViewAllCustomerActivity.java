package com.mountreachsolutionpvtltd.healthsphere.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolutionpvtltd.healthsphere.AdapterGetAllCategoryDetails;
import com.mountreachsolutionpvtltd.healthsphere.Admin.AdapterClass.AdapterViewAllCustomerDetails;
import com.mountreachsolutionpvtltd.healthsphere.Admin.POJOClass.POJOViewAllCustomerDetails;
import com.mountreachsolutionpvtltd.healthsphere.LoginActivity;
import com.mountreachsolutionpvtltd.healthsphere.POJOGetAllCategoryDetails;
import com.mountreachsolutionpvtltd.healthsphere.R;
import com.mountreachsolutionpvtltd.healthsphere.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ViewAllCustomerActivity extends AppCompatActivity {

    SearchView searchCustomer;
    ListView lvShowAllCustomer;
    TextView tvNoCustomerAvailable;

    ProgressDialog progressDialog;

    List<POJOViewAllCustomerDetails> pojoViewAllCustomerDetailsList;
    AdapterViewAllCustomerDetails adapterViewAllCustomerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_all_customer);

        pojoViewAllCustomerDetailsList = new ArrayList<>();
        searchCustomer = findViewById(R.id.svViewAllCustomerSearchCustomer);
        lvShowAllCustomer = findViewById(R.id.lvViewAllCustomerShowAllCustomer);
        tvNoCustomerAvailable = findViewById(R.id.tvViewAllCustomerNoCustomerAvailable);


        progressDialog = new ProgressDialog(ViewAllCustomerActivity.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Customer List Loading In Process");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        viewAllCustomer();

    }

    private void viewAllCustomer() {
        AsyncHttpClient client = new AsyncHttpClient();//Client-Server Communication means passing over the network
        RequestParams params = new RequestParams();//put the data Asynchttpclient

        client.post(Urls.getAllCustomerDetailsWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    progressDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("getAllCustomerDetails");
                    if (jsonArray.isNull(0)) {
                        tvNoCustomerAvailable.setVisibility(View.VISIBLE);
                    }
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strId = jsonObject.getString("id");
                        String strImage = jsonObject.getString("image");
                        String strName = jsonObject.getString("name");
                        String strMobileno = jsonObject.getString("mobileno");
                        String strEmailid = jsonObject.getString("emailid");
                        double dblLatitude = Double.parseDouble(jsonObject.getString("latitude"));
                        double dblLongitude = Double.parseDouble(jsonObject.getString("longitude"));
                        String strAddress = jsonObject.getString("address");
                        String strUsername = jsonObject.getString("username");


                        pojoViewAllCustomerDetailsList.add(new POJOViewAllCustomerDetails(strId,strImage,strName,strMobileno,strEmailid,dblLatitude,dblLongitude,strAddress,strUsername));

                    }
                    adapterViewAllCustomerDetails = new AdapterViewAllCustomerDetails(pojoViewAllCustomerDetailsList,ViewAllCustomerActivity.this);

                    lvShowAllCustomer.setAdapter(adapterViewAllCustomerDetails);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ViewAllCustomerActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}