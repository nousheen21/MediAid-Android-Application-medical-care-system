package com.bekar.smartmedicalcare.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityDoctorCallBinding;

public class DoctorCallActivity extends AppCompatActivity implements View.OnClickListener {

    static final int PERMISSION_REQUEST_CODE=111;
    ActivityDoctorCallBinding binding;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_doctor_call);
        number="";

        if(getIntent()!=null){
            String num=getIntent().getStringExtra("number");
            if(num!=null){
                number+=num;
                binding.tvNumber.setText(number);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn0:
                if(number.length()<11){
                    number+="0";
                    binding.tvNumber.setText(number);
                }
                break;
            case R.id.btn1:
                if(number.length()<11){
                    number+="1";
                    binding.tvNumber.setText(number);
                }
                break;
            case R.id.btn2:
                if(number.length()<11){
                    number+="2";
                    binding.tvNumber.setText(number);
                }
                break;
            case R.id.btn3:
                if(number.length()<11){
                    number+="3";
                    binding.tvNumber.setText(number);
                }
                break;
            case R.id.btn4:
                if(number.length()<11){
                    number+="4";
                    binding.tvNumber.setText(number);
                }
                break;
            case R.id.btn5:
                if(number.length()<11){
                    number+="5";
                    binding.tvNumber.setText(number);
                }
                break;
            case R.id.btn6:
                if(number.length()<11){
                    number+="6";
                    binding.tvNumber.setText(number);
                }
                break;
            case R.id.btn7:
                if(number.length()<11){
                    number+="7";
                    binding.tvNumber.setText(number);
                }
                break;
            case R.id.btn8:
                if(number.length()<11){
                    number+="8";
                    binding.tvNumber.setText(number);
                }
                break;
            case R.id.btn9:
                if(number.length()<11){
                    number+="9";
                    binding.tvNumber.setText(number);
                }
                break;
            case R.id.btnDelete:
                
                if(number.length()>0){
                    number=number.substring(0,number.length()-1);
                    binding.tvNumber.setText(number);
                }
                
                break;
            case R.id.btnDial:
                call();
                break;
        }
    }

    private void call() {
        if(checkPermission()){
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ number)));
        }else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(DoctorCallActivity.this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(DoctorCallActivity.this, android.Manifest.permission.CALL_PHONE)) {
            Toast.makeText(DoctorCallActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(DoctorCallActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ number)));
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}
