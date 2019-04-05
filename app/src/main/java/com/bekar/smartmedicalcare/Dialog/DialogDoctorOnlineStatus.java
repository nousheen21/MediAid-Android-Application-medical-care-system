package com.bekar.smartmedicalcare.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bekar.smartmedicalcare.ModelClass.DoctorModel;
import com.bekar.smartmedicalcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;

public class DialogDoctorOnlineStatus extends Dialog implements View.OnClickListener {

    Context context;
    MaterialButton btnOnline;
    MaterialButton btnOffline;

    FirebaseFirestore firestore;
    FirebaseUser user;

    public DialogDoctorOnlineStatus(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.online_status);

        btnOnline=findViewById(R.id.btnOnline);
        btnOffline=findViewById(R.id.btnOffline);

        btnOffline.setOnClickListener(this);
        btnOnline.setOnClickListener(this);

        firestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnOnline){
            statusChange(true);
        }else {
            statusChange(false);
        }
    }

    protected void statusChange(final boolean flag){
        firestore.collection("All User")
                .whereEqualTo("userId",user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size()>0){
                            DoctorModel model=queryDocumentSnapshots.getDocuments().get(0).toObject(DoctorModel.class);

                            model.setActive(flag);

                            firestore.collection("All User")
                                    .document(model.getId())
                                    .set(model)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                if(flag){
                                                    Toast.makeText(context, "You Are Online", Toast.LENGTH_SHORT).show();

                                                }else {
                                                    Toast.makeText(context, "You Are Offline", Toast.LENGTH_SHORT).show();

                                                }
                                            }else {
                                                Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show();
                                            }
                                            dismiss();
                                        }
                                    });
                        }else {
                            Toast.makeText(context, "not found ...", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }
                });
    }

}
