package com.bekar.smartmedicalcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bekar.smartmedicalcare.Activity.JoApplyDetailsShowActivity;
import com.bekar.smartmedicalcare.ModelClass.JobApplyModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.AppliedJobAplicentItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterJobAppliedList extends RecyclerView.Adapter<AdapterJobAppliedList.ViewHolder> {

    private Context context;
    private List<JobApplyModel> jobApplyModelList;
    private LayoutInflater inflater;

    public AdapterJobAppliedList(Context context, List<JobApplyModel> jobApplyModelList) {
        this.context = context;
        this.jobApplyModelList = jobApplyModelList;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.applied_job_aplicent_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final JobApplyModel model=jobApplyModelList.get(position);

        holder.binding.tvAppicentName.setText(model.getUserName());
        holder.binding.tvSerialJA.setText(""+position+1);

        holder.binding.layoutJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, JoApplyDetailsShowActivity.class);

                intent.putExtra("jobDetails",model);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobApplyModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AppliedJobAplicentItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}
