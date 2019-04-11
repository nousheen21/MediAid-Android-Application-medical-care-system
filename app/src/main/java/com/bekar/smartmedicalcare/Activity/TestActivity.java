package com.bekar.smartmedicalcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bekar.smartmedicalcare.R;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private Button hospitalButton;
    private  Button medicalStoreButton;
    private Button navButton;
    private Button searchMedicineButton;
    private Button searchStoreButton;
    private Button searchHospitalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        hospitalButton = (Button)findViewById(R.id.hospitalSystem);
        medicalStoreButton = (Button)findViewById(R.id.medicalStore);
        navButton = (Button)findViewById(R.id.navButton);
        searchMedicineButton = (Button)findViewById(R.id.medButton);
        searchStoreButton = (Button)findViewById(R.id.storeButton);
        searchHospitalButton = (Button)findViewById(R.id.hospitalButton);

        medicalStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MS_LoginActivity.class);
                startActivity(intent);

            }
        });
        hospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HM_LoginActivity.class);
                startActivity(intent);

            }
        });
        /*
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });*/
        searchMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserViewMedicineListActivity.class);
                startActivity(intent);
            }
        });
        searchStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserViewStoreListActivity.class);
                startActivity(intent);
            }
        });
        searchHospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserViewHospitalListActivity.class);
                startActivity(intent);
            }
        });
    }

}
