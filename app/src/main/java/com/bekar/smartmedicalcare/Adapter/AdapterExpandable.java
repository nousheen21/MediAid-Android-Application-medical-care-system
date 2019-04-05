package com.bekar.smartmedicalcare.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import com.bekar.smartmedicalcare.Dialog.DialogPatientConditionAdd;
import com.bekar.smartmedicalcare.ModelClass.PatientMedicalConditionsModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.PatientConditionExlistChildBinding;
import com.bekar.smartmedicalcare.databinding.PatientConditionExlistParentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

public class AdapterExpandable extends BaseExpandableListAdapter {
    private List<String> parentList;
    private HashMap<String,List<PatientMedicalConditionsModel>> childList;
    DialogPatientConditionAdd dialog;
    private Context context;
    private FirebaseFirestore firestore;

    protected PatientConditionExlistParentBinding parentBinding;
    protected PatientConditionExlistChildBinding childBinding;
    protected LayoutInflater infleter;
    protected String  userId;

    protected FirebaseUser user;

    public AdapterExpandable(Context mContext,
                             List<String> parentList,
                             HashMap<String,
                                     List<PatientMedicalConditionsModel>> childList,
                             String who,String userId) {
        this.parentList = parentList;
        this.childList = childList;
        this.context=mContext;
        this.userId=userId;

        user= FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();
        dialog=new DialogPatientConditionAdd(context,who);
        infleter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //Log.d("TAG", "getChildrenCount: "+childList.get(parentList.get(groupPosition)).size());
        return childList.get(parentList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(parentList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        infleter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=infleter.inflate(R.layout.patient_condition_exlist_parent,parent,false);
        parentBinding = DataBindingUtil.bind(convertView);

        parentBinding.tvTitle.setText(parentList.get(groupPosition));

        if(userId.equals(user.getUid())){
            parentBinding.btnAddOption.setVisibility(View.VISIBLE);
        }else {
            parentBinding.btnAddOption.setVisibility(View.GONE);
        }

        parentBinding.btnAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setFrom(groupPosition+1);
                dialog.show();
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        infleter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infleter.inflate(R.layout.patient_condition_exlist_child,parent,false);
        childBinding = DataBindingUtil.bind(convertView);

        final PatientMedicalConditionsModel obj= (PatientMedicalConditionsModel) getChild(groupPosition,childPosition);
        if(userId.equals(user.getUid())){
            childBinding.btnDeleteChild.setVisibility(View.VISIBLE);
        }else {
            childBinding.btnDeleteChild.setVisibility(View.GONE);
        }
        childBinding.tvTitle.setText(obj.getTitle());
        childBinding.tvDescription.setText(obj.getDiscription());

        Log.d("TAG ","getChildView: here");

        childBinding.btnDeleteChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChild(obj);
            }
        });

        return convertView;
    }

    private void deleteChild(PatientMedicalConditionsModel obj) {
        firestore.collection("Patient").document(user.getUid())
                .collection(obj.getType())
                .document(obj.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("TAG", "onComplete: delete");
                        }
                    }
                });
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
