    package com.mountreachsolutionpvtltd.healthsphere;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


    public class CategoryFragment extends Fragment {

        SearchView searchCategory;

       ListView lvShowAllCategory;
       TextView tvNoCategoryAvailable;

       List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;//POGOGetAllCategoryDetails resultant class of list
        AdapterGetAllCategoryDetails adapterGetAllCategoryDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);

        pojoGetAllCategoryDetails = new ArrayList<>();
        searchCategory = view.findViewById(R.id.svCategoryFragmentSearchCategory);
        lvShowAllCategory = view.findViewById(R.id.lvCategoryFragmentShowMultipleCategory);
        tvNoCategoryAvailable = view.findViewById(R.id.tvCategoryFragmentNoCategoryAvailable);

        //query = user type ex-cake
        searchCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCategory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchCategory(query);
                return false;
            }
        });

        getAllCategory();

        return  view;//view store xml file and design
    }

        private void searchCategory(String query) {
        List<POJOGetAllCategoryDetails> tempcategory = new ArrayList<>();
        tempcategory.clear();

        for (POJOGetAllCategoryDetails obj:pojoGetAllCategoryDetails)
        {
            if (obj.getCategoryName().toUpperCase().contains(query.toUpperCase())){

                tempcategory.add(obj);

            }else {
                tvNoCategoryAvailable.setVisibility(View.VISIBLE);
            }

            adapterGetAllCategoryDetails = new AdapterGetAllCategoryDetails(tempcategory,getActivity());
            lvShowAllCategory.setAdapter(adapterGetAllCategoryDetails);

        }

        }

        private void getAllCategory() {
        //classname objname = new constructorname();
            AsyncHttpClient client = new AsyncHttpClient();//Client-Server Communication means passing over the network
            RequestParams params = new RequestParams();//put the data Asynchttpclient

            client.post(Urls.getAllCategoryDetailsWebService,params,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {
                        JSONArray jsonArray = response.getJSONArray("getAllCategory");
                        if (jsonArray.isNull(0)) {
                            tvNoCategoryAvailable.setVisibility(View.VISIBLE);
                        }
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String strId = jsonObject.getString("id");
                            String strCategoryImage = jsonObject.getString("categoryimage");
                            String strCategoryName = jsonObject.getString("categoryname");

                            pojoGetAllCategoryDetails.add(new POJOGetAllCategoryDetails(strId,strCategoryImage,strCategoryName));

                        }
                        adapterGetAllCategoryDetails = new AdapterGetAllCategoryDetails(pojoGetAllCategoryDetails,getActivity());

                        lvShowAllCategory.setAdapter(adapterGetAllCategoryDetails);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //Activity call = use Activityname.this
    //Fragment call = use getActivity
    //same time of multiple data load by list view