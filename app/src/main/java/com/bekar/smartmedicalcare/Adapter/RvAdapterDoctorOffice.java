package com.bekar.smartmedicalcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bekar.smartmedicalcare.Activity.DoctorOfficeDetailsActivity;
import com.bekar.smartmedicalcare.ModelClass.DoctorOfficeModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.DoctorOfficeItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class RvAdapterDoctorOffice extends RecyclerView.Adapter<RvAdapterDoctorOffice.ViewHolder> {

    private Context context;
    private List<DoctorOfficeModel> modelList;
    private LayoutInflater inflater;

    public RvAdapterDoctorOffice(Context context, List<DoctorOfficeModel> modelList) {
        this.context = context;
        this.modelList = modelList;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.doctor_office_item,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final DoctorOfficeModel model=modelList.get(position);

        holder.binding.tvOfficeNameItem.setText(model.getOfficeName());
        holder.binding.tvOfficeLocationItem.setText(model.getOfficeLocation());

        holder.binding.layoutDoctorOfficeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DoctorOfficeDetailsActivity.class);
                intent.putExtra("model",model);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DoctorOfficeItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}
