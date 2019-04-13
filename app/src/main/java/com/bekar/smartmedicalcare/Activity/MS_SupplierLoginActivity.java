package com.bekar.smartmedicalcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bekar.smartmedicalcare.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

public class MS_SupplierLoginActivity extends AppCompatActivity {
    EditText supplierName;
    EditText supplierPIN;
    Button signInButton;
    TextView registerView;

    public static final String SUPPLIER_NAME = "supplierName";
    public static final String COMPANY_NAME = "companyName";

    DatabaseReference databaseSupplier;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ms_supplier_login_activity);

        supplierName = (EditText)findViewById(R.id.supplierNameField);
        supplierPIN = (EditText)findViewById(R.id.pinField);


        FirebaseApp app = FirebaseApp.getInstance("secondary");

        databaseSupplier = FirebaseDatabase.getInstance(app).getReference("SupplierRegistrationData");

        signInButton = (Button)findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(supplierName.getText()) || TextUtils.isEmpty(supplierPIN.getText())) {

                    print("Field Empty!");
                }else {
                    databaseSupplier.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = supplierName.getText().toString().trim();
                            String pin = supplierPIN.getText().toString().trim();
                            for (DataSnapshot storeSnapshot : dataSnapshot.getChildren()) {
                                MS_SupplierReg supplier = storeSnapshot.getValue(MS_SupplierReg.class);
                                if (supplier.getSupplierName().equalsIgnoreCase(name) && supplier.getSupplierPIN().equalsIgnoreCase(pin)) {

                                    Intent intent = new Intent(getApplicationContext(), MS_StoreListActivity.class);
                                    intent.putExtra(SUPPLIER_NAME, supplier.getSupplierName());
                                    intent.putExtra(COMPANY_NAME,supplier.getSupplierCompany());

                                    startActivity(intent);
                                    print("Login successful");
                                    supplierName.setText(null);
                                    supplierPIN.setText(null);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        registerView = (TextView)findViewById(R.id.registerViewButton);

        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MS_SupplierRegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //databaseSupplier.addValueEventListener(valueEventListener);
    }

    public  void print(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

    }
}
