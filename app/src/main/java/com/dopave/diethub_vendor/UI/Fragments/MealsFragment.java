package com.dopave.diethub_vendor.UI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dopave.diethub_vendor.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MealsFragment extends Fragment {

    public MealsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_meals, container, false);
    }
}
