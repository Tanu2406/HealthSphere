package com.mountreachsolutionpvtltd.healthsphere;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class AdapterGetAllCategoryDetails extends BaseAdapter {

    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;
    Activity activity;//use to call java class itself

    public AdapterGetAllCategoryDetails(List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails, Activity activity) {
        this.pojoGetAllCategoryDetails = pojoGetAllCategoryDetails;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        //return size of pojo class
        return pojoGetAllCategoryDetails.size();
    }

    @Override
    public Object getItem(int position) {
        //return postion of pojo class
        return pojoGetAllCategoryDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return postion of itemid /
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
       //                          typecast        current java class to get access from system service to inflater

        if (view==null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lv_get_all_category,null);
            holder.ivCategoryImage  = view.findViewById(R.id.ivLvCategoryImage);
            holder.tvCategoryName = view.findViewById(R.id.tvCategoryName);
            holder.cvCategoryList = view.findViewById(R.id.cvCtegoryList);

            view.setTag(holder);


        }
        else {
            holder =(ViewHolder) view.getTag();
        }

        final POJOGetAllCategoryDetails obj = pojoGetAllCategoryDetails.get(position);
        holder.tvCategoryName.setText(obj.getCategoryName());

        Glide.with(activity)
                .load("http://192.168.252.74:80/UserAPI/images/"+obj.getCategoryImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk caching
                .skipMemoryCache(true)// Disable memory caching
                .error(R.drawable.icon_profile_photo)
                .into(holder.ivCategoryImage);

        holder.cvCategoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, CtegoryWiseProductActivity.class);
                i.putExtra("categoryname",obj.getCategoryName());
                activity.startActivity(i);
            }
        });


        //LayoutInflater call or store xml file
        //final keyword=>support nonchangeble
        //help to show design on listview and hold all design
        return view;
    }

    class ViewHolder
    {
        ImageView ivCategoryImage;
        TextView tvCategoryName;
        CardView cvCategoryList;
    }


    //custon adapter to show multiple data on listview
    //operation perform on view using base adpater and help to show multiple view like text view, image view
    //AdapterGetAllCategoryDetails collect multiple view and show on listview
}
