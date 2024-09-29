package com.mountreachsolutionpvtltd.healthsphere;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CtegoryWiseProductActivity extends AppCompatActivity {

    SearchView searchCategorywiseProduct;
    ListView lvCategorywiseProduct;
    TextView tvNoProductAvailable;

    String strCategoryname;

    List<POJOCategorywiseProduct> pojoCategorywiseProducts;
    AdapterCategorywiseProduct adapterCategorywiseProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctegory_wise_product);

        searchCategorywiseProduct = findViewById(R.id.svCategorywiseProductSearchProduct);
        lvCategorywiseProduct = findViewById(R.id.lvCategorywiseProductListOfProduct);
        tvNoProductAvailable = findViewById(R.id.tvCategorywiseProductNoProductAvailable);

        pojoCategorywiseProducts = new ArrayList<>();

        strCategoryname = getIntent().getStringExtra("categoryname");

        getCategorywiseProductList();
    }

    private void getCategorywiseProductList() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("categoryname",strCategoryname);
        client.post(Urls.CategorywiseProductWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray = response.getJSONArray("getCategoryWiseProduct");
                    if (jsonArray.isNull(0)){
                        lvCategorywiseProduct.setVisibility(View.GONE);
                        tvNoProductAvailable.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strId = jsonObject.getString("id");
                        String strCategoryname = jsonObject.getString("categoryname");
                        String strMedicalname = jsonObject.getString("medicalname");
                        String strProductcategory = jsonObject.getString("productcategory");
                        String strProductimage = jsonObject.getString("productimage");
                        String strProductname = jsonObject.getString("productname");
                        String strProductprice = jsonObject.getString("productprice");
                        String strProductrating = jsonObject.getString("productrating");
                        String strProductoffer = jsonObject.getString("productoffer");
                        String strProductdescription = jsonObject.getString("productdescription");

                        pojoCategorywiseProducts.add(new POJOCategorywiseProduct(strId,strCategoryname,strMedicalname,strProductcategory,strProductimage,strProductname,strProductprice,strProductrating,strProductoffer,strProductdescription));

                    }

                    adapterCategorywiseProduct = new AdapterCategorywiseProduct(pojoCategorywiseProducts,CtegoryWiseProductActivity.this);

                    lvCategorywiseProduct.setAdapter(adapterCategorywiseProduct);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(CtegoryWiseProductActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}