package com.bekar.smartmedicalcare.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bekar.smartmedicalcare.ModelClass.DoctorModel;
import com.bekar.smartmedicalcare.ModelClass.PatientProfileFullInformation;
import com.bekar.smartmedicalcare.R;
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


public class PatientProfileInformation extends Fragment {

    FragmentPatientProfileInformationBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    PatientProfileFullInformation pInfo;
    String userId;
    CollectionReference cRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_patient_profile_information, container, false);

        binding= DataBindingUtil.bind(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();

        userId=getArguments().getString("userId");

        if(userId.equals(firebaseUser.getUid())){
            binding.btnPDone.setVisibility(View.VISIBLE);
            binding.btnPUpdate.setVisibility(View.VISIBLE);
        }else {
            binding.btnPDone.setVisibility(View.GONE);
            binding.btnPUpdate.setVisibility(View.GONE);
        }

        cRef=firestore.collection("Patient")
                .document(userId)
                .collection("Information");


        pInfo=new PatientProfileFullInformation();

        if(!TextUtils.isEmpty(userId)){

            Log.d("TAG", "onCreate: "+firebaseUser.getUid());


            cRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if(queryDocumentSnapshots.size()>0){
                        DocumentSnapshot info=queryDocumentSnapshots.getDocuments().get(0);
                        pInfo = info.toObject(PatientProfileFullInformation.class);

                        dataSet(pInfo);

                    }


                    //Toast.makeText(getActivity(), "size : "+queryDocumentSnapshots.size(), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Patient Profile Info", "onFailure: "+e.getMessage() );
                    //Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        binding.btnPUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewEnable(true);
            }
        });

        binding.btnPDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {

        Log.d("Patient profile", "updateData: ");

        pInfo.setAge(binding.tvAgeP.getText().toString().trim());
        pInfo.setFullName(binding.tvFullNameP.getText().toString().trim());
        pInfo.setAddress(binding.tvAddressP.getText().toString().trim());
        pInfo.setAveragePresure(binding.tvAvgPressureP.getText().toString().trim());
        pInfo.setBloodGroup(binding.tvBloodGroupP.getText().toString().trim());
        pInfo.setHeight(binding.tvHeightP.getText().toString().trim());
        pInfo.setWeight(binding.tvWeight.getText().toString().trim());
        pInfo.setGender(binding.tvGenderP.getText().toString().trim());
        pInfo.setNationality(binding.tvNationalityP.getText().toString().trim());
        pInfo.setOccupation(binding.tvOccupationP.getText().toString().trim());


        cRef.document(pInfo.getId()).set(pInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                    model.setFullName(pInfo.getFullName());
                    model.setGender(pInfo.getGender());
                    model.setMobile(pInfo.getMobileNo());
                    model.setProfilePic(pInfo.getProfilePic());

                    firestore.collection("All User")
                            .document(model.getId()).set(model)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Update Success", Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "onComplete: update successful");

                                    }else {
                                        Log.d("TAG", "onComplete: "+task.getException().getMessage());
                                    }
                                }
                            });

                }
            }
        });
    }

    private void dataSet(PatientProfileFullInformation pInfo) {
        binding.tvAgeP.setText(pInfo.getAge());
        binding.tvFullNameP.setText(pInfo.getFullName());
        binding.tvAddressP.setText(pInfo.getMobileNo());
        binding.tvAvgPressureP.setText(pInfo.getAveragePresure());
        binding.tvBloodGroupP.setText(pInfo.getBloodGroup());
        binding.tvHeightP.setText(pInfo.getHeight());
        binding.tvWeight.setText(pInfo.getWeight());
        binding.tvGenderP.setText(pInfo.getGender());
        binding.tvNationalityP.setText(pInfo.getNationality());
        binding.tvOccupationP.setText(pInfo.getOccupation());
    }

    protected void viewEnable(boolean flag){
        binding.tvAgeP.setEnabled(flag);
        binding.tvFullNameP.setEnabled(flag);
        binding.tvAddressP.setEnabled(flag);
        binding.tvAvgPressureP.setEnabled(flag);
        binding.tvBloodGroupP.setEnabled(flag);
        binding.tvHeightP.setEnabled(flag);
        binding.tvWeight.setEnabled(flag);
        binding.tvGenderP.setEnabled(flag);
        binding.tvNationalityP.setEnabled(flag);
        binding.tvOccupationP.setEnabled(flag);
    }
}
