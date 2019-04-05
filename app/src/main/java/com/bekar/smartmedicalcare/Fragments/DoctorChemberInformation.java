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

import com.bekar.smartmedicalcare.Activity.DoctorOfficeAddActivity;
import com.bekar.smartmedicalcare.Adapter.RvAdapterDoctorOffice;
import com.bekar.smartmedicalcare.ModelClass.DoctorOfficeModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.FragmentDoctorChemberInformationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DoctorChemberInformation extends Fragment implements View.OnClickListener{

    private FragmentDoctorChemberInformationBinding binding;

    private FirebaseUser user;
    private FirebaseFirestore firestore;

    private List<DoctorOfficeModel> modelList;
    private RvAdapterDoctorOffice adapter;

    private String userId;

    public DoctorChemberInformation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_doctor_chember_information, container, false);
        binding= DataBindingUtil.bind(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user= FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();

        userId=getArguments().getString("userId");

        if(userId.equals(user.getUid())){
            binding.fabAddOffice.setVisibility(View.VISIBLE);
        }else {
            binding.fabAddOffice.setVisibility(View.GONE);
        }

        modelList=new ArrayList<>();
        adapter=new RvAdapterDoctorOffice(getContext(),modelList);

        RecyclerView.LayoutManager manager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        binding.rvDoctorOffice.setLayoutManager(manager);
        binding.rvDoctorOffice.setHasFixedSize(true);
        binding.rvDoctorOffice.setAdapter(adapter);

        binding.fabAddOffice.setOnClickListener(this);

        setData();
    }

    private void setData() {
        firestore.collection("Doctor")
                .document(userId)
                .collection("Offices")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc :
                                queryDocumentSnapshots.getDocumentChanges()) {
                            DoctorOfficeModel model=dc.getDocument().toObject(DoctorOfficeModel.class);
                            switch (dc.getType()){
                                case ADDED:
                                    modelList.add(model);
                                    break;
                                case REMOVED:
                                    for (int i=0;i<modelList.size();i++){
                                        DoctorOfficeModel m=modelList.get(i);
                                        if(model.getId().equals(m.getId())){
                                            modelList.remove(i);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;

                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getContext(), DoctorOfficeAddActivity.class);

        startActivity(intent);
    }
}
