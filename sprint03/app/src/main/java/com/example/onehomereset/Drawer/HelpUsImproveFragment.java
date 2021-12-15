package com.example.onehomereset.Drawer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.onehomereset.HUIhelper;
import com.example.onehomereset.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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