package com.bekar.smartmedicalcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bekar.smartmedicalcare.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

public class MS_SupplierRegisterActivity extends AppCompatActivity {
    EditText supplierName;
    EditText companyName;
    EditText supplierPIN;
    Button regButton;

    DatabaseReference databaseSupplier;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ms_supplier_register_activity);

        FirebaseApp app = FirebaseApp.getInstance("secondary");

        databaseSupplier = FirebaseDatabase.getInstance(app).getReference("SupplierRegistrationData");

        supplierName = (EditText)findViewById(R.id.editTextSupplierName);
        companyName = (EditText)findViewById(R.id.editTextCompanyName);
        supplierPIN = (EditText)findViewById(R.id.supplierPin);
        regButton = (Button)findViewById(R.id.registerButton);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSupplier(supplierName.getText().toString().trim(), companyName.getText().toString().trim(), supplierPIN.getText().toString().trim());
            }
        });
    }


    private void addSupplier(String Name, String Company, String PIN){

        if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Company) || TextUtils.isEmpty(PIN)){

            print("Field Empty!");
        }else {
            String id = databaseSupplier.push().getKey();
            MS_SupplierReg supplier = new MS_SupplierReg(id, Name, Company, PIN);
            databaseSupplier.child(id).setValue(supplier);
            print("Registration Done!");
            Intent intent = new Intent(getApplicationContext(),MS_SupplierLoginActivity.class);
            startActivity(intent);
            print("Login to enter.");
            finish();
            supplierName.setText(null);
            companyName.setText(null);
            supplierPIN.setText(null);
        }
    }

    public  void print(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

    }

}
