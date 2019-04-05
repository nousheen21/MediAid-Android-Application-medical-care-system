package com.bekar.smartmedicalcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bekar.smartmedicalcare.Activity.MedicalReportDetailsActivity;
import com.bekar.smartmedicalcare.ModelClass.MedicalReportModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.MedicalReportItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterMedicalReport extends RecyclerView.Adapter<AdapterMedicalReport.ViewHolder> {

    private Context context;
    private List<MedicalReportModel> modelList;
    private LayoutInflater inflater;

    public AdapterMedicalReport(Context context, List<MedicalReportModel> modelList) {
        this.context = context;
        this.modelList = modelList;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.medical_report_item,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MedicalReportModel model=modelList.get(position);

        holder.binding.tvDrNameReport.setText(model.getDrName());

        holder.binding.tvDateReport.setText(model.getDate());

        holder.binding.tvSerialMRI.setText(""+position+1);

        holder.binding.layoutMedicalReportItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MedicalReportDetailsActivity.class);
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
        MedicalReportItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}
