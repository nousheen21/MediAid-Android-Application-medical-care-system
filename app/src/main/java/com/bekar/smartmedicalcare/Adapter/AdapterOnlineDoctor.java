package com.bekar.smartmedicalcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bekar.smartmedicalcare.Activity.DoctorCallActivity;
import com.bekar.smartmedicalcare.Activity.DoctorProfile;
import com.bekar.smartmedicalcare.ModelClass.DoctorModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.DoctorOnlineItemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterOnlineDoctor extends RecyclerView.Adapter<AdapterOnlineDoctor.ViewHolder> {

    private Context context;
    private List<DoctorModel> searchModelList;
    private LayoutInflater inflater;
    private String from;

    public AdapterOnlineDoctor(Context context, List<DoctorModel> modelList,String from) {
        this.context = context;
        this.searchModelList=new ArrayList<>(modelList);
        this.inflater=LayoutInflater.from(context);
        this.from=from;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.doctor_online_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DoctorModel model=searchModelList.get(position);

        holder.binding.tvDoctorName.setText(model.getFullName());
        holder.binding.tvSpecility.setText(model.getSpeciality());
        if(model.isActive()){
            holder.binding.ivOnlineOffline.setImageResource(R.drawable.online);
        }else {
            holder.binding.ivOnlineOffline.setImageResource(R.drawable.offline);
        }

        if(!TextUtils.isEmpty(model.getProfilePic())){
            Glide.with(context).load(model.getProfilePic()).into(holder.binding.ivDoctorImage);

        }else {
            Glide.with(context).load(R.drawable.doctor)
                    .into(holder.binding.ivDoctorImage);
        }

        holder.binding.layoutDoctorOnlineItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(from.equals("Doctor")){
                    Intent intent=new Intent(context, DoctorProfile.class);
                    intent.putExtra("userId",model.getUserId());
                    context.startActivity(intent);
                }else {
                    Intent intent=new Intent(context, DoctorCallActivity.class);
                    intent.putExtra("number",model.getMobile());
                    context.startActivity(intent);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return searchModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DoctorOnlineItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }


    public void addItems(List<DoctorModel> modelList){
        searchModelList=new ArrayList<>();
        searchModelList.addAll(modelList);
        notifyDataSetChanged();
    }

}
