package com.mountreachsolutionpvtltd.healthsphere.Admin.AdapterClass;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mountreachsolutionpvtltd.healthsphere.POJOGetAllCategoryDetails;
import com.mountreachsolutionpvtltd.healthsphere.R;
import java.util.List;
public class AdapterGetAllCategoryDetailsRV extends RecyclerView.Adapter<AdapterGetAllCategoryDetailsRV.ViewHolder>
        //                                              class    , nested class, resultant class,             inner class
        // communication with 2nd xml file i.e. design and set data to adapter class & all data in the form of recyclerview
        // Adapter is nested class of RecyclerView and pass current java class and its inner||nested class is the resultant class of adapter
{
    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;
    Activity activity;
    public AdapterGetAllCategoryDetailsRV(List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails, Activity activity) {
        this.pojoGetAllCategoryDetails = pojoGetAllCategoryDetails;
        this.activity = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create design/layout and call 2nd xml file and pass design to View
        View view = LayoutInflater.from(activity).inflate(R.layout.rv_get_all_category,parent,false);
        //call layout .indicate current java class use method from .call xml file
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind data and set to pojo
        POJOGetAllCategoryDetails obj = pojoGetAllCategoryDetails.get(position);
        holder.tvCategoryName.setText(obj.getCategoryName());
        Glide.with(activity).load("http://192.168.252.74:80/UserAPI/images/"+obj.getCategoryImage())//http:// for client server communication for transfer data
                .skipMemoryCache(false)//to clare skip memory
                .error(R.drawable.icon_profile_photo)
                .into(holder.ivCategoryImage);
    }
    @Override
    public int getItemCount() {
        //return size of pojo class
        return pojoGetAllCategoryDetails.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryImage;
        TextView tvCategoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}