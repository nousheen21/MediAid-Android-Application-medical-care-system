package com.bekar.smartmedicalcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bekar.smartmedicalcare.Adapter.PatientViewPagerAdapter;
import com.bekar.smartmedicalcare.Fragments.DoctorChemberInformation;
import com.bekar.smartmedicalcare.Fragments.DoctorMedicalProfile;
import com.bekar.smartmedicalcare.Fragments.DoctorProfileInformation;
import com.bekar.smartmedicalcare.ModelClass.DoctorModel;
import com.bekar.smartmedicalcare.ModelClass.DoctorProfileInformationModel;
import com.bekar.smartmedicalcare.ModelClass.PatientProfileFullInformation;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityDoctorProfileBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DoctorProfile extends AppCompatActivity {

    private final int PERMISSION_REQUEST_CODE=111;

    private ActivityDoctorProfileBinding binding;
    private PatientViewPagerAdapter adapter;
    private FirebaseUser user;

    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    private String userId;

    private InputStream inputStream;
    private String fileName;
    private String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_doctor_profile);
        setSupportActionBar(binding.toolbarDr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filePath="";
        fileName="";

        user= FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();

        if(getIntent()!=null){
            userId=getIntent().getStringExtra("userId");
            Log.d("Profile : ", "onCreate: "+userId);

            if(!user.getUid().equals(userId)){
                binding.btnUploadImageDoneD.setVisibility(View.GONE);
                binding.btnUploadImageD.setVisibility(View.GONE);
            }else userId=user.getUid();


            FirebaseFirestore.getInstance().collection("All User")
                    .whereEqualTo("userId",userId)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.getDocuments().size()>0){
                                DoctorModel model=queryDocumentSnapshots.getDocuments().get(0).toObject(DoctorModel.class);
                                if(model.getProfilePic()==null){

                                    Glide.with(getApplicationContext())
                                            .load(R.drawable.doctor)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(binding.ivProfilePicD);
                                }else {
                                    Glide.with(getApplicationContext())
                                            .load(model.getProfilePic())
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(binding.ivProfilePicD);
                                }
                            }
                            Log.d("Profile", "onSuccess: ");
                        }
                    });


        }


        adapter=new PatientViewPagerAdapter(getSupportFragmentManager());

        Fragment dp=new DoctorProfileInformation();
        Bundle b=new Bundle();
        b.putString("userId",userId);
        dp.setArguments(b);

        Fragment dmp=new DoctorMedicalProfile();
        dmp.setArguments(b);

        Fragment dai=new DoctorChemberInformation();
        dai.setArguments(b);

        adapter.addFragment(dp,"Information");
        adapter.addFragment(dmp,"Profile");
        adapter.addFragment(dai,"Appointment Info");

        binding.viewPagerDoctor.setAdapter(adapter);
        binding.tabLayoutDoctor.setupWithViewPager(binding.viewPagerDoctor);

        binding.btnUploadImageD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        binding.btnUploadImageDoneD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void uploadImage() {

        if(inputStream==null){
            Toast.makeText(this, "Select Image First", Toast.LENGTH_SHORT).show();
            return;
        }

        long time=System.currentTimeMillis();
        String fName=String.valueOf(time)+fileName;


        final StorageReference sRef= storage.getReference("Profile Image/"+user.getUid()+"/").child(fName);

        Uri file=Uri.fromFile(new File(filePath));

        UploadTask uploadTask=sRef.putFile(file);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return sRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    Log.d("Download path", "onComplete: "+downloadUri.toString());

                    dataSaveToDatabse(downloadUri.toString());

                } else {
                    // Handle failures
                    // ...

                }
            }
        });

    }

    private void dataSaveToDatabse(final String toString) {
        //final CollectionReference cf=firestore.collection("Patient").document(user.getUid()).collection("Information");
        final CollectionReference cf=firestore.collection("Doctor")
                .document(user.getUid())
                .collection("Information");

        cf.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size()>0){
                            DoctorProfileInformationModel info=queryDocumentSnapshots.getDocuments().get(0).toObject(DoctorProfileInformationModel.class);

                            info.setProfileImage(toString);

                            cf.document(info.getId()).set(info)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(DoctorProfile.this, "Update Success", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(DoctorProfile.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                    }
                });
        firestore.collection("All User")
                .whereEqualTo("userId",user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size()>0){
                            DoctorModel model=queryDocumentSnapshots.getDocuments().get(0).toObject(DoctorModel.class);
                            model.setProfilePic(toString);

                            firestore.collection("All User").document(model.getId())
                                    .set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(DoctorProfile.this, "Success ..", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(DoctorProfile.this, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

    }

    private void selectImage() {
        if(checkPermission()){
            chooseFile();
        }else {
            requestPermission();
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(DoctorProfile.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(DoctorProfile.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(DoctorProfile.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(DoctorProfile.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseFile();
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    //file chooser
    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(data.getData()!=null){
                fileName = getFileName(this,data.getData());

                inputStream=getContentResolver().openInputStream(data.getData());

                if(inputStream!=null) {
                    Log.d("TAG", "onActivityResult: file found");
                }else {
                    Log.d("TAG", "onActivityResult: null found");
                }

                saveFile();

                Glide.with(getApplicationContext())
                        .load(filePath)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.ivProfilePicD);

                Log.d("TAG", "onActivityResult: "+data.getData());
            }else {
                Log.d("TAG", "onActivityResult: null found");
            }
        }catch (Exception e){
            Log.d("Catch", "onActivityResult: "+e.getMessage());
        }




    }

    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void saveFile()
            throws IOException {

        try {
            File file = new File(getCacheDir(), fileName);
            Log.d("save File", "onActivityResult: "+file.getPath());
            filePath=file.getPath();
            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            } finally {
                output.close();
            }
        } finally {
            inputStream.close();
        }
    }




}
