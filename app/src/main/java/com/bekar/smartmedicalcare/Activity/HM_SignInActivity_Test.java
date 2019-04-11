package com.bekar.smartmedicalcare.Activity;
/*
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HM_SignInActivity_Test extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private Button signInButton;
    private Button registerButton;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hm_sign_in_activity);

        emailField=(EditText)findViewById(R.id.emailField);
        passwordField=(EditText)findViewById(R.id.passwordField);
        signInButton=(Button)findViewById(R.id.signInButton);
        forgotPassword=(TextView)findViewById(R.id.forgotPassword);

        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            finish();
            startActivity(new Intent(HM_SignInActivity_Test.this, HM_AddCategoryActivity.class));
        }
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(emailField.getText().toString(),passwordField.getText().toString());
            }
        });
        /*forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HM_SignInActivity_Test.this, ForgotPassword.class));
            }
        });
    }
    private void validate(String email, String password){
        progressDialog.setMessage("Logging In");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(HM_SignInActivity_Test.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HM_SignInActivity_Test.this, HM_AddCategoryActivity.class));
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(HM_SignInActivity_Test.this, "Email or Password Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
*/