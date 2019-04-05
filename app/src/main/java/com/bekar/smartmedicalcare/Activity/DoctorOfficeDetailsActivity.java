package com.bekar.smartmedicalcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.bekar.smartmedicalcare.ModelClass.DoctorOfficeModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityDoctorOfficeDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorOfficeDetailsActivity extends AppCompatActivity implements
        View.OnClickListener {

    private ActivityDoctorOfficeDetailsBinding binding;
    private DoctorOfficeModel model;
    private FirebaseUser user;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_doctor_office_details);

        user= FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();

        model= (DoctorOfficeModel) getIntent().getSerializableExtra("model");

        if(user.getUid().equals(model.getUserId())){
            binding.btnDeleteOfficeInfo.setVisibility(View.VISIBLE);
        }else {
            binding.btnDeleteOfficeInfo.setVisibility(View.GONE);
        }

        binding.btnDeleteOfficeInfo.setOnClickListener(this);

        binding.etOfficeNameOD.setText(model.getOfficeName());
        binding.etofficeLocationOD.setText(model.getOfficeLocation());
        binding.etOfficeContactInfoOD.setText(model.getOfficeContactInfo());
        binding.etOfficeDescriptionOD.setText(model.getOfficeDescription());
        binding.etOfficeDaysOD.setText(model.getOfficeDays());
        binding.etOfficeTimeOD.setText(model.getOfficeTime());
    }

    @Override
    public void onClick(View v) {
        firestore.collection("Doctor")
                .document(user.getUid())
                .collection("Offices")
                .document(model.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            finish();
                        }
                    }
                });
    }
}
