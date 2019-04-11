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

public class HM_LoginActivity extends AppCompatActivity {
    EditText hospitalName;
    EditText hospitalPIN;
    Button signInButton;
    TextView registerView;

    public static final String HOSPITAL_NAME = "hospitalName";

    DatabaseReference databaseHospital;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hm_login_activity);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:275910108005:android:565f5de895c4dded") // Required for Analytics.
                .setApiKey("AIzaSyDBRPUDQi28OurfEiEWbYGODY3Z5MFSP3s") // Required for Auth.
                .setDatabaseUrl("https://hospital-management-705b9.firebaseio.com") // Required for RTDB.
                .build();

        FirebaseApp.initializeApp(this,options,"secondary");
        FirebaseApp app = FirebaseApp.getInstance("secondary");


        hospitalName = (EditText)findViewById(R.id.hospitalNameField);
        hospitalPIN = (EditText)findViewById(R.id.hospitalPinField) ;

        databaseHospital = FirebaseDatabase.getInstance(app).getReference("HospitalRegistrationData");

        signInButton = (Button)findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(hospitalName.getText()) || TextUtils.isEmpty(hospitalPIN.getText())) {

                    print("Field Empty!");
                }else {
                    databaseHospital.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = hospitalName.getText().toString().trim();
                            String pin = hospitalPIN.getText().toString().trim();
                            for (DataSnapshot hospitalSnapshot : dataSnapshot.getChildren()) {
                                HM_Hospital hmHospital = hospitalSnapshot.getValue(HM_Hospital.class);
                                if (hmHospital.getHospitalName().equalsIgnoreCase(name) && hmHospital.getHospitalPIN().equalsIgnoreCase(pin)) {

                                    Intent intent = new Intent(getApplicationContext(), HM_AddCategoryActivity.class);
                                    intent.putExtra(HOSPITAL_NAME, hmHospital.getHospitalName());
                                    startActivity(intent);

                                    print("Login successful");
                                    hospitalName.setText(null);
                                    hospitalPIN.setText(null);
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
                Intent intent = new Intent(getApplicationContext(),HM_HospitalRegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    public  void print(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

    }
}
