package com.dopave.diethub_vendor.UI.Setting.Modify_Images;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForEditImages;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.GetDeliveries.Image;
import com.dopave.diethub_vendor.Models.ProviderIMages.GetImages.ProviderImagesResponse;
import com.dopave.diethub_vendor.Models.ProviderIMages.Update.MainImage;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInformation;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info.Modify_Person_info_viewModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Modify_ImagesActivity extends AppCompatActivity {
    Modify_Images_ViewModel viewModel;
    Modify_Person_info_viewModel modify_person_info_viewModel;
    Button EnterButton;
    ProgressDialog dialog;
    ImageView MainImage;
    RecyclerView recyclerView;
    public int SELECT_IMAGE_FOR_PROVIDER = 2;
    public int SELECT_MAIN_IMAGE_FOR_PROVIDER = 1;
    AdapterForEditImages adapterForEditImages;
    String MainImageBase46;
    ArrayList<com.dopave.diethub_vendor.Models.ProviderIMages.Update.Image> listForRequest;
    List<Image> list;
    public static int numberOfIndexes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify__images);
        viewModel = ViewModelProviders.of(this).get(Modify_Images_ViewModel.class);
        modify_person_info_viewModel = ViewModelProviders.of(this).get(Modify_Person_info_viewModel.class);
        EnterButton = findViewById(R.id.EnterButton);
        MainImage = findViewById(R.id.MainImage);
        listForRequest = new ArrayList<>();
        list = new ArrayList<>();
        list.add(new Image());
        recyclerView = findViewById(R.id.Recycler_Res_Icons);
        dialog = new ProgressDialog(this);

        getMainImage();

        getProviderImages();

        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("IIIIII","onclick");
                if (Common.currentPosition.getData().getProvider().getMainImage() != null) {
                    Log.i("IIIIII","getMainImage != null");
                    if (MainImageBase46 != null && listForRequest.size() != 0) {
                        Log.i("IIIIII","MainImageBase46 != null && listForRequest.size() != 0");
                        upDateImage(new MainImage(Common.currentPosition.getData().getProvider().
                                getMainImage().getId(), "edited", MainImageBase46),listForRequest);
                    }else if (MainImageBase46 != null || listForRequest.size() != 0){
                        Log.i("IIIIII","MainImageBase46 != null || listForRequest.size() != 0");
                        if (MainImageBase46 != null){
                            Log.i("IIIIII","MainImageBase46 != null");
                            upDateImage(new MainImage(Common.currentPosition.getData().getProvider().
                                    getMainImage().getId(), "edited", MainImageBase46),null);

                        }else if (listForRequest.size() != 0){
                            Log.i("IIIIII","listForRequest.size() != 0");

                            upDateImage(null,listForRequest);
                        }
                    }else {
                        Log.i("IIIIII","لا تغييرات");
                        Toast.makeText(Modify_ImagesActivity.this, R.string.no_changes, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (MainImageBase46 != null && listForRequest.size() != 0) {

                        upDateImage(new MainImage(Common.currentPosition.getData().getProvider().
                                getMainImage().getId(), "new", MainImageBase46),listForRequest);
                    }else if (MainImageBase46 != null || listForRequest.size() != 0){

                        if (MainImageBase46 != null){

                            upDateImage(new MainImage(Common.currentPosition.getData().getProvider().
                                    getMainImage().getId(), "new", MainImageBase46),null);

                        }else if (listForRequest.size() != 0){

                            upDateImage(null,listForRequest);
                        }
                    }else {

                        Toast.makeText(Modify_ImagesActivity.this, R.string.no_changes, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void upDateImage(MainImage mainImage,
                             ArrayList<com.dopave.diethub_vendor.Models.ProviderIMages.Update.Image>
                                     list_For_Request){
        viewModel.updateImages(Modify_ImagesActivity.this, dialog,
                mainImage, list_For_Request)
                .observe(Modify_ImagesActivity.this, new Observer<Defualt>() {
                    @Override
                    public void onChanged(Defualt defualt) {
                        startActivity(new Intent(Modify_ImagesActivity.this,
                                HomeActivity.class).putExtra("type", "Modify"));
                    }
                });
    }
    private void getProviderImages() {
        viewModel.getProviderImages(dialog,this,viewModel).observe(this, new Observer<ProviderImagesResponse>() {
            @Override
            public void onChanged(ProviderImagesResponse providerImagesResponse) {
                onGetProviderInfo(providerImagesResponse);
            }
        });
    }

    private void getMainImage(){
        modify_person_info_viewModel.getProviderInfo(this,dialog,modify_person_info_viewModel,
                "forImage").observe(this, new Observer<ProviderInformation>() {
            @Override
            public void onChanged(ProviderInformation providerInfo) {
                dialog.dismiss();
                onGetMainImage(providerInfo);
            }
        });
    }

    public void onGetMainImage(ProviderInformation providerInfo){
        Common.currentPosition.getData().getProvider().getMainImage().setFor(providerInfo.getData().getProvider().getMainImage().getFor());
        Common.currentPosition.getData().getProvider().getMainImage().setName(providerInfo.getData().getProvider().getMainImage().getName());
        String PathMainImage = Common.BaseUrl + "images/" + Common.currentPosition.getData()
                .getProvider().getMainImage().getFor() + "/" +
                Uri.encode(Common.currentPosition.getData().getProvider().getMainImage().getName());
        Picasso.with(Modify_ImagesActivity.this).load(PathMainImage).into(MainImage);
    }

    public void onGetProviderInfo(ProviderImagesResponse providerImagesResponse){
        if (providerImagesResponse.getData().getRows() != null) {
            if (providerImagesResponse.getData().getRows().size() != 0) {
                list.addAll(providerImagesResponse.getData().getRows());
                Modify_ImagesActivity.numberOfIndexes = list.size();
            }
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapterForEditImages = new AdapterForEditImages(list, this,numberOfIndexes,
                listForRequest,recyclerView,viewModel,dialog);
        recyclerView.setAdapter(adapterForEditImages);
    }

    public void BackButton(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_MAIN_IMAGE_FOR_PROVIDER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        MainImage.setImageBitmap(bitmap);
                        MainImageBase46 = compressToBase46(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        else if (requestCode == SELECT_IMAGE_FOR_PROVIDER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                                data.getData());
                        listForRequest.add(
                                new com.dopave.diethub_vendor.Models.ProviderIMages.Update.
                                        Image("new",compressToBase46(bitmap)));
                        list.add(new Image(bitmap));

                        recyclerView.setAdapter(new AdapterForEditImages(list,
                                Modify_ImagesActivity.this,numberOfIndexes,listForRequest,
                                recyclerView,viewModel,dialog));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void openGallery(int type) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),type);
    }

    public void openGalleryForMainImage(View view) {
        openGallery(SELECT_MAIN_IMAGE_FOR_PROVIDER);
    }

    private String compressToBase46(Bitmap btmap){
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        btmap.compress(Bitmap.CompressFormat.JPEG, 30, boas);
        byte[] b = boas.toByteArray();
        String encodeImage = Base64.encodeToString(b, Base64.DEFAULT).replaceAll("\n| ","").trim();

        return encodeImage;
    }

}
