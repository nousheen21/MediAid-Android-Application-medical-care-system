package com.bekar.smartmedicalcare.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bekar.smartmedicalcare.ModelClass.MedicalReportModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityMedicalReportDetailsBinding;
import com.bumptech.glide.Glide;

public class MedicalReportDetailsActivity extends AppCompatActivity {

    ActivityMedicalReportDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_medical_report_details);

        final MedicalReportModel model= (MedicalReportModel) getIntent().getSerializableExtra("model");

        binding.tvDoctorNameRD.setText(model.getDrName());
        binding.tvDateReportRD.setText(model.getDate());

        Glide.with(getApplicationContext())
                .load(model.getReportPath())
                .into(binding.ivReportRD);
        binding.ivReportRD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PrescripsionActivity.class);
                intent.putExtra("path",model.getReportPath());

                startActivity(intent);
            }
        });
    }
}
