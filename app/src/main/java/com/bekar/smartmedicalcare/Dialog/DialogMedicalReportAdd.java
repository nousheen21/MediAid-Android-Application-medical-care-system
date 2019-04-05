package com.bekar.smartmedicalcare.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bekar.smartmedicalcare.Activity.JobApplyActive;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.DialogMedicalReportAddBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

public class DialogMedicalReportAdd extends Dialog {

    DialogMedicalReportAddBinding binding;

    Context context;
    FirebaseFirestore firestore;
    FirebaseUser user;
    CollectionReference cRef;

    public DialogMedicalReportAdd(@NonNull Context context) {
        super(context);

        this.context=context;
        this.firestore=FirebaseFirestore.getInstance();
        this.user= FirebaseAuth.getInstance().getCurrentUser();

        this.cRef=firestore.collection("Patient")
                .document(user.getUid())
                .collection("Medical Report");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding= DataBindingUtil.setContentView(getOwnerActivity(),R.layout.dialog_medical_report_add);

        binding.btnAddPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrescription();
            }


        });
    }
    private void getPrescription() {
    }


}
