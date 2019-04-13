package com.bekar.smartmedicalcare.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bekar.smartmedicalcare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserViewDoctorListActivity extends AppCompatActivity {

    TextView textViewCategoryName;
    ListView listViewElements;
    FloatingActionButton addElementButton;
    EditText searchbar;

    DatabaseReference databaseElements;

    List<HM_Element> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hm_add_element_activity);

        FirebaseApp app = FirebaseApp.getInstance("secondary");


        textViewCategoryName = (TextView) findViewById(R.id.management_category);

        addElementButton = (FloatingActionButton) findViewById(R.id.add_Element_button);
        addElementButton.setVisibility(View.INVISIBLE);

        listViewElements = (ListView) findViewById(R.id.listViewElement);

        Intent intent = getIntent();

        elements = new ArrayList<>();

        // for search
        searchbar = (EditText)findViewById(R.id.searchbar);

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    search(s.toString());
                } else {
                    databaseElements.addValueEventListener(valueEventListener);
                }
            }
        });


        String id = intent.getStringExtra(HM_HospitalListActivity.CATEGORY_ID);

        textViewCategoryName.setText("Doctor List");

        databaseElements = FirebaseDatabase.getInstance(app).getReference("Elements").child(id);

        /*addElementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addElementDialogue();
            }
        });*/

    }
    private void search(String s) {
        Query query = databaseElements.orderByChild("elementName")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseElements.addValueEventListener(valueEventListener);
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            elements.clear();

            for (DataSnapshot elementSnapshot: dataSnapshot.getChildren()){
                HM_Element element = elementSnapshot.getValue(HM_Element.class);
                elements.add(element);
            }
            HM_ElementList elementListAdapter = new HM_ElementList(UserViewDoctorListActivity.this, elements);
            listViewElements.setAdapter(elementListAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /*private void showUpdateDialog(final String elementId, final String elementName, final String elementInfo, final String elementDept){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.hm_update_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextInfo = (EditText) dialogView.findViewById(R.id.editTextInfo);
        final EditText editTextDept = (EditText) dialogView.findViewById(R.id.editTextDept);
        final Button updateButton = (Button) dialogView.findViewById(R.id.updateButton);
        final Button deleteButton = (Button) dialogView.findViewById(R.id.deleteButton);

        dialogBuilder.setTitle("Updating " +elementName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String info = editTextInfo.getText().toString().trim();
                String dept = editTextDept.getText().toString().trim();
                if (TextUtils.isEmpty(name)){
                    editTextName.setError("Field empty!");
                    return;
                } else if (TextUtils.isEmpty(info)){
                    editTextInfo.setError("Field empty!");
                    return;
                } else if (TextUtils.isEmpty(dept)){
                    editTextDept.setError("Field empty!");
                    return;
                }
                updateElement(elementId,name,info,dept);
                alertDialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteElement(elementId, elementName);
                alertDialog.dismiss();
            }
        });

    }
    public void addElementDialogue(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.hm_add_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextInfo = (EditText) dialogView.findViewById(R.id.editTextInfo);
        final EditText editTextDept = (EditText) dialogView.findViewById(R.id.editTextDept);
        final Button addButton = (Button) dialogView.findViewById(R.id.addButton);
        final Button cancelButton = (Button) dialogView.findViewById(R.id.cancelButton);

        dialogBuilder.setTitle("New Entry:");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String info = editTextInfo.getText().toString().trim();
                String dept = editTextDept.getText().toString().trim();
                if (TextUtils.isEmpty(name)){
                    editTextName.setError("Field empty!");
                    return;
                } else if (TextUtils.isEmpty(info)){
                    editTextInfo.setError("Field empty!");
                    return;
                } else if (TextUtils.isEmpty(dept)){
                    editTextDept.setError("Field empty!");
                    return;
                }
                addElement(name,info,dept);
                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    private void deleteElement(String elementId, String elementName){
        databaseElements.child(elementId).removeValue();
        Toast.makeText(this,elementName +" is deleted!",Toast.LENGTH_LONG).show();
    }

    private void updateElement(String id, String name, String info, String dept){
        HM_Element hm_element = new HM_Element(id, name, info, dept);
        databaseElements.child(id).setValue(hm_element);

        Toast.makeText(this, "Updated successfully!",Toast.LENGTH_LONG).show();
    }
    public void addElement(String name, String info, String dept){
        String id = databaseElements.push().getKey();
        HM_Element element = new HM_Element(id, name,info,dept);

        databaseElements.child(id).setValue(element);
        Toast.makeText(this, "Added successfully!",Toast.LENGTH_LONG).show();

    }*/

    public void print(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_LONG).show();

    }
}
