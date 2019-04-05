package com.bekar.smartmedicalcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bekar.smartmedicalcare.Adapter.AdapterGridView;
import com.bekar.smartmedicalcare.Dialog.DialogDoctorOnlineStatus;
import com.bekar.smartmedicalcare.MainActivity;
import com.bekar.smartmedicalcare.ModelClass.DoctorModel;
import com.bekar.smartmedicalcare.ModelClass.DoctorProfileInformationModel;
import com.bekar.smartmedicalcare.ModelClass.GridViewItem;
import com.bekar.smartmedicalcare.ModelClass.PatientProfileFullInformation;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityDashbordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class DashbordActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    ActivityDashbordBinding binding;
    private List<GridViewItem> itemList;
    private List<Integer> imageList;
    private List<String> titleList;

    private DialogDoctorOnlineStatus dialog;
    private AdapterGridView adapter;

    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private FirebaseAuth auth;

    private FirebaseAuth.AuthStateListener listener;

    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_dashbord);

        setSupportActionBar(binding.toolbarDashboard);

        userType="";

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        firestore=FirebaseFirestore.getInstance();

        dialog=new DialogDoctorOnlineStatus(this);

        if(getIntent()!=null){
            String from=getIntent().getStringExtra("from");
            if(!TextUtils.isEmpty(from) && from.equals("signUp")){
                String type=getIntent().getStringExtra("type");

                if(type.equals("Doctor")){
                    userType="Doctor";
                    getInfo(userType);

                    dialog.show();

                }else {
                    userType="Patient";
                    getInfo(userType);
                }
                Log.d("dashbord", "onCreate: "+type);
            }else{

                Log.d("dashbord", "onCreate: user id "+user.getUid());

                firestore.collection("All User")
                        .whereEqualTo("userId",user.getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.getDocuments().size()>0){
                                    DoctorModel mm=queryDocumentSnapshots.getDocuments().get(0).toObject(DoctorModel.class);
                                    
                                    userType=mm.getType();
                                }else {
                                    auth.signOut();
                                    auth.addAuthStateListener(DashbordActivity.this);
                                }

                                Log.d("dashbord", "onSuccess: "+userType);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("dashbord", "onFailure: "+e.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            if (userType.equals("Doctor")) dialog.show();
                        }

                        Log.d("dashbord", "onComplete: dashbord");
                    }
                });
            }
        }else {
            Log.d("dashbord", "onCreate: intent null");
        }

        binding.imageSlider.setIndicatorAnimation(IndicatorAnimations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        binding.imageSlider.setScrollTimeInSec(1); //set scroll delay in seconds :

        setSliderViews();

        gridData();
        adapter=new AdapterGridView(this,itemList);
        binding.gridView.setAdapter(adapter);

        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(titleList.get(position).equals("Profile")){

                    if(!TextUtils.isEmpty(userType) && userType.equals("Doctor")){
                        Intent intent=new Intent(getApplicationContext(), DoctorProfile.class);
                        intent.putExtra("userId",user.getUid());
                        startActivity(intent);
                    }else if(!TextUtils.isEmpty(userType) &&userType.equals("Patient")){
                        Intent intent=new Intent(getApplicationContext(), PatientProfile.class);
                        intent.putExtra("userId",user.getUid());
                        startActivity(intent);
                    }else {

                        auth.signOut();
                        finish();

                        Toast.makeText(DashbordActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                    }

                }else if(titleList.get(position).equals("Job Offers")){
                    Intent intent=new Intent(new Intent(getApplicationContext(), EmploymentOpurtunity.class));
                    intent.putExtra("type",userType);
                    startActivity(intent);

                }else if(titleList.get(position).equals("Doctors")){
                    startActivity(new Intent(getApplicationContext(), DoctorOnlineActivity.class));

                }else if(titleList.get(position).equals("Call A Doctor")){
                    startActivity(new Intent(getApplicationContext(),CallADoctorActivity.class));
                }else if(titleList.get(position).equals("Patients")){
                    startActivity(new Intent(getApplicationContext(),PatientsActivity.class));
                }
            }
        });

        Toast.makeText(this, "user id : "+ FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();

    }



    private void getInfo(final String type) {
        firestore.collection(type)
                .document(user.getUid())
                .collection("Information")
        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    CollectionReference cRef=firestore.collection("All User");

                    if(type.equals("Doctor")){
                        DoctorProfileInformationModel model=queryDocumentSnapshots.getDocuments().get(0).toObject(DoctorProfileInformationModel.class);
                        DoctorModel mm=new DoctorModel(user.getUid(),model.getFullName(),type,model.getContactMobile(),model.getSpciality(),model.getProfileImage());
                        String id=cRef.document().getId();

                        mm.setId(id);

                        cRef.document(id).set(mm).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DashbordActivity.this, "all user add success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        PatientProfileFullInformation model=queryDocumentSnapshots.getDocuments().get(0).toObject(PatientProfileFullInformation.class);
                        DoctorModel mm=new DoctorModel(user.getUid(),model.getFullName(),type,model.getMobileNo(),"",model.getProfilePic());
                        String id=cRef.document().getId();
                        mm.setId(id);

                        cRef.document(id).set(mm).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DashbordActivity.this, "all user add success", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }


                }
            }
        });
    }

    private void gridData(){
        itemList=new ArrayList<>();
        imageList=new ArrayList<>();
        titleList=new ArrayList<>();

        imageList.add(R.drawable.ic_boy);
        titleList.add("Profile");
        itemList.add(new GridViewItem(R.drawable.ic_boy,"Profile"));

        imageList.add(R.drawable.ic_job_seeking);
        titleList.add("Job Offers");
        itemList.add(new GridViewItem(R.drawable.ic_job_seeking,"Job Offers"));

        imageList.add(R.drawable.ic_doctor);
        titleList.add("Doctors");
        itemList.add(new GridViewItem(R.drawable.ic_doctor,"Doctors"));

        imageList.add(R.drawable.ic_call_doctor);
        titleList.add("Call A Doctor");
        itemList.add(new GridViewItem(R.drawable.ic_call_doctor,"Call A Doctor"));

        imageList.add(R.drawable.ic_video_call_doctor);
        titleList.add("Video Call A Doctor");
        itemList.add(new GridViewItem(R.drawable.ic_video_call_doctor,"Video Call A Doctor"));

        imageList.add(R.drawable.ic_mental_health);
        titleList.add("Patients");
        itemList.add(new GridViewItem(R.drawable.ic_mental_health,"Patients"));

        imageList.add(R.drawable.ic_hospital_system);
        titleList.add("Hospital Search");
        itemList.add(new GridViewItem(R.drawable.ic_hospital_system,"Hospital Search"));

        imageList.add(R.drawable.ic_pharmecy_system);
        titleList.add("Medicine Search");
        itemList.add(new GridViewItem(R.drawable.ic_pharmecy_system,"Medicine Search"));





    }



    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            switch (i) {
                case 0:

                    sliderView.setImageDrawable(R.drawable.img1);
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.img_2);
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.img3);
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.img4);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            //sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(DashbordActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            binding.imageSlider.addSliderView(sliderView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashbord_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.logout){

            if(user!=null){
                auth.signOut();
                auth.addAuthStateListener(this);
            }else{
                Log.d("TAG", "onOptionsItemSelected: null");
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else {
            Log.d("TAG", "onAuthStateChanged: SIgn out failed");
        }
    }

    @Override
    protected void onDestroy() {


        if(userType.equals("Doctor")){
            statusChange(false);
        }

        Log.d("Destroy", "onDestroy: call");

        super.onDestroy();
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
                                                    Toast.makeText(getApplicationContext(), "You Are Online", Toast.LENGTH_SHORT).show();

                                                }else {
                                                    Toast.makeText(getApplicationContext(), "You Are Offline", Toast.LENGTH_SHORT).show();

                                                }
                                            }else {
                                                Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        }else {
                            Toast.makeText(getApplicationContext(), "not found ...", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
