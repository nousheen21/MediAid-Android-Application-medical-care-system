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

public class MS_StoreRegisterActivity extends AppCompatActivity {
    EditText storeName;
    EditText storeAddress;
    EditText storePIN;
    Button regButton;

    DatabaseReference databaseStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ms_store_register_activity);

        FirebaseApp app = FirebaseApp.getInstance("secondary");

        databaseStore = FirebaseDatabase.getInstance(app).getReference("StoreRegistrationData");

        storeName = (EditText)findViewById(R.id.editTextStoreName);
        storeAddress = (EditText)findViewById(R.id.editTextStoreAddress);
        storePIN = (EditText)findViewById(R.id.storePin);
        regButton = (Button)findViewById(R.id.registerButton);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStore(storeName.getText().toString().trim(),storeAddress.getText().toString().trim(),storePIN.getText().toString().trim());
            }
        });
    }


    private void addStore(String Name, String Address, String PIN){

        if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Address) || TextUtils.isEmpty(PIN)){

            print("Field Empty!");
        }else {
            String id = databaseStore.push().getKey();
            MS_Store store = new MS_Store(id, Name, Address, PIN);
            databaseStore.child(id).setValue(store);
            print("Registration Done!");
            Intent intent = new Intent(getApplicationContext(),MS_LoginActivity.class);
            startActivity(intent);
            print("Login to enter.");
            finish();
            storeName.setText(null);
            storeAddress.setText(null);
            storePIN.setText(null);
        }
    }

    public  void print(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

    }

}
