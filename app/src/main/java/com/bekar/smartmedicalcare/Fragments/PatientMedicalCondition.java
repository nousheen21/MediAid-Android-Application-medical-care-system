package com.bekar.smartmedicalcare.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bekar.smartmedicalcare.Adapter.AdapterExpandable;
import com.bekar.smartmedicalcare.ModelClass.PatientMedicalConditionsModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.FragmentPatientMedicalConditionBinding;
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
import java.util.HashMap;
import java.util.List;


public class PatientMedicalCondition extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    FragmentPatientMedicalConditionBinding binding;

    private ArrayList<String> parentList;
    private HashMap<String,List<PatientMedicalConditionsModel>> childList;
    private AdapterExpandable exAdapter;
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_patient_medical_condition, container, false);
        binding= DataBindingUtil.bind(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentList=new ArrayList<>();
        childList=new HashMap<>();



        firestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();

        userId=getArguments().getString("userId");

        exAdapter=new AdapterExpandable(getContext(),parentList,childList,"Patient",userId);
        binding.exList.setAdapter(exAdapter);
        binding.exList.setGroupIndicator(null);

        data();
    }

    public void data(){

        parentList.add("Problems");
        parentList.add("Allergies");
        parentList.add("Special Conditions");

        List<PatientMedicalConditionsModel> list=new ArrayList<>();

        childList.put(parentList.get(0),list);


        firestore.collection("Patient")
                .document(userId)
                .collection("Problems")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc :
                                queryDocumentSnapshots.getDocumentChanges()) {
                            PatientMedicalConditionsModel model=dc.getDocument().toObject(PatientMedicalConditionsModel.class);
                            switch (dc.getType()){
                                case ADDED:
                                  childList.get(parentList.get(0)).add(model);
                                  exAdapter.notifyDataSetChanged();
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



                    }
                });
        getAllergiesPatient();
        getSpecialConditionPatient();

    }

    protected void getAllergiesPatient(){

        List<PatientMedicalConditionsModel> list=new ArrayList<>();

        childList.put(parentList.get(1),list);

        firestore.collection("Patient").document(userId)
                .collection("Allergies")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc:
                                queryDocumentSnapshots.getDocumentChanges()) {
                            PatientMedicalConditionsModel model=dc.getDocument().toObject(PatientMedicalConditionsModel.class);
                            switch (dc.getType()){

                                case ADDED:
                                    childList.get(parentList.get(1)).add(model);
                                    exAdapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    for (int i=0;i<childList.get(parentList.get(1)).size();i++){
                                        PatientMedicalConditionsModel m=childList.get(parentList.get(1)).get(i);

                                        if(model.getId().equals(m.getId())){
                                            childList.get(parentList.get(1)).remove(i);
                                            exAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }

                    }
                });


    }

    protected void getSpecialConditionPatient(){

        List<PatientMedicalConditionsModel> list=new ArrayList<>();

        childList.put(parentList.get(2),list);

        firestore.collection("Patient").document(userId)
                .collection("Special Condition")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc:
                                queryDocumentSnapshots.getDocumentChanges()) {
                            PatientMedicalConditionsModel model=dc.getDocument().toObject(PatientMedicalConditionsModel.class);
                            switch (dc.getType()){
                                case ADDED:
                                    childList.get(parentList.get(2)).add(model);
                                    exAdapter.notifyDataSetChanged();

                                    Log.d("Special Condition", "onEvent: ");

                                    break;
                                case REMOVED:
                                    for (int i=0;i<childList.get(parentList.get(1)).size();i++){
                                        PatientMedicalConditionsModel m=childList.get(parentList.get(2)).get(i);

                                        if(model.getId().equals(m.getId())){
                                            childList.get(parentList.get(2)).remove(i);
                                            exAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                });



    }

    /*@Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("parent",parentList);
        outState.putSerializable("child",childList);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            parentList=savedInstanceState.getStringArrayList("parent");
            childList= (HashMap<String, List<TitleClass>>) savedInstanceState.getSerializable("child");
        }

    }*/
}
