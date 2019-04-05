package com.bekar.smartmedicalcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bekar.smartmedicalcare.MainActivity;
import com.bekar.smartmedicalcare.ModelClass.DoctorProfileInformationModel;
import com.bekar.smartmedicalcare.ModelClass.PatientProfileFullInformation;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.Utils.Utils;
import com.bekar.smartmedicalcare.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySignUpBinding binding;

    //firesbase
    FirebaseFirestore firestore;
    //CollectionReference collectoinRef;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
       binding.btnSignUp.setOnClickListener(this);

       firestore=FirebaseFirestore.getInstance();
        //collectoinRef=firestore.collection("Information");
       auth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        final String fullName=binding.etFullName.getText().toString().trim();
        final String email=binding.etEmail.getText().toString().trim();
        final String password=binding.etPassword.getText().toString().trim();
        String confPassword=binding.etConfirmPassword.getText().toString().trim();
        final String mobileNo=binding.etMobileNo.getText().toString().trim();

        String type="";

        if(binding.rbDoctor.isChecked()) type="Doctor";
        else if(binding.rbPatient.isChecked()) type="Patient";

        boolean flag=true;

        if(fullName.isEmpty()){
            binding.etFullName.setError("Empty found");
            flag=false;
        }
        if(email.isEmpty()){
            binding.etEmail.setError("Empty Found");
            flag=false;

        }else if(!Utils.isEmail(email)){
            binding.etEmail.setError("Please enter a valid E-mail id");
            flag=false;
        }
        if(password.length()<6) {
            binding.etPassword.setError("Password must be 6 charecter");
            flag=false;
        }
        if(!password.equals(confPassword)){
            binding.etConfirmPassword.setError("Password don't match");
            flag=false;
        }
        if(TextUtils.isEmpty(mobileNo)){
            binding.etMobileNo.setError("Empty field found");
            flag=false;
        }

        Log.d("Password", "onClick: "+password);

        if(flag){
            final String finalType = type;
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                final FirebaseUser user = auth.getCurrentUser();

                                UserProfileChangeRequest profielUpdate=new UserProfileChangeRequest.Builder()
                                        .setDisplayName(fullName).build();

                                user.updateProfile(profielUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                            if(finalType.equals("Patient")){
                                                PatientProfileFullInformation info=new PatientProfileFullInformation(user.getUid(),fullName, finalType,email,password);
                                                CollectionReference cf=firestore.collection("Patient").document(user.getUid()).collection("Information");
                                                String id=cf.document().getId();
                                                info.setId(id);
                                                info.setMobileNo(mobileNo);

                                                cf.document(id).set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(SignUpActivity.this, "Sign up success", Toast.LENGTH_SHORT).show();
                                                        Intent intent=new Intent(SignUpActivity.this,DashbordActivity.class);
                                                        intent.putExtra("from","signUp");
                                                        intent.putExtra("type",finalType);
                                                        startActivity(intent);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                                        }else {
                                                            Toast.makeText(SignUpActivity.this, "Failed : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                Toast.makeText(SignUpActivity.this, "Patient", Toast.LENGTH_SHORT).show();

                                            }else if(finalType.equals("Doctor")){

                                                DoctorProfileInformationModel info=new DoctorProfileInformationModel(user.getUid(),fullName,email,password);

                                                CollectionReference cf=firestore.collection(finalType).document(user.getUid()).collection("Information");
                                                String id=cf.document().getId();
                                                info.setId(id);
                                                info.setContactMobile(mobileNo);


                                                cf.document(id).set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(SignUpActivity.this, "Sign up success", Toast.LENGTH_SHORT).show();
                                                        Intent intent=new Intent(SignUpActivity.this,DashbordActivity.class);
                                                        intent.putExtra("from","signUp");
                                                        intent.putExtra("type",finalType);
                                                        startActivity(intent);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                                Toast.makeText(SignUpActivity.this, "Doctor", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(SignUpActivity.this, "Else", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });





                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, ""+task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }

                            // ...
                        }
                    });
        }

    }


}
