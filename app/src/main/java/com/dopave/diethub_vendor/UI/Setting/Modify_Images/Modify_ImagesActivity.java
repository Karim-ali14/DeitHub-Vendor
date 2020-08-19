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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForEditImages;
import com.dopave.diethub_vendor.Adapter.AdapterForResImage;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.GetDeliveries.Image;
import com.dopave.diethub_vendor.Models.ProviderIMages.GetImages.ProviderImagesResponse;
import com.dopave.diethub_vendor.Models.ProviderIMages.Update.ImagesProvider;
import com.dopave.diethub_vendor.Models.ProviderIMages.Update.MainImage;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info.Modify_personal_infoActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Modify_ImagesActivity extends AppCompatActivity {
    Modify_Images_ViewModel viewModel;
    Button EnterButton;
    ProgressDialog dialog;
    ImageView MainImage;
    com.dopave.diethub_vendor.Models.ProviderIMages.Update.MainImage mainImage;
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
        EnterButton = findViewById(R.id.EnterButton);
        MainImage = findViewById(R.id.MainImage);
        listForRequest = new ArrayList<>();
        list = new ArrayList<>();
        list.add(new Image());
        recyclerView = findViewById(R.id.Recycler_Res_Icons);
        dialog = new ProgressDialog(this);
        getInitchild1();
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainImage != null) {
                    viewModel.updateImages(Modify_ImagesActivity.this,dialog,
                            new MainImage(mainImage.getId(),"edited",MainImageBase46),listForRequest)
                            .observe(Modify_ImagesActivity.this, new Observer<Defualt>() {
                                @Override
                                public void onChanged(Defualt defualt) {
                                    startActivity(new Intent(Modify_ImagesActivity.this,
                                            HomeActivity.class).putExtra("type","Modify"));
                                }
                            });
                }
                else {
                    viewModel.updateImages(Modify_ImagesActivity.this,dialog,
                            new MainImage("new",MainImageBase46),listForRequest)
                            .observe(Modify_ImagesActivity.this, new Observer<Defualt>() {
                                @Override
                                public void onChanged(Defualt defualt) {
                                    startActivity(new Intent(Modify_ImagesActivity.this,
                                            HomeActivity.class).putExtra("type","Modify"));
                                }
                            });
                }
            }
        });

    }

    private void getInitchild1() {
        viewModel.getProviderImages(dialog,this,viewModel).observe(this, new Observer<ProviderImagesResponse>() {
            @Override
            public void onChanged(ProviderImagesResponse providerImagesResponse) {
                onGetProviderInfo(providerImagesResponse);
            }
        });


    }

    private void onGetProviderInfo(ProviderImagesResponse providerImagesResponse){
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
