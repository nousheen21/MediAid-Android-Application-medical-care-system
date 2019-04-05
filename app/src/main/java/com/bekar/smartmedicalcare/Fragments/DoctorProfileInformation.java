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

import com.bekar.smartmedicalcare.ModelClass.DoctorModel;
import com.bekar.smartmedicalcare.ModelClass.DoctorProfileInformationModel;
import com.bekar.smartmedicalcare.ModelClass.PatientProfileFullInformation;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.FragmentDoctorProfileBinding;
import com.bekar.smartmedicalcare.databinding.FragmentPatientProfileInformationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorProfileInformation extends Fragment {

    private FragmentDoctorProfileBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    CollectionReference cRef;

    String userId;

    DoctorProfileInformationModel drInfo;

    public DoctorProfileInformation() {
        // Required empty public constructor
    }



    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_doctor_profile, container, false);

        binding= DataBindingUtil.bind(v);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        drInfo=new DoctorProfileInformationModel();

        userId=getArguments().getString("userId");

        if(userId.equals(firebaseUser.getUid())){
            binding.btnDrUpdateDone.setVisibility(View.VISIBLE);
            binding.btnDrUpdate.setVisibility(View.VISIBLE);
        }else {
            binding.btnDrUpdateDone.setVisibility(View.GONE);
            binding.btnDrUpdate.setVisibility(View.GONE);
        }

        binding.btnDrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInformationClick();
            }
        });
        binding.btnDrUpdateDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDoneClick();
            }
        });


        cRef=firestore.collection("Doctor")
                .document(userId)
                .collection("Information");
        if(firebaseUser!=null){

            Log.d("TAG", "onCreate: "+userId);


            cRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if(queryDocumentSnapshots.size()>0){
                        DocumentSnapshot info=queryDocumentSnapshots.getDocuments().get(0);
                        drInfo = info.toObject(DoctorProfileInformationModel.class);

                        dataSet(drInfo);

                    }


                    //Toast.makeText(getContext(), "size : "+queryDocumentSnapshots.size(), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void updateDoneClick() {

        drInfo.setFullName(binding.tvFullNameD.getText().toString().trim());
        drInfo.setAge(binding.tvAgeD.getText().toString().trim());
        drInfo.setGender(binding.tvGenderD.getText().toString().trim());
        drInfo.setNationality(binding.tvNationalityD.getText().toString().trim());
        drInfo.setContact(binding.tvAddressD.getText().toString().trim());
        drInfo.setSpciality(binding.tvCurrentAffiliationD.getText().toString().trim());

        cRef.document(drInfo.getId()).set(drInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                addAllUserData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                viewEnable(false);
            }
        });



    }

    private void addAllUserData(){
        firestore.collection("All User")
                .whereEqualTo("userId",firebaseUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0){
                    DoctorModel model=queryDocumentSnapshots.getDocuments().get(0).toObject(DoctorModel.class);

                    model.setSpeciality(drInfo.getSpciality());
                    model.setFullName(drInfo.getFullName());
                    model.setGender(drInfo.getGender());
                    model.setMobile(drInfo.getContactMobile());
                    model.setProfilePic(drInfo.getProfileImage());

                    firestore.collection("All User")
                            .document(model.getId()).set(model)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("TAG", "onComplete: update successful");

                                    }else {
                                        Log.d("TAG", "onComplete: "+task.getException().getMessage());
                                    }
                                }
                            });

                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Update success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateInformationClick() {
        viewEnable(true);
    }

    private void viewEnable(boolean flag){
        binding.tvCurrentAffiliationD.setEnabled(flag);
        binding.tvAddressD.setEnabled(flag);
        binding.tvAgeD.setEnabled(flag);
        binding.tvFullNameD.setEnabled(flag);
        binding.tvGenderD.setEnabled(flag);
        binding.tvNationalityD.setEnabled(flag);
    }

    private void dataSet(DoctorProfileInformationModel pInfo) {
        binding.tvAgeD.setText(pInfo.getAge());
        binding.tvFullNameD.setText(pInfo.getFullName());
        binding.tvCurrentAffiliationD.setText(pInfo.getSpciality());
        binding.tvGenderD.setText(pInfo.getGender());
        binding.tvNationalityD.setText(pInfo.getNationality());
        binding.tvAddressD.setText(pInfo.getContact());
    }
}
