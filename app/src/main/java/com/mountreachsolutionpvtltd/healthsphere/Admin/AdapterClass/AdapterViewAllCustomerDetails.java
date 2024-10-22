package com.mountreachsolutionpvtltd.healthsphere.Admin.AdapterClass;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mountreachsolutionpvtltd.healthsphere.AdapterCategorywiseProduct;
import com.mountreachsolutionpvtltd.healthsphere.Admin.POJOClass.POJOViewAllCustomerDetails;
import com.mountreachsolutionpvtltd.healthsphere.POJOCategorywiseProduct;
import com.mountreachsolutionpvtltd.healthsphere.R;

import java.util.List;

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
                .error(R.drawable.icon_profile_photo)
                .into(holder.ivProfilePhoto);


        return view;
    }
    class ViewHolder{
        ImageView ivProfilePhoto;
        TextView tvName,tvMobileNo,tvEmailId,tvAddress,tvUsername;
    }
}
