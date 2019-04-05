package com.bekar.smartmedicalcare.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bekar.smartmedicalcare.Adapter.ExAdapterDoctorProfile;
import com.bekar.smartmedicalcare.ModelClass.PatientMedicalConditionsModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.FragmentDoctorMedicalProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorMedicalProfile extends Fragment implements View.OnClickListener{

    private FragmentDoctorMedicalProfileBinding binding;
    private ArrayList<String> parentList;
    private HashMap<String,List<PatientMedicalConditionsModel>> childList;
    private ExAdapterDoctorProfile exAdapter;
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private DocumentReference dRef;

    private String userId;

    public DoctorMedicalProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_doctor_medical_profile, container, false);
        binding= DataBindingUtil.bind(v);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentList=new ArrayList<>();
        childList=new HashMap<>();

        userId=getArguments().getString("userId");

        firestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        dRef=firestore.collection("Doctor")
                .document(userId);

        exAdapter=new ExAdapterDoctorProfile(getContext(),parentList,childList,"Doctor",userId);

        binding.exDrList.setAdapter(exAdapter);
        binding.exDrList.setGroupIndicator(null);

        data();

    }



    public void data(){

        parentList.add("Education");
        parentList.add("Achievements");
        parentList.add("Specialities");

        List<PatientMedicalConditionsModel> list=new ArrayList<>();

        childList.put(parentList.get(0),list);


        dRef.collection("Education")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc :
                                queryDocumentSnapshots.getDocumentChanges()) {
                            PatientMedicalConditionsModel model=dc.getDocument().toObject(PatientMedicalConditionsModel.class);
                            switch (dc.getType()){
                                case ADDED:
                                    childList.get(parentList.get(0)).add(model);
                                    break;
                                case REMOVED:
                                    for (int i=0;i<childList.get(parentList.get(0)).size();i++){
                                        PatientMedicalConditionsModel m=childList.get(parentList.get(0)).get(i);

                                        if(model.getId().equals(m.getId())){
                                            childList.get(parentList.get(0)).remove(i);
                                            exAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        exAdapter.notifyDataSetChanged();

                    }
                });
        getAchievements();
        getSpecialities();

    }

    protected void getAchievements(){

        List<PatientMedicalConditionsModel> list=new ArrayList<>();

        childList.put(parentList.get(1),list);


        dRef.collection("Achievements")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc :
                                queryDocumentSnapshots.getDocumentChanges()) {
                            PatientMedicalConditionsModel model=dc.getDocument().toObject(PatientMedicalConditionsModel.class);
                            switch (dc.getType()){
                                case ADDED:
                                    childList.get(parentList.get(1)).add(model);
                                    break;
                                case REMOVED:
                                    for (int i=0;i<childList.get(parentList.get(1)).size();i++){
                                        PatientMedicalConditionsModel m=childList.get(parentList.get(0)).get(i);

                                        if(model.getId().equals(m.getId())){
                                            childList.get(parentList.get(1)).remove(i);
                                            exAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        exAdapter.notifyDataSetChanged();

                    }
                });


    }

    protected void getSpecialities(){


        List<PatientMedicalConditionsModel> list=new ArrayList<>();

        childList.put(parentList.get(2),list);


        dRef.collection("Specialities")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc :
                                queryDocumentSnapshots.getDocumentChanges()) {
                            PatientMedicalConditionsModel model=dc.getDocument().toObject(PatientMedicalConditionsModel.class);
                            switch (dc.getType()){
                                case ADDED:
                                    childList.get(parentList.get(2)).add(model);
                                    break;
                                case REMOVED:
                                    for (int i=0;i<childList.get(parentList.get(2)).size();i++){
                                        PatientMedicalConditionsModel m=childList.get(parentList.get(0)).get(i);

                                        if(model.getId().equals(m.getId())){
                                            childList.get(parentList.get(2)).remove(i);
                                            exAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        exAdapter.notifyDataSetChanged();
                    }
                });



    }

    @Override
    public void onClick(View v) {

    }
}
