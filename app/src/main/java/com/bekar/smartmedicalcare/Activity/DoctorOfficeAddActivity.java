package com.bekar.smartmedicalcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bekar.smartmedicalcare.ModelClass.DoctorOfficeModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityDoctorOfficeAddBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorOfficeAddActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityDoctorOfficeAddBinding binding;

    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private CollectionReference cRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_doctor_office_add);

        user= FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();
        cRef=firestore.collection("Doctor")
                .document(user.getUid())
                .collection("Offices");

        binding.btnAddOffice.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        String oName=binding.etOfficeName.getText().toString().trim();
        String oLocation=binding.etofficeLocation.getText().toString().trim();
        String oDiscription=binding.etOfficeDescription.getText().toString().trim();
        String oContact=binding.etOfficeContactInfo.getText().toString().trim();
        String oDays=binding.etOfficeDays.getText().toString().trim();
        String oTime=binding.etOfficeTime.getText().toString().trim();

        boolean flag=true;

        if(TextUtils.isEmpty(oName)){
            binding.etOfficeName.setError("Empty field found");
            flag=false;
        }
        if(TextUtils.isEmpty(oLocation)){
            binding.etofficeLocation.setError("Empty field found");
            flag=false;
        }
        if(TextUtils.isEmpty(oDiscription)){
            binding.etOfficeDescription.setError("Empty field found");
            flag=false;
        }
        if(TextUtils.isEmpty(oContact)){
            binding.etOfficeContactInfo.setError("Empty field found");
            flag=false;
        }
        if(TextUtils.isEmpty(oDays)){
            binding.etOfficeDays.setError("Empty field found");
            flag=false;
        }
        if(TextUtils.isEmpty(oTime)){
            binding.etOfficeTime.setError("Empty field found");
            flag=false;
        }

        if(flag){

            String id=cRef.document().getId();

            DoctorOfficeModel model=new DoctorOfficeModel(id,user.getUid(),oName,oLocation,oContact,oDiscription,oTime,oDays);

            cRef.document(id).set(model)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DoctorOfficeAddActivity.this, "Office Add Success", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }else {
                                Toast.makeText(DoctorOfficeAddActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }



    }
}
