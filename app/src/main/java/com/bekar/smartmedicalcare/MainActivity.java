package com.bekar.smartmedicalcare;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bekar.smartmedicalcare.Activity.AdminPanelActivity;
import com.bekar.smartmedicalcare.Activity.DashbordActivity;
import com.bekar.smartmedicalcare.Activity.SignUpActivity;
import com.bekar.smartmedicalcare.Utils.Utils;
import com.bekar.smartmedicalcare.databinding.ActivityMainBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityMainBinding binding;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        FirebaseApp.initializeApp(this);

        //for management database
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:275910108005:android:565f5de895c4dded") // Required for Analytics.
                .setApiKey("AIzaSyDBRPUDQi28OurfEiEWbYGODY3Z5MFSP3s") // Required for Auth.
                .setDatabaseUrl("https://hospital-management-705b9.firebaseio.com") // Required for RTDB.
                .build();

        try {
            FirebaseApp.getInstance("secondary");

        }catch (IllegalStateException e){
            FirebaseApp.initializeApp(this,options,"secondary");

        }



        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.btnSignIn.setOnClickListener(this);
        binding.tvForgetPassword.setOnClickListener(this);
        binding.tvSignUp.setOnClickListener(this);
        auth=FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null){
            Intent intent=new Intent(this,DashbordActivity.class);
            intent.putExtra("from","signIn");
            startActivity(intent);
            finish();
        }

        binding.tvAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminPanelActivity.class));
            }
        });

    }


    @Override
    public void onClick(View v) {

        if(R.id.btnSignIn == v.getId()){
            logIn();
        }else if(R.id.tvSignUp == v.getId()){
            startActivity(new Intent(MainActivity.this,SignUpActivity.class));
        }

    }

    void logIn(){
        String email=binding.etEmail.getText().toString().trim();
        String password=binding.etPassword.getText().toString().trim();

        boolean flag=true;

        if(email.isEmpty()){
            binding.etEmail.setError("Empty found");
            flag=false;
        }else if(!Utils.isEmail(email)) {
            binding.etEmail.setError("PLease Enter a Valid Email");
            flag=false;
        }
        if(password.isEmpty()) {
            binding.etPassword.setError("Empty Found");
            flag=false;
        }



        if(flag){
            Log.d("login", "logIn: "+email+ " "+password);
            auth.signInWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent=new Intent(MainActivity.this,DashbordActivity.class);
                    intent.putExtra("from","signIn");
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Log in success ...", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onFailure: "+e.getMessage());
                }
            }).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(MainActivity.this,DashbordActivity.class));
                        Toast.makeText(MainActivity.this, "Log in success ...", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            //startActivity(new Intent(MainActivity.this,DashbordActivity.class));

        }
    }
}
