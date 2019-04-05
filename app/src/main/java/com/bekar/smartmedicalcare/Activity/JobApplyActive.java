package com.bekar.smartmedicalcare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bekar.smartmedicalcare.ModelClass.JobApplyModel;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.ActivityJobApplyActiveBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JobApplyActive extends AppCompatActivity {

    private final int PERMISSION_REQUEST_CODE=333;
    private final int PICKFILE_REQUEST_CODE=222;

    private InputStream inputStream;
    private String fileName;
    private String filePath;

    private String jobId;
    private String jobPosterId;

    private ActivityJobApplyActiveBinding binding;

    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_job_apply_active);

        filePath="";
        fileName="";

        firestore=FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        storage=FirebaseStorage.getInstance();

        if(getIntent()!=null){
            //flagForIntent=true;
            jobId=getIntent().getStringExtra("jobId");
            jobPosterId=getIntent().getStringExtra("jobPosterId");
        }//else flagForIntent=false;

        binding.btnUploadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){
                    chooseFile();
                }else {
                    requestPermission();
                }
            }
        });

        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobApplyClick();
            }
        });

    }

    private void jobApplyClick() {
        /*final String budget=binding.etBudgetJobApply.getText().toString().trim();
        final String comment=binding.etCommentJobApply.getText().toString().trim();

        boolean flag=true;

        if(TextUtils.isEmpty(budget)){
            binding.etBudgetJobApply.setError("Empty field found");
            flag=false;
        }

        if(TextUtils.isEmpty(comment)){
            binding.etCommentJobApply.setError("Empty field found");
            flag=false;
        }*/
        boolean flag=true;
        if(TextUtils.isEmpty(filePath)){
            //Toast.makeText(this, "Please Upload your CV", Toast.LENGTH_SHORT).show();
            Snackbar.make(binding.layoutJobApply,"Please Upload your CV",Snackbar.LENGTH_LONG).show();
            flag=false;
        }

        if(flag){

            long time=System.currentTimeMillis();
            String fName=String.valueOf(time)+fileName;



            final StorageReference sRef= storage.getReference("JobApply/"+jobId+"/").child(fName);

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

                        dataSaveToDatabse("0","",downloadUri.toString());

                    } else {
                        // Handle failures
                        // ...

                    }
                }
            });



        }
    }

    private void dataSaveToDatabse(String budget, String comment, String toString) {
        CollectionReference cRef= firestore.collection("Job Apply List").document(jobPosterId).collection(jobId);

        String id=cRef.document().getId();

        JobApplyModel obj=new JobApplyModel(id,user.getUid(),user.getDisplayName(),Integer.parseInt(budget),comment,toString);
        obj.setJobPosterId(jobPosterId);
        obj.setJobId(jobId);

        cRef.document(id).set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "onSuccess: Apply Done ...");

                Toast.makeText(JobApplyActive.this, "Apply Done", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: "+e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //onBackPressed();
                    finish();
                }
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(JobApplyActive.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(JobApplyActive.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(JobApplyActive.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(JobApplyActive.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
        intent.setType("application/pdf");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);

        /*new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                .withFilter(Pattern.compile(".*\\.txt$"))
                .withFilterDirectories(true) // Set directories filterable (false by default)
                .start();*/


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            try {
                if(data.getData()!=null){

                    fileName=getFileName(this,data.getData());

                    binding.btnUploadCV.setText(fileName);

                    inputStream=getContentResolver().openInputStream(data.getData());

                    saveFile();

                    if(inputStream!=null) {
                        Log.d("TAG", "onActivityResult: file found");
                    }else {
                        Log.d("TAG", "onActivityResult: null found");
                    }

                    Log.d("TAG", "onActivityResult: "+data.getData());



                }
            }catch (Exception e){
                Log.d("Catch", "onActivityResult: "+e.getMessage());
            }
        }


        /*if (requestCode == 1 && resultCode == RESULT_OK) {
            filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
            
            String ex=filePath.substring(filePath.lastIndexOf('.'));
            
            if(ex.equals(",pdf")){
                String fName= filePath.substring(filePath.lastIndexOf('/')+1) ;
                binding.btnUploadCV.setText(fName);
            }else {
                Toast.makeText(this, "Please Select PDF file", Toast.LENGTH_SHORT).show();
            }
            
            
        }*/


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
