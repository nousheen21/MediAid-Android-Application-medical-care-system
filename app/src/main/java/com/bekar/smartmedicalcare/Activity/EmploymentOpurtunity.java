package com.bekar.smartmedicalcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bekar.smartmedicalcare.Adapter.AdapterJobList;
import com.bekar.smartmedicalcare.Interface.UpdateInterface;
import com.bekar.smartmedicalcare.ModelClass.JobCreateModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityEmploymentOpurtunityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class EmploymentOpurtunity extends AppCompatActivity
        implements View.OnClickListener {
    ActivityEmploymentOpurtunityBinding binding;
    AdapterJobList adapter;
    List<JobCreateModel> jobItemList;


    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;

    private String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_employment_opurtunity);

        setSupportActionBar(binding.toolbar);

        usertype=getIntent().getStringExtra("type");

        jobItemList=new ArrayList<>();
        //setJob();

        adapter=new AdapterJobList(this,jobItemList);

        RecyclerView.LayoutManager manager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        binding.rvJobList.setLayoutManager(manager);
        binding.rvJobList.setHasFixedSize(true);
        binding.rvJobList.setAdapter(adapter);

        binding.btnAddJob.setOnClickListener(this);

        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        getData();

    }

    private void getData() {

        firestore.collection("All Job Posts")
                .orderBy("jobCreateDate")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc :
                                queryDocumentSnapshots.getDocumentChanges()) {
                            JobCreateModel model=dc.getDocument().toObject(JobCreateModel.class);
                            switch (dc.getType()){
                                case ADDED:
                                    Log.d("Job list", "onEvent: added");
                                    jobItemList.add(model);
                                    break;
                                case REMOVED:{
                                    Log.d("Job list", "onEvent: update");
                                    for(int i=0;i<jobItemList.size();i++){
                                        JobCreateModel m=jobItemList.get(i);
                                        if(m.getJobId().equals(model.getJobId())){
                                            jobItemList.remove(i);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

    }

    /*private void setJob() {
        for(int i=0;i<10;i++){
            JobItem jj=new JobItem("New JobInformationActivity",
                    "description description description " +
                            "description description description description " +
                            "description description description description " +
                            "description description description description " +
                            "description description description description " +
                            "description description description description " +
                            "description description ","Joy "+i);
            jobItemList.add(jj);

        }
    }*/

    @Override
    public void onClick(View v) {

        Intent intent=new Intent(this,JobCreateActive.class);
        intent.putExtra("type",usertype);

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
