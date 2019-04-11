package com.bekar.smartmedicalcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bekar.smartmedicalcare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MS_AddSupplierActivity extends AppCompatActivity {

    EditText searchSupplier;
    FloatingActionButton addButton;

    DatabaseReference databaseSupplier;

    private String storeName = "StoreName";

    ListView listViewSupplier;
    List<MS_Supplier> supplierList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ms_add_supplier_activity);

        Intent intent = getIntent();
        storeName = intent.getStringExtra(MS_AddMedicineActivity.storeName);


        FirebaseApp app = FirebaseApp.getInstance("secondary");
        databaseSupplier = FirebaseDatabase.getInstance(app).getReference("SupplierData");

        listViewSupplier = (ListView) findViewById(R.id.listViewSupplier);
        supplierList = new ArrayList<>();

    // for supplier search
        searchSupplier = (EditText)findViewById(R.id.supplier_search);

        searchSupplier.addTextChangedListener(new TextWatcher() {
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
                    databaseSupplier.addValueEventListener(valueEventListener);
                }
            }
        });

        addButton = (FloatingActionButton) findViewById(R.id.button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

    //Update or delete list
        listViewSupplier.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                MS_Supplier msSupplier = supplierList.get(position);

                showUpdateDialog(msSupplier.getSupplierId(),msSupplier.getSupplierName(),msSupplier.getSupplierCompany());
                return true;
            }
        });
    }

    private void search(String s) {
        Query query = databaseSupplier.orderByChild("supplierName")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseSupplier.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            supplierList.clear();

            for (DataSnapshot supplierSnapshot: dataSnapshot.getChildren()){
                MS_Supplier msSupplier = supplierSnapshot.getValue(MS_Supplier.class);
                if(msSupplier.getStoreName().equalsIgnoreCase(storeName)){
                    supplierList.add(msSupplier);
                }
            }
            MS_SupplierList adapter = new MS_SupplierList(MS_AddSupplierActivity.this,supplierList);
            listViewSupplier.setAdapter(adapter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void showUpdateDialog(final String supplierId, final String supplierName, final String supplierCompany){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.ms_supplier_update_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText editSupplierName = (EditText) dialogView.findViewById(R.id.editSupplierName);
        final EditText editSupplierCompany = (EditText) dialogView.findViewById(R.id.editSupplierCompany);
        final Button updateButton = (Button) dialogView.findViewById(R.id.updateButton);
        final Button deleteButton = (Button) dialogView.findViewById(R.id.deleteButton);

        dialogBuilder.setTitle("Updating " +supplierName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editSupplierName.getText().toString().trim();
                String company = editSupplierCompany.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    editSupplierName.setError("Field empty!");
                    return;
                }else if (TextUtils.isEmpty(company)){
                    editSupplierCompany.setError("Field empty!");
                    return;
                }
                updateSupplier(supplierId,name,company);
                alertDialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSupplier(supplierId, supplierName);
                alertDialog.dismiss();
            }
        });

    }


    private void deleteSupplier(String supplierId, String supplierName){
        DatabaseReference drSupplier = FirebaseDatabase.getInstance().getReference("SupplierData").child(supplierId);

        drSupplier.removeValue();

        Toast.makeText(this,"Supplier " + supplierName +" is deleted!",Toast.LENGTH_LONG).show();
    }

    private void updateSupplier(String id, String name, String company){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SupplierData").child(id);

        MS_Supplier ms_supplier = new MS_Supplier(id, name, company, storeName);
        databaseReference.setValue(ms_supplier);

        print("Updated successfully!");
    }

    private void showAddDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.ms_supplier_add_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText editSupplierName = (EditText) dialogView.findViewById(R.id.editSupplierName);
        final EditText editSupplierCompany = (EditText) dialogView.findViewById(R.id.editSupplierCompany);
        final Button addButton = (Button) dialogView.findViewById(R.id.addButton);
        final Button cancelButton = (Button) dialogView.findViewById(R.id.cancelButton);

        dialogBuilder.setTitle("Enter supplier information:");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editSupplierName.getText().toString().trim();
                String company = editSupplierCompany.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    editSupplierName.setError("Field empty!");
                    return;
                }else if (TextUtils.isEmpty(company)){
                    editSupplierCompany.setError("Field empty!");
                    return;
                }
                addSupplier(name,company);
                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    private void addSupplier(String supplierName, String supplierCompany){

            String id = databaseSupplier.push().getKey();
            MS_Supplier supplier = new MS_Supplier(id,supplierName,supplierCompany,storeName);
            databaseSupplier.child(id).setValue(supplier);

    }
    public void print(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

}
