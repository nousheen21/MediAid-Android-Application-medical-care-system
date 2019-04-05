package com.bekar.smartmedicalcare.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bekar.smartmedicalcare.ModelClass.PatientMedicalConditionsModel;
import com.bekar.smartmedicalcare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
public class DialogPatientConditionAdd extends Dialog implements View.OnClickListener {

    private Context context;
    private MaterialButton yes;
    private MaterialButton no;
    private TextInputEditText tvTitle;
    private TextInputEditText tvDiscription;
    private int from;
    private String who;

    private FirebaseFirestore firestore;
    private FirebaseUser user;

    public DialogPatientConditionAdd(@NonNull Context context,String who) {
        super(context);
        this.context=context;
        this.who=who;
        firestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_medical_condition_add);
        yes =  findViewById(R.id.btnYes);
        no =  findViewById(R.id.btnNo);
        tvTitle= findViewById(R.id.tvTitleP);
        tvDiscription=findViewById(R.id.tvDiscriptionP);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnYes){
            dataSet();
        }else {
            dismiss();
        }
    }



    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    private void dataSet() {
        String title=tvTitle.getText().toString().trim();
        String discription=tvDiscription.getText().toString().trim();
        boolean flag=true;
        if(title.isEmpty()) {
            tvTitle.setError("Empty field found");
            flag=false;
        }
        if(discription.isEmpty()){
            tvDiscription.setError("Empty field found");
            flag=false;
        }
        if(flag){
            if(who.equals("Patient")){
                switch (from){
                    case 1:
                        setProblemsPatient(title,discription);
                        break;
                    case 2:
                        setAllergiesPatient(title,discription);
                        break;
                    case 3:
                        setSpecialConditionPatient(title,discription);
                        break;

                }
            }else {
                switch (from){
                    case 1:
                        setEducationDoctor(title,discription);
                        break;
                    case 2:
                        setAchievementst(title,discription);
                        break;
                    case 3:
                        setSpecialities(title,discription);
                        break;

                }
            }
        }
    }

    protected void setProblemsPatient(String title, String discription){
        CollectionReference df=firestore.collection(who).document(user.getUid())
                .collection("Problems");
        String id=df.document().getId();

        PatientMedicalConditionsModel pp=new PatientMedicalConditionsModel(user.getUid(),id,title,discription);
        pp.setType("Problems");
        df.document(id).set(pp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Problem Add Success", Toast.LENGTH_SHORT).show();
                resetData();
                dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    protected void setAllergiesPatient(String title, String discription){
        CollectionReference df=firestore.collection(who).document(user.getUid())
                .collection("Allergies");
        String id=df.document().getId();

        PatientMedicalConditionsModel pp=new PatientMedicalConditionsModel(user.getUid(),id,title,discription);
        pp.setType("Allergies");
        df.document(id).set(pp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Allergies Add Success", Toast.LENGTH_SHORT).show();
                resetData();
                dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    protected void setSpecialConditionPatient(String title, String discription){
        CollectionReference df=firestore.collection(who).document(user.getUid())
                .collection("Special Condition");
        String id=df.document().getId();

        PatientMedicalConditionsModel pp=new PatientMedicalConditionsModel(user.getUid(),id,title,discription);
        pp.setType("Special Condition");
        df.document(id).set(pp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Special Condition Add Success", Toast.LENGTH_SHORT).show();
                resetData();
                dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    protected void setEducationDoctor(String title, String discription){
        CollectionReference df=firestore.collection(who).document(user.getUid())
                .collection("Education");
        String id=df.document().getId();

        PatientMedicalConditionsModel pp=new PatientMedicalConditionsModel(user.getUid(),id,title,discription);
        pp.setType("Education");
        df.document(id).set(pp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Education Add Success", Toast.LENGTH_SHORT).show();
                resetData();
                dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    protected void setAchievementst(String title, String discription){
        CollectionReference df=firestore.collection(who).document(user.getUid())
                .collection("Achievements");
        String id=df.document().getId();

        PatientMedicalConditionsModel pp=new PatientMedicalConditionsModel(user.getUid(),id,title,discription);
        pp.setType("Achievements");
        df.document(id).set(pp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "AAchievements Add Success", Toast.LENGTH_SHORT).show();
                resetData();
                dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    protected void setSpecialities(String title, String discription){
        CollectionReference df=firestore.collection(who).document(user.getUid())
                .collection("Specialities");
        String id=df.document().getId();

        PatientMedicalConditionsModel pp=new PatientMedicalConditionsModel(user.getUid(),id,title,discription);
        pp.setType("Specialities");
        df.document(id).set(pp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Specialities Add Success", Toast.LENGTH_SHORT).show();
                resetData();
                dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void resetData(){
        tvTitle.setText("");
        tvDiscription.setText("");
    }

}
