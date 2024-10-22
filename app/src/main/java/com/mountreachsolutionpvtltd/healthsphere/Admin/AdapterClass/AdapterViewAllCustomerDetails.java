package com.mountreachsolutionpvtltd.healthsphere.Admin.AdapterClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolutionpvtltd.healthsphere.AdapterCategorywiseProduct;
import com.mountreachsolutionpvtltd.healthsphere.Admin.POJOClass.POJOViewAllCustomerDetails;
import com.mountreachsolutionpvtltd.healthsphere.POJOCategorywiseProduct;
import com.mountreachsolutionpvtltd.healthsphere.R;
import com.mountreachsolutionpvtltd.healthsphere.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AdapterViewAllCustomerDetails extends BaseAdapter {

    List<POJOViewAllCustomerDetails> pojoViewAllCustomerDetails;
    Activity activity;

    public AdapterViewAllCustomerDetails(List<POJOViewAllCustomerDetails> pojoViewAllCustomerDetails, Activity activity) {
        this.pojoViewAllCustomerDetails = pojoViewAllCustomerDetails;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pojoViewAllCustomerDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return pojoViewAllCustomerDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final AdapterViewAllCustomerDetails.ViewHolder holder;
        final LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view==null)
        {
            holder = new AdapterViewAllCustomerDetails.ViewHolder();
            view = inflater.inflate(R.layout.lv_view_all_customer_details,null);

            holder.ivProfilePhoto = view.findViewById(R.id.ivViewAllCustomerProfilePhoto);
            holder.tvName = view.findViewById(R.id.tvViewAllCustomerName);
            holder.tvMobileNo = view.findViewById(R.id.tvViewAllCustomerMobileNo);
            holder.tvEmailId = view.findViewById(R.id.tvViewAllCustomerEmailId);
            holder.tvAddress = view.findViewById(R.id.tvViewAllCustomerAddress);
            holder.tvUsername = view.findViewById(R.id.tvViewAllCustomerUsername);
            holder.acbtnDeleteUser = view.findViewById(R.id.acbtnViewAllCustomerDeleteUser);

            view.setTag(holder);

        }
        else
        {
            holder = (AdapterViewAllCustomerDetails.ViewHolder) view.getTag();
        }

        final POJOViewAllCustomerDetails obj = pojoViewAllCustomerDetails.get(position);

        holder.tvName.setText(obj.getName());
        holder.tvMobileNo.setText(obj.getMobileno());
        holder.tvEmailId.setText(obj.getEmailid());
        holder.tvAddress.setText(obj.getAddress());
        holder.tvUsername.setText(obj.getUsername());


        Glide.with(activity)
                .load("http://192.168.252.74:80/UserAPI/images/"+obj.getImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk caching
                .skipMemoryCache(true)// Disable memory caching
                .error(R.drawable.noimgavailable)
                .into(holder.ivProfilePhoto);

        holder.acbtnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(activity);
                ad.setTitle("Delete User");
                ad.setMessage("Are You Sure You Want to Delete User?");
                ad.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.cancel();
                    }
                });
                ad.setNegativeButton("Delete User", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(obj.getUsername(),position);
                    }
                }).create().show();
            }
        });




        return view;
    }

    private void deleteUser(String username, int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",username);

        client.post(Urls.deleteUserWebService,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("success");
                    if(status.equals("1")){
                        pojoViewAllCustomerDetails.remove(position);//data delete
                        notifyDataSetChanged();//list update
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ViewHolder{
        ImageView ivProfilePhoto;
        TextView tvName,tvMobileNo,tvEmailId,tvAddress,tvUsername;
        AppCompatButton acbtnDeleteUser;
    }
}
