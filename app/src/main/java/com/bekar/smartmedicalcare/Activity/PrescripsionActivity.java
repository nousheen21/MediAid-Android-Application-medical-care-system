package com.bekar.smartmedicalcare.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityPrescripsionBinding;
import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class PrescripsionActivity extends AppCompatActivity {

    private ActivityPrescripsionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_prescripsion);

        String path=getIntent().getStringExtra("path");


        Glide.with(this)
                .load(path)
                .into(binding.photoView);

    }
}
