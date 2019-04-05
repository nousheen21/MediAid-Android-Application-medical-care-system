package com.bekar.smartmedicalcare.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bekar.smartmedicalcare.ModelClass.JobApplyModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityJoApplyDetailsShowBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

import java.io.File;

public class JoApplyDetailsShowActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityJoApplyDetailsShowBinding binding;
    private JobApplyModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_jo_apply_details_show);

        model= (JobApplyModel) getIntent().getSerializableExtra("jobDetails");

        binding.etNameJobApplyDetails.setText(model.getUserName());
        //binding.etBudgetJobApplyDetails.setText(""+model.getBudget());
        //binding.etCommentJobApplyDetails.setText(model.getComment());

        binding.btnCvShow.setOnClickListener(this);

        Log.d("PDF URL", "onCreate: "+model.getCvUrl());


       /* binding.pdfViewer.fromUri(Uri.parse("http://kmmc.in/wp-content/uploads/2014/01/lesson2.pdf")).swipeHorizontal(true)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .load();*/

        /*binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.getSettings().setAllowFileAccess(true);
        binding.webView.getSettings().setAllowContentAccess(true);
        binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        binding.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        binding.webView.getSettings().setDisplayZoomControls(true);

        Log.d("PDF URL", "onCreate: "+model.getCvUrl());

        binding.webView.loadUrl("http://docs.google.com/gview?embedded=true&url="+model.getCvUrl());

        binding.btnCVshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(model.getCvUrl());
            }
        });*/

    }

    public void openWebPage(String url) {

        try{
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }catch (Exception e){

        }


    }

    @Override
    public void onClick(View v) {
        openWebPage(model.getCvUrl());
    }
}
