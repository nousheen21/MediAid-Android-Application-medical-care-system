package com.bekar.smartmedicalcare.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bekar.smartmedicalcare.Adapter.AdapterOnlineDoctor;
import com.bekar.smartmedicalcare.ModelClass.DoctorModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityDoctorOnlineBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DoctorOnlineActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ActivityDoctorOnlineBinding binding;
    FirebaseFirestore firestore;

    List<DoctorModel> modelList;
    AdapterOnlineDoctor adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_doctor_online);
        setSupportActionBar(binding.toolbar);
        firestore=FirebaseFirestore.getInstance();

        modelList=new ArrayList<>();
        adapter=new AdapterOnlineDoctor(this,modelList,"Doctor");

        RecyclerView.LayoutManager manager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        binding.rvDoctorList.setHasFixedSize(true);
        binding.rvDoctorList.setLayoutManager(manager);
        binding.rvDoctorList.setAdapter(adapter);

        binding.btnActiveDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDoctorList(true);
            }
        });

        binding.btnInactiveDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               allDoctor();
            }
        });
        showDoctorList(true);
    }

    private void showDoctorList(boolean b) {
        modelList.clear();
        firestore.collection("All User")
                .whereEqualTo("active",b)
                .whereEqualTo("type","Doctor")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds :
                                queryDocumentSnapshots.getDocuments()) {
                            modelList.add(ds.toObject(DoctorModel.class));

                        }
                        adapter.addItems(modelList);
                    }
                });
    }

    private void allDoctor(){
        modelList.clear();
        firestore.collection("All User")
                .whereEqualTo("type","Doctor")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds :
                                queryDocumentSnapshots.getDocuments()) {
                            modelList.add(ds.toObject(DoctorModel.class));

                        }
                        adapter.addItems(modelList);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.doctor_search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search By Speciality");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("Adapter", "onQueryTextChange: "+ modelList.size());
        filter(newText);
        return true;
    }
    public void filter(String text){
        //Log.d("Adapter ", "filter: "+text);
        List<DoctorModel> list=new ArrayList<>();
        for (int i=0;i<modelList.size();i++) {
            DoctorModel dm=modelList.get(i);
            //Log.d("Adapter ", "filter: "+dm.getSpeciality().trim().toLowerCase());

            if(!TextUtils.isEmpty(dm.getSpeciality())){
                if(dm.getSpeciality().trim().toLowerCase().contains(text.trim().toLowerCase())){
                    list.add(dm);
                }
            }


            //Log.d("Adapter ", "filter: "+dm.getSpeciality());
        }
        adapter.addItems(list);
    }
}
