package com.example.onehomereset.Drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.onehomereset.R;

public class HelpUsImproveFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_helpusimprove ,container,false);
    }
}
