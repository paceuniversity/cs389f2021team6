package com.example.onehomereset;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class HomePageFragment extends Fragment{
    Activity context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState) {
        context = getActivity();
        return inflater.inflate(R.layout.fragment_homepage ,container,false);

    }

}
