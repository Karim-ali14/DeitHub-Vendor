package com.dopave.diethub_vendor.UI.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.UI.Setting.Modify_Images.Modify_ImagesActivity;
import com.dopave.diethub_vendor.UI.Setting.TimeWork.Modify_Work_TimeActivity;
import com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info.Modify_personal_infoActivity;
import com.dopave.diethub_vendor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    RecyclerView Recycler;
    EditText RestaurantsName,About_Res;
    ConstraintLayout Layout_Setting;
    LinearLayout Modify_personal_info,Modify_Images,Modify_Work_Time;
    ImageView Res_icon_Setting;
    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Res_icon_Setting = view.findViewById(R.id.Res_icon_Setting);
        if (Common.currentPosition.getData().getProvider().getMainImage() != null){
            String path = Common.BaseUrlForImages +
                    Common.currentPosition.getData().getProvider().getMainImage().getFor()
                    + "/" + Uri.encode(Common.currentPosition.getData().getProvider().getMainImage().getName());
            Picasso.with(getActivity()).load(path).into(Res_icon_Setting);
        }
//        RestaurantsName = view.findViewById(R.id.RestaurantsName);
//        About_Res = view.findViewById(R.id.About_Res);
//        Recycler = view.findViewById(R.id.Recycler_Res_Icons);
//        Layout_Setting = view.findViewById(R.id.Layout_Setting);
//        Recycler.setHasFixedSize(true);
//        Recycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
////        Recycler.setAdapter(new AdapterForResImage(getData(), getActivity()));
//        Layout_Setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeKeyBoard();
//            }
//        });
        Modify_personal_info = view.findViewById(R.id.modify_personal_info);
        Modify_Work_Time = view.findViewById(R.id.Modify_Work_Time);
        Modify_Images = view.findViewById(R.id.Modify_Images);
        onClickEvents();
        return view;
    }

    private void onClickEvents() {
        Modify_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Modify_personal_infoActivity.class));
            }
        });
        Modify_Images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Modify_ImagesActivity.class));
            }
        });
        Modify_Work_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Modify_Work_TimeActivity.class));
            }
        });
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        return list;
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
        RestaurantsName.clearFocus();
        About_Res.clearFocus();
    }
}
