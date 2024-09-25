package com.mountreachsolutionpvtltd.healthsphere;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class AdapterGetAllCategoryDetails extends BaseAdapter {

    List<POGOGetAllCategoryDetails> pogoGetAllCategoryDetails;
    Activity activity;//use to call java class itself

    public AdapterGetAllCategoryDetails(List<POGOGetAllCategoryDetails> pogoGetAllCategoryDetails, Activity activity) {
        this.pogoGetAllCategoryDetails = pogoGetAllCategoryDetails;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        //return size of pojo class
        return pogoGetAllCategoryDetails.size();
    }

    @Override
    public Object getItem(int position) {
        //return postion of pojo class
        return pogoGetAllCategoryDetails.get(position);
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

            view.setTag(holder);


        }
        else {
            holder =(ViewHolder) view.getTag();
        }

        final POGOGetAllCategoryDetails obj = pogoGetAllCategoryDetails.get(position);
        holder.tvCategoryName.setText(obj.getCategoryName());

        Glide.with(activity)
                .load("http://192.168.2.74:80/UserAPI/images/"+obj.getCategoryImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk caching
                .skipMemoryCache(true)// Disable memory caching
                .error(R.drawable.icon_profile_photo)
                .into(holder.ivCategoryImage);


        //LayoutInflater call or store xml file
        //final keyword=>support nonchangeble
        //help to show design on listview and hold all design
        return view;
    }

    class ViewHolder
    {
        ImageView ivCategoryImage;
        TextView tvCategoryName;
    }


    //custon adapter to show multiple data on listview
    //operation perform on view using base adpater and help to show multiple view like text view, image view
    //AdapterGetAllCategoryDetails collect multiple view and show on listview
}
