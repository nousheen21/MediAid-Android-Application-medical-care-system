package com.bekar.smartmedicalcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.bekar.smartmedicalcare.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MS_RequestedMedicineActivity extends AppCompatActivity {

    DatabaseReference databaseMedicine;

    ListView listViewMedicine;
    List<MS_RequestMedicine> medicineList;
    private String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ms_requested_medicine_activity);

        Intent intent = getIntent();
        storeName = intent.getStringExtra(MS_AddMedicineActivity.storeName);

        setTitle(storeName);

        FirebaseApp app = FirebaseApp.getInstance("secondary");

        databaseMedicine = FirebaseDatabase.getInstance(app).getReference("RequestMedicineData");

        listViewMedicine = (ListView) findViewById(R.id.listViewMedicine);
        medicineList = new ArrayList<>();
    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseMedicine.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            medicineList.clear();

            for (DataSnapshot medicineSnapshot: dataSnapshot.getChildren()){
                MS_RequestMedicine msRequestMedicine = medicineSnapshot.getValue(MS_RequestMedicine.class);

                if(msRequestMedicine.getStoreName().equalsIgnoreCase(storeName)){
                    medicineList.add(msRequestMedicine);
                }
            }
            MS_RequestedMedicineList adapter = new MS_RequestedMedicineList(MS_RequestedMedicineActivity.this,medicineList);

            listViewMedicine.setAdapter(adapter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
