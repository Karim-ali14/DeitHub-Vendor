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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForResImage;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.GetVehicles.Image;
import com.dopave.diethub_vendor.Models.ProviderIMages.ImagesProvider;
import com.dopave.diethub_vendor.Models.ProviderIMages.MainImage;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info.Modify_personal_infoActivity;
import com.dopave.diethub_vendor.UI.Setting.TimeWork.Modify_Work_TimeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Modify_ImagesActivity extends AppCompatActivity {
    Modify_Images_ViewModel viewModel;
    Button EnterButton;
    ProgressDialog dialog;
    ImageView MainImage;
    int SELECT_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify__images);
        viewModel = ViewModelProviders.of(this).get(Modify_Images_ViewModel.class);
        EnterButton = findViewById(R.id.EnterButton);
        MainImage = findViewById(R.id.MainImage);
        dialog = new ProgressDialog(this);
        getInitchild1();
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        final ArrayList<com.dopave.diethub_vendor.Models.ProviderIMages.Image> list = new ArrayList<>();
        final String base46 = "iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAABHNCSVQICAgIfAhkiAAABNtJREFUaEPNWu1V3DoQ1cgFhA5CKgip4JECsOUKAhU8UkGggkcqyKYCW0sBgQoCFYR0sPyHmXeuj+yjleW19sPL+h+LLM3VjGbuXJnUimc+nxfMbJRSx0R0iqEi8qSUeiKimpltWZb4+80eiq1srf0iIldEdDxmmYjMROT6rYAsAaiq6oiIfhARdj35EZFFlmUXZ2dndfJLOxrYAXDG/yKik4G5710IIZzex8YQ0UWe57Md2ZY0TQegrusqsvP3WuubcGerqjrWWp+LyCURvfNXYuZPZVk+JK2+g0ENgPl8DmN+BPNdF0VxtWqNqqpOtNbY8Y/tOBxyY8yHHdiWNEUDoK7rP/6BJaKveZ7fpMzgQg9ZqfPEPkOJbm9vDTNXnrH3RVE0KTP1qarqVGv9y/PCgzHmU+r724wjay1C4Es7ida6bGPepVOAOW09NOQday3ivgslZv6wj9QKAHdKqX9aAEVR4LdvInI+VAcwJtw1ay3Oy7f2d2b+XJYl5p70gbHiu14ptWirbmxlEXk2xhyF/wvDSCk1mgR2gWwJwNCEMJqIECILZr6KpclDBRCtAzGgkYM8cyl2q41+fX19XlVXBj2wTiqFhREPbGV4+DLqCxHNmPl7WZaL9v9RAOsavw8A3jldiAgSRFPtYwDWrgP7BIC1QB5bED0Afh1YJwYiIQTyt4s0iowHgtmlegeioSw9AGGOB1XIsuyjiDQslZl/+jHYgpw6C2F+NFEBZfkaAujCx1VhsM0lei0iUZowNQBsVIT22B4AZobRoNaD3VisEu8DAEDUdY1C2xBHnIWwEuOfvSobnAVbFEWvY9sXgJD6pFTiRwDXWj+8vLwshvjNwQEAfciy7Dy1z41U4ka9WCeThWOJ6K4oimv/91QPPDLzaSzbDBk0VSUOm6MkAJv0tRMCWOoOUwAs0WA08FmWvReRtksDJ3nK87xRKVbUgW2ip33XMvO5HwmjANpOaqgO+JwEh7sVtQ7iEIvIXxExY3UgsrU3kBn9vniqhmalB5rCMF4HoqEReXeSjmw0hCLWNXWAmRtilmXZMc6D89SSqBW8+7YAUAdg5IrCdQTVzlc0DglAch0YUPWAZSoPQEAuHBd67lEJt/MoYsn6ZqgtOU9MAgByJmi1UgoRcBnjQmsvHJMXp/JAj274upBrWDZS1EIv4OJjG1ViTI1ogYR0+q8xptcHOLfh7uAIhhljLsKdWHEWNq7IUCJEBHJ9p0KMeSDa0IeyYWJDs7Hh/otjCslgS+lPMp/PL0Xkv/bkJ0qLOwEwprFGxd3Yyg7EETPPYqpz6CWlFArgoOsT0D1pretYP4KQzrLsHQjlSnk9YZFuSF3Xv30BYCp53Y8GpdTP3gWHiNwZYz6vY3ykG4smg3XmHBrb40IYWNc1OL5/85hcC1wNwBVVJwaMHbxtgATXAc+rLvmuwn40XNilV9wrd9oRKHksFW9jdPtuED742XY3LdbajmO0LyCcQNjyPLe+Ae6aFbf50JCWZJhN2tEUcNbaf93XA76nL5YuurXWoMzdPZc/McC4v3HRHRW90BMopZI5VIrhGDNwY/RYFMVJ71MDV/4btneojyOcJ0jn0Y89QAtwlTT0ScEbAwPVR5/SaE5RAK2BTkxtPrcJ5e19gnB3dHeg0eG3GP8DxPGF4K0O1rgAAAAASUVORK5CYII=";

        list.add(new com.dopave.diethub_vendor.Models.ProviderIMages.Image(base46));
        list.add(new com.dopave.diethub_vendor.Models.ProviderIMages.Image(base46));
        list.add(new com.dopave.diethub_vendor.Models.ProviderIMages.Image(base46));

        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.updateImages(Modify_ImagesActivity.this,dialog,base46,list).observe(Modify_ImagesActivity.this, new Observer<Defualt>() {
                    @Override
                    public void onChanged(Defualt defualt) {
                        startActivity(new Intent(Modify_ImagesActivity.this,
                                HomeActivity.class).putExtra("type","Modify"));
                    }
                });
            }
        });

        MainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
            }
        });
    }
    private void getInitchild1() {
        Common.getAPIRequest().getProviderImages("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"")
                .enqueue(new Callback<ImagesProvider>() {
            @Override
            public void onResponse(Call<ImagesProvider> call, Response<ImagesProvider> response) {
                if (response.code() == 200)
                    Log.i("GGGGGGG",response.body().getImages().size()+" "+response.body().getMainImage().getBase64());
                else {
                    try {
                        String message = new JSONObject(response.errorBody().string())
                                .getString("message");
                        Toast.makeText(Modify_ImagesActivity.this,message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ImagesProvider> call, Throwable t) {

            }
        });
        RecyclerView recyclerView = findViewById(R.id.Recycler_Res_Icons);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new AdapterForResImage(getData(), this,null,"update",getData().size(),recyclerView));
    }

    private List<Image> getData() {
        List<Image> list = new ArrayList<>();
        list.add(new Image(BitmapFactory.decodeResource(this.getResources(),R.drawable.gg)));
        list.add(new Image(BitmapFactory.decodeResource(this.getResources(),R.drawable.gg)));
        list.add(new Image(BitmapFactory.decodeResource(this.getResources(),R.drawable.gg)));
        return list;
    }

    public void BackButton(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        MainImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}