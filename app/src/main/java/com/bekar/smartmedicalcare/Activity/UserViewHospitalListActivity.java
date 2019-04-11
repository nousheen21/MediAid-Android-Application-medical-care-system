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

public class UserViewHospitalListActivity extends AppCompatActivity {
    EditText searchHospital;
    DatabaseReference databaseHospital;

    ListView listViewHospital;
    List<HM_Hospital> hospitalList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hm_hospital_list_activity);

        FirebaseApp app = FirebaseApp.getInstance("secondary");

        databaseHospital = FirebaseDatabase.getInstance(app).getReference("HospitalRegistrationData");

        listViewHospital = (ListView) findViewById(R.id.listViewHospital);
        hospitalList = new ArrayList<>();

        // for hospital search
        searchHospital = (EditText) findViewById(R.id.hospital_search);

        searchHospital.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    search(s.toString());
                } else {
                    databaseHospital.addValueEventListener(valueEventListener);
                }
            }
        });
        /*listViewStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MS_Store msStore = storeList.get(position);
                Intent intent = new Intent(getApplicationContext(),MS_RequestedMedicineSupplierViewActivity.class );

                startActivity(intent);
            }
        });*/
    }
    private void search(String s) {
        Query query = databaseHospital.orderByChild("hospitalName")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addListenerForSingleValueEvent(valueEventListener);
    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseHospital.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            hospitalList.clear();

            for (DataSnapshot hospitalSnapshot: dataSnapshot.getChildren()){
                HM_Hospital hmHospital = hospitalSnapshot.getValue(HM_Hospital.class);
                hospitalList.add(hmHospital);
            }
            HM_HospitalList adapter = new HM_HospitalList(UserViewHospitalListActivity.this,hospitalList);
            listViewHospital.setAdapter(adapter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
