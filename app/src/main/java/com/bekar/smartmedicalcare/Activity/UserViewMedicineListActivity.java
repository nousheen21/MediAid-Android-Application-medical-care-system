package com.bekar.smartmedicalcare.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.bekar.smartmedicalcare.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class UserViewMedicineListActivity extends AppCompatActivity {
    EditText searchMedicine;
    DatabaseReference databaseMedicine;

    ListView listViewMedicine;
    List<MS_Medicine> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userview_medicine_list_activity);

        FirebaseApp app = FirebaseApp.getInstance("secondary");

        databaseMedicine = FirebaseDatabase.getInstance(app).getReference("MedicalStoreData");

        listViewMedicine = (ListView) findViewById(R.id.listViewMedicine);
        medicineList = new ArrayList<>();

        // for medicine search
        searchMedicine = (EditText)findViewById(R.id.medicine_search);

        searchMedicine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    search(s.toString());
                } else {
                    databaseMedicine.addValueEventListener(valueEventListener);
                }
            }
        });

    }
    private void search(String s) {
        Query query = databaseMedicine.orderByChild("medicineName")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addListenerForSingleValueEvent(valueEventListener);
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
                MS_Medicine msMedicine = medicineSnapshot.getValue(MS_Medicine.class);
                medicineList.add(msMedicine);
            }
            UserViewMedicineList adapter = new UserViewMedicineList(UserViewMedicineListActivity.this,medicineList);
            listViewMedicine.setAdapter(adapter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
