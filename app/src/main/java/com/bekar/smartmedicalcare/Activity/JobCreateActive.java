package com.bekar.smartmedicalcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bekar.smartmedicalcare.ModelClass.JobCreateModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityJobCreateActiveBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class JobCreateActive extends AppCompatActivity {

    private ActivityJobCreateActiveBinding binding;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_job_create_active);

       userType=getIntent().getStringExtra("type");

       auth=FirebaseAuth.getInstance();
       user=auth.getCurrentUser();
       firestore=FirebaseFirestore.getInstance();

       binding.btnCreateJob.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               createJob();
           }
       });

    }

    private void createJob() {
        String userId=user.getUid();
        String title=binding.etTitleJobCreate.getText().toString().trim();
        String discription=binding.etDiscriptionJobCreate.getText().toString().trim();
        int budget=Integer.parseInt(binding.etBudgetJobCreate.getText().toString().trim());

        long timestamp= System.currentTimeMillis();
        String address=binding.etAddressJobCreate.getText().toString().trim();

        boolean flag=true;

        if(TextUtils.isEmpty(title)){
            binding.etTitleJobCreate.setError("Empty field found ...");
            flag=false;
        }
        if(TextUtils.isEmpty(discription)){
            binding.etDiscriptionJobCreate.setError("Empty field found ...");
            flag=false;
        }
        if(TextUtils.isEmpty(address)){
            binding.etAddressJobCreate.setError("Empty field found ...");
            flag=false;
        }
        if(budget<=0){
            binding.etBudgetJobCreate.setError("Empty field found ...");
            flag=false;
        }

        if(flag){
            JobCreateModel obj=new JobCreateModel(userId,user.getDisplayName(),title,discription,address,budget,timestamp);

            obj.setJobPosterType(userType);

            String id=firestore.collection("All Job Posts").document().getId();
            obj.setJobId(id);

            firestore.collection("All Job Posts").document(id)
                    .set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(JobCreateActive.this, "JobInformationActivity post Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(JobCreateActive.this, EmploymentOpurtunity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(JobCreateActive.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}
