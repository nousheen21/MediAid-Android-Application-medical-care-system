package com.bekar.smartmedicalcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bekar.smartmedicalcare.Adapter.AdapterJobAppliedList;
import com.bekar.smartmedicalcare.ModelClass.JobApplyModel;
import com.bekar.smartmedicalcare.ModelClass.JobCreateModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityJobBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class JobInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityJobBinding binding;
    private JobCreateModel model;

    private AdapterJobAppliedList adapter;
    private List<JobApplyModel> jobApplyModels;

    private FirebaseFirestore firestore;
    private FirebaseUser user;

    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_job);

       //firebase
        firestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();

        model= (JobCreateModel) getIntent().getSerializableExtra("obj");

        if(model.getJobPosterId().equals(user.getUid())){
            flag=true;
            binding.btnApplyJob.setText("Delete this job");
            //binding.btnApplyJob.setVisibility(View.GONE);
        }else {
            flag=false;
            //binding.btnApplyJob.setVisibility(View.VISIBLE);
            binding.btnApplyJob.setText("Apply this job");
        }

        binding.tvUserNameJob.setText(model.getJobPostName());
        binding.tvJobCreateDate.setText("");
        binding.tvJobTitle.setText(model.getJobTitle());
        binding.tvJobDiscription.setText(model.getJobDiscription());
        binding.tvAddressJob.setText(model.getJobAddress());
        binding.tvBudget.setText(""+model.getJobBudget());

        jobApplyModels=new ArrayList<>();

        adapter = new AdapterJobAppliedList(this,jobApplyModels);

        final RecyclerView.LayoutManager manager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        binding.rvApplicentList.setHasFixedSize(true);
        binding.rvApplicentList.setLayoutManager(manager);
        binding.rvApplicentList.setAdapter(adapter);


        if(!model.getJobPosterId().equals(user.getUid())){
            binding.rvApplicentList.setVisibility(View.GONE);
        }

        binding.btnApplyJob.setOnClickListener(this);

        binding.tvUserNameJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();

                if(model.getJobPosterType().equals("Doctor")){
                    intent=new Intent(getApplicationContext(),DoctorProfile.class);
                }else {
                    intent=new Intent(getApplicationContext(),PatientProfile.class);
                }

                intent.putExtra("userId",model.getJobPosterId());

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setData();
    }

    private void setData() {

        jobApplyModels.clear();

        firestore.collection("Job Apply List")
                .document(model.getJobPosterId())
                .collection(model.getJobId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot ds :
                        queryDocumentSnapshots.getDocuments()) {
                    jobApplyModels.add(ds.toObject(JobApplyModel.class));
                }

                adapter.notifyDataSetChanged();

                binding.tvTotalApplied.setText(""+jobApplyModels.size());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(flag){
            firestore.collection("All Job Posts")
                    .document(model.getJobId())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                finish();
                            }
                        }
                    });
        }else {
            Intent intent=new Intent(this,JobApplyActive.class);
            intent.putExtra("jobId",model.getJobId());
            intent.putExtra("jobPosterId",model.getJobPosterId());
            startActivity(intent);
        }
    }


}
