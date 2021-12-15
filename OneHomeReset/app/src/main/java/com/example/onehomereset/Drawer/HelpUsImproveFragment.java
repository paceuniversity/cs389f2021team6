package com.example.onehomereset.Drawer;

import static com.google.common.io.Files.getFileExtension;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.onehomereset.HUIhelper;
import com.example.onehomereset.R;
import com.example.onehomereset.Upload;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;

public class HelpUsImproveFragment extends Fragment {
    EditText mtitle,mfeedback;
    Button send;
    String Description,Title;
    Activity context;

    FirebaseDatabase rootNode;
    DatabaseReference mDatabaseRef;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState) {

        context = getActivity();



        return inflater.inflate(R.layout.fragment_helpusimprove ,container,false);

    }
    public void onStart() {
        super.onStart();
        mtitle =(EditText) context.findViewById(R.id.fb_Title);
        mfeedback = (EditText) context.findViewById(R.id.fb_feedback);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("feedback");
        send =(Button) context.findViewById(R.id.msend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                mDatabaseRef = rootNode.getReference("feedback");
                HUIhelper huIhelper = new HUIhelper(mtitle.getText().toString().trim(),
                        mfeedback.getText().toString().trim());
                mDatabaseRef.push().setValue(huIhelper);
                Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();

            }
        });
    }






}







