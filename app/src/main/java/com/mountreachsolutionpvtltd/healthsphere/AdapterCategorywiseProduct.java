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

public class AdapterCategorywiseProduct extends BaseAdapter {

    List<POJOCategorywiseProduct> list;
    Activity activity;

    public AdapterCategorywiseProduct(List<POJOCategorywiseProduct> pojoCategorywiseProductsList, Activity activity) {
        this.list = pojoCategorywiseProductsList;
        this.activity = activity;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        final LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view==null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lv_categorywise_product,null);

            holder.ivCategorywiseProductImage = view.findViewById(R.id.ivcategorywiseProductImg);
            holder.tvMedicalName = view.findViewById(R.id.tvcategorywiseMedicalName);
            holder.tvMedicalRating = view.findViewById(R.id.tvcategorywiseMedicalRating);
            holder.tvProductName = view.findViewById(R.id.tvcategorywiseProductName);
            holder.tvProductCategory = view.findViewById(R.id.tvcategorywiseProductCategory);
            holder.tvProductPrice = view.findViewById(R.id.tvcategorywiseProductPrice);
            holder.tvProductOffer = view.findViewById(R.id.tvcategorywiseProductOffer);
            holder.tvProductDescription = view.findViewById(R.id.tvcategorywiseProductDescription);

            view.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        final POJOCategorywiseProduct obj = list.get(position);

        holder.tvMedicalName.setText(obj.getMedicalname());
        holder.tvMedicalRating.setText(obj.getProductrating());
        holder.tvProductName.setText(obj.getProductname());
        holder.tvProductCategory.setText(obj.getProductcategory());
        holder.tvProductPrice.setText(obj.getProductprice());
        holder.tvProductOffer.setText(obj.getProductoffer());
        holder.tvProductDescription.setText(obj.getProductdescription());

        Glide.with(activity)
                .load("http://192.168.252.74:80/UserAPI/images/"+obj.getProductimage())
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk caching
                .skipMemoryCache(true)// Disable memory caching
                .error(R.drawable.icon_profile_photo)
                .into(holder.ivCategorywiseProductImage);


        return view;
    }

    class ViewHolder{
        ImageView ivCategorywiseProductImage;
        TextView tvMedicalName,tvMedicalRating,tvProductName,tvProductCategory,tvProductPrice,tvProductOffer,tvProductDescription;

    }
}

//Database => Web Service => Android app => CategorywiseProductActivity => POJO class => Adapter class(Baseadapter) => Listview

//5 file
// 2 XML => 1: Activity/Fragment = LIstview, 2: XML => Design ji user la show hoil
//2 Java => 1: POJO class => get and set , 2: Adapter class => Get data pojo and set listview
//1 Activity/Fragment => receive data from web services
