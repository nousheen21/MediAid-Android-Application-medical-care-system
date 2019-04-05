package com.bekar.smartmedicalcare.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bekar.smartmedicalcare.Adapter.AdapterOnlineDoctor;
import com.bekar.smartmedicalcare.ModelClass.DoctorModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityCallAdoctorBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CallADoctorActivity extends AppCompatActivity {

    private ActivityCallAdoctorBinding binding;
    FirebaseFirestore firestore;

    List<DoctorModel> modelList;
    AdapterOnlineDoctor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_call_adoctor);
        firestore=FirebaseFirestore.getInstance();

        modelList=new ArrayList<>();
        adapter=new AdapterOnlineDoctor(this,modelList,"CallADoctor");

        RecyclerView.LayoutManager manager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        binding.rvOnlineDoctorList.setHasFixedSize(true);
        binding.rvOnlineDoctorList.setLayoutManager(manager);
        binding.rvOnlineDoctorList.setAdapter(adapter);

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
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
