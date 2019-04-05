package com.bekar.smartmedicalcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bekar.smartmedicalcare.Activity.PatientProfile;
import com.bekar.smartmedicalcare.ModelClass.DoctorModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.PatientsListItemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPatientList extends RecyclerView.Adapter<AdapterPatientList.ViewHolder> {
    private Context context;
    private List<DoctorModel> patientList;
    private LayoutInflater inflater;

    public AdapterPatientList(Context context, List<DoctorModel> patientList) {
        this.context = context;
        this.patientList = patientList;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdapterPatientList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.patients_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPatientList.ViewHolder holder, int position) {
        final DoctorModel model=patientList.get(position);

        holder.binding.tvPatientNameItem.setText(model.getFullName());
        holder.binding.tvPhoneNoItem.setText(model.getMobile());

        if(TextUtils.isEmpty(model.getProfilePic())){
            Glide.with(context).load(R.drawable.ic_boy)
                    .into(holder.binding.ivImagePatientItem);
        }else {
            Glide.with(context).load(model.getProfilePic())
                    .into(holder.binding.ivImagePatientItem);
        }

        holder.binding.layoutPatientItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PatientProfile.class);
                intent.putExtra("userId",model.getUserId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public PatientsListItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }

    public void addItems(List<DoctorModel> modelList){
        this.patientList=new ArrayList<>();
        this.patientList.addAll(modelList);
        notifyDataSetChanged();
    }
}
