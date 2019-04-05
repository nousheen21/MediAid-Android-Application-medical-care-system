package com.bekar.smartmedicalcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputContentInfo;
import android.widget.Toast;

import com.bekar.smartmedicalcare.ModelClass.MedicalReportModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityMedicalReportAddBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MedicalReportAddActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE=111;
    private static final int PICKFILE_REQUEST_CODE=113;

    ActivityMedicalReportAddBinding binding;

    Context context;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    FirebaseUser user;
    CollectionReference cRef;

    InputStream inputStream;
    String fileName;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_medical_report_add);

        this.context=context;
        this.firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        this.user= FirebaseAuth.getInstance().getCurrentUser();

        this.cRef=firestore.collection("Patient")
                .document(user.getUid())
                .collection("Medical Report");

        filePath="";
        fileName="";

        binding.btnYesR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReport();
            }
        });
        binding.btnNoR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnAddPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){
                    chooseFile();
                }else {
                    requestPermission();
                }
            }
        });
    }

    private void addReport() {

        final String drName=binding.tvDrNameReportAdd.getText().toString().trim();
        final String date=binding.tvDateReportAdd.getText().toString().trim();

        boolean flag=true;

        if(TextUtils.isEmpty(drName)){
            binding.tvDrNameReportAdd.setError("Empty field found");
            flag=false;

        }
        if(TextUtils.isEmpty(date)){
            binding.tvDateReportAdd.setError("Empty field found ");
            flag=false;
        }

        if(inputStream==null){
            Toast.makeText(context, "Please select Prescription", Toast.LENGTH_LONG).show();
            flag=false;
        }

        if(flag){
            long time=System.currentTimeMillis();
            String fName=String.valueOf(time)+fileName;


            final StorageReference sRef= storage.getReference("Patient/"+user.getUid()+"/Prescription/").child(fName);


            UploadTask uploadTask=sRef.putFile(Uri.fromFile(new File(filePath)));
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

                        dataSaveToDatabse(drName,date,downloadUri.toString());

                    } else {
                        // Handle failures
                        // ...

                    }
                }
            });
        }

    }

    private void dataSaveToDatabse(String drName, String date, String toString) {
        String id=cRef.document().getId();

        MedicalReportModel model=new MedicalReportModel(id,user.getUid(),drName,date,toString);

        cRef.document(id).set(model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Report Add Success", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }else {
                            Toast.makeText(getApplicationContext(), "Report upload failed", Toast.LENGTH_SHORT).show();
                            Log.e("Report Add", "onComplete: "+task.getException().getMessage() );
                        }
                    }
                });
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MedicalReportAddActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MedicalReportAddActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MedicalReportAddActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MedicalReportAddActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {
            fileName=getFileName(this,data.getData());
            inputStream=getContentResolver().openInputStream(data.getData());

            binding.btnAddPrescription.setText(fileName);

            saveFile();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // do somthing...


        Log.d("TAG", "onActivityResult: "+data.getData());


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
