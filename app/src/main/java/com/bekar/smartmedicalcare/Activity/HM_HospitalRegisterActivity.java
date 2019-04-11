package com.bekar.smartmedicalcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bekar.smartmedicalcare.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

public class HM_HospitalRegisterActivity extends AppCompatActivity {
    EditText hospitalName;
    EditText hospitalAddress;
    EditText hospitalPIN;
    Button regButton;

    DatabaseReference databaseHospital;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hm_hospital_register_activity);

        FirebaseApp app = FirebaseApp.getInstance("secondary");


        databaseHospital = FirebaseDatabase.getInstance(app).getReference("HospitalRegistrationData");

        hospitalName = (EditText)findViewById(R.id.editTextHospitalName);
        hospitalAddress = (EditText)findViewById(R.id.editTextHospitalAddress);
        hospitalPIN = (EditText)findViewById(R.id.hospitalPin);
        regButton = (Button)findViewById(R.id.registerButton);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHospital(hospitalName.getText().toString().trim(),hospitalAddress.getText().toString().trim(),hospitalPIN.getText().toString().trim());
            }
        });
    }


    private void addHospital(String Name, String Address, String PIN){

        if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Address) || TextUtils.isEmpty(PIN)){
            print("Field Empty!");
        }else {
            String id = databaseHospital.push().getKey();
            HM_Hospital hospital = new HM_Hospital(id, Name, Address, PIN);
            databaseHospital.child(id).setValue(hospital);
            print("Registration Done!");
            Intent intent = new Intent(getApplicationContext(),HM_LoginActivity.class);
            startActivity(intent);
            print("Login to enter.");
            finish();
            hospitalName.setText(null);
            hospitalAddress.setText(null);
            hospitalPIN.setText(null);
        }
    }
    public  void print(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

    }


}
