package com.bekar.smartmedicalcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bekar.smartmedicalcare.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MS_AddMedicineActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

        EditText searchMedicine;
        DatabaseReference databaseMedicine;
        DatabaseReference databaseRequest;
        DatabaseReference databaseSupplier;

        public static String storeName = "supplierName";
        public static String storeAddress="Add";

        ListView listViewMedicine;
        List<MS_Medicine> medicineList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.ms_add_medicine_activity_nav);

            Intent intent = getIntent();
            storeName = intent.getStringExtra(MS_LoginActivity.STORE_NAME);
            storeAddress = intent.getStringExtra(MS_LoginActivity.STORE_ADDRESS);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(storeName);
            setSupportActionBar(toolbar);


            /*
            FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.fab);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addMedicineDialog();
                }
            });*/

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            //FirebaseApp.initializeApp(this);
            FirebaseApp app = FirebaseApp.getInstance("secondary");

            databaseMedicine = FirebaseDatabase.getInstance(app).getReference("MedicalStoreData");
            databaseRequest = FirebaseDatabase.getInstance(app).getReference("RequestMedicineData");
            databaseSupplier = FirebaseDatabase.getInstance(app).getReference("SupplierData");

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


            //Update or delete list
            listViewMedicine.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    MS_Medicine msMedicine = medicineList.get(position);
                    showUpdateDialog(parent, position, msMedicine.getMedicineId(),
                            msMedicine.getMedicineName(),msMedicine.getCompanyName(),
                            msMedicine.getMedicinePrice(), msMedicine.getAvailable());
                    return true;
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
                    if(msMedicine.getStoreName().equalsIgnoreCase(storeName)) {
                        medicineList.add(msMedicine);
                    }
                }
                MS_MedicineList adapter = new MS_MedicineList(MS_AddMedicineActivity.this,medicineList);
                listViewMedicine.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        private void showUpdateDialog(final AdapterView<?> parent, final int position,
                                      final String medicineId, final String medicineName,
                                      final String companyName, final String medicinePrice,
                                      final String availability){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.ms_update_dialog,null);
            dialogBuilder.setView(dialogView);

            final EditText editName = (EditText) dialogView.findViewById(R.id.editMedicineName);
            final EditText editCompany = (EditText) dialogView.findViewById(R.id.editCompanyName);
            final EditText editPrice = (EditText) dialogView.findViewById(R.id.editPrice);
            final Button updateButton = (Button) dialogView.findViewById(R.id.updateButton);
            final Button deleteButton = (Button) dialogView.findViewById(R.id.deleteButton);
            final Button reqButton = (Button) dialogView.findViewById(R.id.reqButton);
            final Button stockButton = (Button) dialogView.findViewById(R.id.stockButton);

            if(availability.equalsIgnoreCase("StockOut")){
                stockButton.setText("Available");
            }

            dialogBuilder.setTitle("Updating " +medicineName);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = editName.getText().toString().trim();
                    String company = editCompany.getText().toString().trim();
                    String price = editPrice.getText().toString().trim();

                    if (TextUtils.isEmpty(name)){
                        editName.setError("Field empty!");
                        return;
                    }else if (TextUtils.isEmpty(company)){
                        editCompany.setError("Field empty!");
                        return;
                    }else if (TextUtils.isEmpty(price)){
                        editPrice.setError("Field empty!");
                        return;
                    }
                    updateMedicine(medicineId,name,company,price,availability);
                    alertDialog.dismiss();
                }
            });

            reqButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reqMedicine(medicineName,companyName);
                    alertDialog.dismiss();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMedicine(medicineId, medicineName);
                    alertDialog.dismiss();
                }
            });
            stockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (availability.equalsIgnoreCase("Available")) {
                        updateMedicine(medicineId, medicineName, companyName, medicinePrice, "StockOut");
                        stockButton.setText("Available");
                    } else {
                        updateMedicine(medicineId, medicineName, companyName, medicinePrice, "Available");
                        stockButton.setText("StockOut");

                    }
                }
            });

        }

        private void showRequestDialog(){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            LayoutInflater inflater = getLayoutInflater();
            final View reqDialogView = inflater.inflate(R.layout.ms_request_dialog,null);
            dialogBuilder.setView(reqDialogView);

            final EditText reqName = (EditText) reqDialogView.findViewById(R.id.reqMedicineName);
            final EditText reqCompany = (EditText) reqDialogView.findViewById(R.id.reqCompanyName);
            final Button requestButton = (Button) reqDialogView.findViewById(R.id.reqButton);
            final Button cancelButton = (Button) reqDialogView.findViewById(R.id.cancelButton);

            dialogBuilder.setTitle("Request Medicine:");
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = reqName.getText().toString().trim();
                    String company = reqCompany.getText().toString().trim();

                    if (TextUtils.isEmpty(name)){
                        reqName.setError("Field empty!");
                        return;
                    }else if (TextUtils.isEmpty(company)){
                        reqCompany.setError("Field empty!");
                        return;
                    }
                    reqMedicine(name,company);
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

    private void addMedicineDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View addDialogView = inflater.inflate(R.layout.ms_medicine_add_dialog,null);
        dialogBuilder.setView(addDialogView);

        final EditText Name = (EditText) addDialogView.findViewById(R.id.addMedicineName);
        final EditText Company = (EditText) addDialogView.findViewById(R.id.addCompanyName);
        final EditText Price = (EditText) addDialogView.findViewById(R.id.addPrice);

        final Button addButton = (Button) addDialogView.findViewById(R.id.addButton);
        final Button cancelButton = (Button) addDialogView.findViewById(R.id.cancelButton);

        dialogBuilder.setTitle("Add new medicine:");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString().trim();
                String company = Company.getText().toString().trim();
                String price = Price.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Name.setError("Field empty!");
                    return;
                }else if (TextUtils.isEmpty(company)){
                    Company.setError("Field empty!");
                    return;
                }else if (TextUtils.isEmpty(price)){
                    Price.setError("Field empty!");
                    return;
                }
                addMedicine(name,company,price,"Available");
                Name.setText(null);
                Company.setText(null);
                Price.setText(null);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    private void deleteMedicine(String medicineId, String medicineName){
            DatabaseReference drMedicine = FirebaseDatabase.getInstance().getReference("MedicalStoreData").child(medicineId);

            drMedicine.removeValue();

            Toast.makeText(this,"Medicine " + medicineName +" is deleted!",Toast.LENGTH_LONG).show();
        }

        private void updateMedicine(String id, String name, String company, String price, String availability){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MedicalStoreData").child(id);

            MS_Medicine ms_medicine = new MS_Medicine(id, name, company, price,storeName,storeAddress,availability);
            databaseReference.setValue(ms_medicine);

            Toast.makeText(this, "Updated successfully!",Toast.LENGTH_LONG).show();
        }

        private void addMedicine(String name, String company, String price, String availability){

                String id = databaseMedicine.push().getKey();
                MS_Medicine medicine = new MS_Medicine(id,name,company,price,storeName,storeAddress,availability);
                databaseMedicine.child(id).setValue(medicine);

                Toast.makeText(this,"Medicine added!",Toast.LENGTH_LONG).show();
        }

        private void reqMedicine(String name,String company){

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(company)){

                String id = databaseRequest.push().getKey();
                MS_RequestMedicine requestMedicine = new MS_RequestMedicine(id,name,company,storeName);
                databaseRequest.child(id).setValue(requestMedicine);

                Toast.makeText(this,"Medicine requested!",Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(this, "Field Empty!", Toast.LENGTH_LONG).show();
            }
        }

        //ms_nav

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ms_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_addMed) {
            addMedicineDialog();

        } else if (id == R.id.nav_reqMed) {
            showRequestDialog();

        } else if (id == R.id.nav_reqList) {
            Intent intent = new Intent(this,MS_RequestedMedicineActivity.class);
            intent.putExtra(storeName,storeName);
            startActivity(intent);

        } else if (id == R.id.nav_supplierList) {
            Intent intent = new Intent(this,MS_AddSupplierActivity.class);
            intent.putExtra(storeName,storeName);
            startActivity(intent);

        } else if (id == R.id.nav_signOut) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
