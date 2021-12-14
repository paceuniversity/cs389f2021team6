package com.example.onehomereset.Drawer;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.onehomereset.ImageActivity;
import com.example.onehomereset.R;
import com.example.onehomereset.Upload;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class AddItemFragment extends Fragment {

        private static final int PICK_IMAGE_REQUEST = 1;
        private Button mButtonChooseImage, mButtonupload, mButtonShowUploads, mCaptureBtn;
        private EditText mEditTextFileName, mEditTextPhoneNum, mEditDescription;
        private ImageView mImageView;
        private ProgressBar mProgressBar;
        private String CategoryName, Description, PhoneNumber, ProductName, saveCurrentDate, saveCurrentTime;
        private String productRandomKey,downloadImageUrl;
        private String imageUrl;


        private Uri mImageUri;

        private StorageReference mStorageRef;
        private DatabaseReference mDatabaseRef;
        private StorageTask mUploadTask,mDownload;
        private ProgressDialog mLoadingBar;

        String currentImagePath = null;
        private static final int IMAGE_REQUEST = 1;

        final StorageReference mImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");




    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_additem , container , false);


            mButtonChooseImage = (Button) view.findViewById(R.id.button_choose_image);
            mButtonupload = (Button) view.findViewById(R.id.button_upload);
            mButtonShowUploads = (Button) view.findViewById(R.id.btn_show_uploads);
            mEditTextFileName = (EditText) view.findViewById(R.id.edit_text_file_name);
            mImageView = (ImageView) view.findViewById(R.id.image_view);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            mEditTextPhoneNum = (EditText) view.findViewById(R.id.edit_text_phonenum);
            mEditDescription = (EditText) view.findViewById(R.id.edit_description);
            mCaptureBtn = (Button) view.findViewById(R.id.btn_camera);
            mLoadingBar = new ProgressDialog(getActivity());

            mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

            mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFileChooser();
                }
            });
            mButtonupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(getActivity() , "Upload in Progress" , Toast.LENGTH_SHORT).show();
                    }
                    ValidateProductData();

                }
            });
            mButtonShowUploads.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        openImageActivity();
                }
            });
            return view;
        }

        private void openImageActivity() {
            Intent in = new Intent(getActivity(), ImageActivity.class);
            in.putExtra("some","some data");
            startActivity(in);
        }

        private void openFileChooser() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent , PICK_IMAGE_REQUEST);
        }

        @Override
        public void onActivityResult(int requestCode , int resultCode , @Nullable Intent data) {
            super.onActivityResult(requestCode , resultCode , data);

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                mImageUri = data.getData();
                Picasso.get().load(mImageUri).into(mImageView);
            }
        }

        private String getFileExtension(Uri uri) {
            ContentResolver cR = getActivity().getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cR.getType(uri));
        }

        private void ValidateProductData() {
            Description = mEditDescription.getText().toString();
            PhoneNumber = mEditTextPhoneNum.getText().toString();
            ProductName = mEditTextFileName.getText().toString();

            if(mImageUri == null){
                Toast.makeText(getActivity(),
                        "Please Select Item Image!",
                        Toast.LENGTH_LONG).show();
            }else if(TextUtils.isEmpty(Description)){
                Toast.makeText(getActivity(),
                        "Please Write Description!",
                        Toast.LENGTH_LONG).show();
            }else if(TextUtils.isEmpty(PhoneNumber)){
                Toast.makeText(getActivity(),
                        "Please Enter your Phone Number!",
                        Toast.LENGTH_LONG).show();
            }else if(TextUtils.isEmpty(ProductName)){
                Toast.makeText(getActivity(),
                        "Please Write the Name of Item!",
                        Toast.LENGTH_LONG).show();
            }
            else{
                uploadFile();
            }
        }

        private void StoreProductImfInformation() {
            mLoadingBar.setTitle("Add New Product");
            mLoadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calendar.getTime());

            productRandomKey = saveCurrentDate + saveCurrentTime;

            final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
            final StorageReference filePath = mStorageRef.child(mImageUri.getLastPathSegment() + productRandomKey + ".jpg");

            final UploadTask uploadTask = filePath.putFile(mImageUri);


            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    String message = e.toString();
                    Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Toast.makeText(getActivity(), "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            downloadImageUrl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();

                                Toast.makeText(getActivity(), "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                                uploadFile();
                            }
                        }
                    });
                }
            });
        }
        private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        //productMap.put("category", CategoryName);
        productMap.put("phone",mEditTextPhoneNum);
        productMap.put("productName",ProductName);

        mDatabaseRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(getActivity(), ImageActivity.class);
                            startActivity(intent);

                            mLoadingBar.dismiss();
                            Toast.makeText(getActivity(), "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mLoadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

        private void uploadFile() {
            if (mImageUri != null) {
                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        + "." + getFileExtension(mImageUri));

                mUploadTask = fileReference.putFile(mImageUri)
                        //when successful
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(0);
                                    }
                                } , 500);

                                //this is the new way to do it
                                mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                                uri.toString(),
                                                mEditTextPhoneNum.getText().toString().trim(),
                                                mEditDescription.getText().toString().trim());
                                        String uploadId = mDatabaseRef.push().getKey();
                                        mDatabaseRef.child(uploadId).setValue(upload);
                                        mEditTextFileName.setText("");
                                        mLoadingBar.dismiss();

                                    }
                                });
                                Toast.makeText(getActivity() , "Upload Successfully" , Toast.LENGTH_LONG).show();

                            }
                        })
                        //when failure
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mLoadingBar.dismiss();
                                Toast.makeText(getActivity() , e.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        })
                        //when in progress
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                mProgressBar.setProgress((int) progress);
                                mLoadingBar.dismiss();
                            }
                        });

            }

        }

    }


