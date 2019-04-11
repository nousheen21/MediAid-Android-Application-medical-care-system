package com.bekar.smartmedicalcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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

public class MS_StoreListActivity extends AppCompatActivity {
    EditText searchStore;
    DatabaseReference databaseStore;
    public static final String storeName = "storeName";

    ListView listViewStore;
    List<MS_Store> storeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ms_store_list_activity);

        FirebaseApp app = FirebaseApp.getInstance("secondary");

        databaseStore = FirebaseDatabase.getInstance(app).getReference("StoreRegistrationData");

        listViewStore = (ListView) findViewById(R.id.listViewStore);
        storeList = new ArrayList<>();

        // for store search
        searchStore = (EditText) findViewById(R.id.store_search);

        searchStore.addTextChangedListener(new TextWatcher() {
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
                    databaseStore.addValueEventListener(valueEventListener);
                }
            }
        });
        listViewStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MS_Store msStore = storeList.get(position);
                Intent intent = new Intent(getApplicationContext(), MS_RequestedMedicineSupplierViewActivity.class );
                intent.putExtra(storeName,msStore.getStoreName());
                startActivity(intent);

            }
        });
    }
    private void search(String s) {
        Query query = databaseStore.orderByChild("storeName")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addListenerForSingleValueEvent(valueEventListener);
    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseStore.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            storeList.clear();

            for (DataSnapshot storeSnapshot: dataSnapshot.getChildren()){
                MS_Store msStore = storeSnapshot.getValue(MS_Store.class);
                storeList.add(msStore);
            }
            MS_StoreList adapter = new MS_StoreList(MS_StoreListActivity.this,storeList);
            listViewStore.setAdapter(adapter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
