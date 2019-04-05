package com.bekar.smartmedicalcare.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bekar.smartmedicalcare.Activity.MedicalReportAddActivity;
import com.bekar.smartmedicalcare.Adapter.AdapterMedicalReport;
import com.bekar.smartmedicalcare.ModelClass.MedicalReportModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.FragmentPatientMedicalReportsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class PatientMedicalReports extends Fragment implements View.OnClickListener{

    private FragmentPatientMedicalReportsBinding binding;

    private FirebaseFirestore firestore;
    private CollectionReference cRef;
    private FirebaseUser user;

    private String userId;

    private AdapterMedicalReport adapter;
    private List<MedicalReportModel> modelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_patient_medical_reports, container, false);
        binding= DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();

        userId=getArguments().getString("userId");

        if(userId.equals(user.getUid())){
            binding.fabAddReport.setVisibility(View.VISIBLE);
        }else {
            binding.fabAddReport.setVisibility(View.GONE);
        }

        modelList=new ArrayList<>();
        adapter=new AdapterMedicalReport(getContext(),modelList);

        RecyclerView.LayoutManager manager= new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);

        binding.rvReportList.setLayoutManager(manager);
        binding.rvReportList.setHasFixedSize(true);
        binding.rvReportList.setAdapter(adapter);

        binding.fabAddReport.setOnClickListener(this);

        getData();
    }

    private void getData() {
        cRef=firestore.collection("Patient")
                .document(userId)
                .collection("Medical Report");

        cRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc :
                        queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()){
                        case ADDED:
                            modelList.add(dc.getDocument().toObject(MedicalReportModel.class));
                            adapter.notifyDataSetChanged();
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getContext(), MedicalReportAddActivity.class);

        startActivity(intent);
    }
}
