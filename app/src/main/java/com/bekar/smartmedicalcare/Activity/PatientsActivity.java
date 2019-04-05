package com.bekar.smartmedicalcare.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bekar.smartmedicalcare.Adapter.AdapterPatientList;
import com.bekar.smartmedicalcare.ModelClass.DoctorModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityPatientsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ActivityPatientsBinding binding;
    private AdapterPatientList adapter;
    private List<DoctorModel> patientList;

    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_patients);
        setSupportActionBar(binding.toolbar);

        firestore=FirebaseFirestore.getInstance();

        patientList=new ArrayList<>();
        adapter=new AdapterPatientList(this,patientList);

        RecyclerView.LayoutManager manager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        binding.rvPatientList.setLayoutManager(manager);
        binding.rvPatientList.setHasFixedSize(true);
        binding.rvPatientList.setAdapter(adapter);




        getData();
    }

    private void getData() {
        firestore.collection("All User")
                .whereEqualTo("type","Patient")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds :
                                queryDocumentSnapshots.getDocuments()) {
                            patientList.add(ds.toObject(DoctorModel.class));

                        }
                        adapter.addItems(patientList);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.doctor_search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search By Name");
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("Adapter", "onQueryTextChange: "+ patientList.size());
        filter(newText);
        return true;
    }
    public void filter(String text){
        //Log.d("Adapter ", "filter: "+text);
        List<DoctorModel> list=new ArrayList<>();
        for (int i=0;i<patientList.size();i++) {
            DoctorModel dm=patientList.get(i);
            //Log.d("Adapter ", "filter: "+dm.getSpeciality().trim().toLowerCase());

            if(dm.getFullName().trim().toLowerCase().contains(text.trim().toLowerCase())){
                list.add(dm);
            }
            //Log.d("Adapter ", "filter: "+dm.getSpeciality());
        }
        adapter.addItems(list);
    }
}
