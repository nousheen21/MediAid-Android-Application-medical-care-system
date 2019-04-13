package com.bekar.smartmedicalcare.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bekar.smartmedicalcare.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class HM_AddCategoryActivity extends AppCompatActivity {

    public static final String CATEGORY_NAME = "Category_name";
    public static final String CATEGORY_ID = "Category_id";
    private String hospitalName = "hospitalName";

    EditText categoryName;
    Button addButton;
    DatabaseReference databaseHMCategory;

    ListView listViewCategory;
    List<HM_Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hm_add_category_activity);

        Intent intent = getIntent();
        hospitalName = intent.getStringExtra(HM_LoginActivity.HOSPITAL_NAME);

        FirebaseApp app = FirebaseApp.getInstance("secondary");

        databaseHMCategory = FirebaseDatabase.getInstance(app).getReference("HospitalData");

        categoryName = (EditText) findViewById(R.id.management_category);
        addButton = (Button) findViewById(R.id.add_category_button);
        listViewCategory = (ListView) findViewById(R.id.listViewCategory);
        categoryList = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });

        listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HM_Category hmCategory = categoryList.get(position);
                Intent intent = new Intent(getApplicationContext(), HM_AddElementActivity.class);

                intent.putExtra(CATEGORY_ID,hmCategory.getCategoryId());
                intent.putExtra(CATEGORY_NAME,hmCategory.getCategoryName());

                startActivity(intent);
            }
        });
        listViewCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                HM_Category hmCategory = categoryList.get(position);

                showUpdateDialog(hmCategory.getCategoryId(),hmCategory.getCategoryName());
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseHMCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                categoryList.clear();

                for (DataSnapshot categorySnapshot: dataSnapshot.getChildren()){
                    HM_Category hmCategory = categorySnapshot.getValue(HM_Category.class);
                    if(hmCategory.getHospitalName().equalsIgnoreCase(hospitalName)) {
                        categoryList.add(hmCategory);
                    }
                }
                HM_CategoryList adapter = new HM_CategoryList(HM_AddCategoryActivity.this,categoryList);
                listViewCategory.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String categoryId, final String categoryName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.hm_update_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button updateButton = (Button) dialogView.findViewById(R.id.updateButton);
        final Button deleteButton = (Button) dialogView.findViewById(R.id.deleteButton);

        dialogBuilder.setTitle("Updating " +categoryName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                if (TextUtils.isEmpty(name)){
                    editText.setError("Field empty!");
                    return;
                }
                updateCategory(categoryId,name);
                alertDialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory(categoryId, categoryName);
                alertDialog.dismiss();
            }
        });

    }

    private void deleteCategory(String categoryId, String categoryName){
        FirebaseApp app = FirebaseApp.getInstance("secondary");
        DatabaseReference drCategory = FirebaseDatabase.getInstance(app).getReference("HospitalData").child(categoryId);
        DatabaseReference drElement = FirebaseDatabase.getInstance().getReference("Elements").child(categoryId);

        drCategory.removeValue();
        drElement.removeValue();

        Toast.makeText(this,"Category " + categoryName +" is deleted!",Toast.LENGTH_LONG).show();
    }

    private void updateCategory(String id, String name){
        FirebaseApp app = FirebaseApp.getInstance("secondary");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(app).getReference("HospitalData").child(id);

        HM_Category hm_category = new HM_Category(id, name, hospitalName);
        databaseReference.setValue(hm_category);

        Toast.makeText(this, "Updated successfully!",Toast.LENGTH_LONG).show();
    }

    private void addCategory(){
        String name = categoryName.getText().toString().trim();

        if (!TextUtils.isEmpty(name)){

            String id = databaseHMCategory.push().getKey();
            HM_Category Category = new HM_Category(id,name,hospitalName);
            databaseHMCategory.child(id).setValue(Category);

            Toast.makeText(this,"Category added!",Toast.LENGTH_LONG).show();
            categoryName.setText(null);
        }else {
            Toast.makeText(this, "Enter a Category name!", Toast.LENGTH_LONG).show();
        }
    }
    public void print(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
