package com.bekar.smartmedicalcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bekar.smartmedicalcare.Activity.JobInformationActivity;
import com.bekar.smartmedicalcare.ModelClass.JobCreateModel;
import com.bekar.smartmedicalcare.ModelClass.JobItem;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.JobListItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterJobList extends RecyclerView.Adapter<AdapterJobList.ViewHolder> implements View.OnClickListener{
    private Context context;
    private List<JobCreateModel> jobItemList;
    private LayoutInflater inflater;

    public AdapterJobList(Context context, List<JobCreateModel> jobItemList) {
        this.context = context;
        this.jobItemList = jobItemList;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=inflater.inflate(R.layout.job_list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final JobCreateModel ji=jobItemList.get(position);
        holder.binding.jobClientName.setText(ji.getJobPostName());
        holder.binding.jobTitle.setText(ji.getJobTitle());
        holder.binding.jobDiscription.setText(ji.getJobDiscription());

        holder.binding.jobItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, JobInformationActivity.class);
                intent.putExtra("obj",ji);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobItemList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private JobListItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}
