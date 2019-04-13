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
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

public class MS_LoginActivity extends AppCompatActivity {
    EditText storeName;
    EditText storePIN;
    Button signInButton;
    TextView registerView;
    TextView supplierView;

    public static final String STORE_NAME = "storeName";
    public static final String STORE_ADDRESS = "storeAddress";

    DatabaseReference databaseStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ms_login_activity);

        storeName = (EditText)findViewById(R.id.storeNameField);
        storePIN = (EditText)findViewById(R.id.pinField);


        FirebaseApp app = FirebaseApp.getInstance("secondary");

        databaseStore = FirebaseDatabase.getInstance(app).getReference("StoreRegistrationData");

        signInButton = (Button)findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(storeName.getText()) || TextUtils.isEmpty(storePIN.getText())) {

                    print("Field Empty!");
                }else {
                    databaseStore.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = storeName.getText().toString().trim();
                            String pin = storePIN.getText().toString().trim();
                            for (DataSnapshot storeSnapshot : dataSnapshot.getChildren()) {
                                MS_Store msStore = storeSnapshot.getValue(MS_Store.class);
                                if (msStore.getStoreName().equalsIgnoreCase(name) && msStore.getStorePIN().equalsIgnoreCase(pin)) {

                                    Intent intent = new Intent(getApplicationContext(), MS_AddMedicineActivity.class);
                                    intent.putExtra(STORE_NAME, msStore.getStoreName());
                                    intent.putExtra(STORE_ADDRESS,msStore.getStoreAddress());

                                    startActivity(intent);
                                    print("Login successful");
                                    storeName.setText(null);
                                    storePIN.setText(null);
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
                Intent intent = new Intent(getApplicationContext(),MS_StoreRegisterActivity.class);
                startActivity(intent);
            }
        });

        supplierView = (TextView)findViewById(R.id.supplierViewButton);

        supplierView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MS_StoreListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //databaseStore.addValueEventListener(valueEventListener);
    }

    public  void print(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

    }
}
