package com.dopave.diethub_vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForResImage;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.GetVehicles.Image;
import com.dopave.diethub_vendor.Models.ProviderIMages.ImagesProvider;
import com.dopave.diethub_vendor.Models.ProviderIMages.MainImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Modify_ImagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify__images);
        getInitchild1();
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ArrayList<com.dopave.diethub_vendor.Models.ProviderIMages.Image> list = new ArrayList<>();
        String base46 = "iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAABHNCSVQICAgIfAhkiAAABNtJREFUaEPNWu1V3DoQ1cgFhA5CKgip4JECsOUKAhU8UkGggkcqyKYCW0sBgQoCFYR0sPyHmXeuj+yjleW19sPL+h+LLM3VjGbuXJnUimc+nxfMbJRSx0R0iqEi8qSUeiKimpltWZb4+80eiq1srf0iIldEdDxmmYjMROT6rYAsAaiq6oiIfhARdj35EZFFlmUXZ2dndfJLOxrYAXDG/yKik4G5710IIZzex8YQ0UWe57Md2ZY0TQegrusqsvP3WuubcGerqjrWWp+LyCURvfNXYuZPZVk+JK2+g0ENgPl8DmN+BPNdF0VxtWqNqqpOtNbY8Y/tOBxyY8yHHdiWNEUDoK7rP/6BJaKveZ7fpMzgQg9ZqfPEPkOJbm9vDTNXnrH3RVE0KTP1qarqVGv9y/PCgzHmU+r724wjay1C4Es7ida6bGPepVOAOW09NOQday3ivgslZv6wj9QKAHdKqX9aAEVR4LdvInI+VAcwJtw1ay3Oy7f2d2b+XJYl5p70gbHiu14ptWirbmxlEXk2xhyF/wvDSCk1mgR2gWwJwNCEMJqIECILZr6KpclDBRCtAzGgkYM8cyl2q41+fX19XlVXBj2wTiqFhREPbGV4+DLqCxHNmPl7WZaL9v9RAOsavw8A3jldiAgSRFPtYwDWrgP7BIC1QB5bED0Afh1YJwYiIQTyt4s0iowHgtmlegeioSw9AGGOB1XIsuyjiDQslZl/+jHYgpw6C2F+NFEBZfkaAujCx1VhsM0lei0iUZowNQBsVIT22B4AZobRoNaD3VisEu8DAEDUdY1C2xBHnIWwEuOfvSobnAVbFEWvY9sXgJD6pFTiRwDXWj+8vLwshvjNwQEAfciy7Dy1z41U4ka9WCeThWOJ6K4oimv/91QPPDLzaSzbDBk0VSUOm6MkAJv0tRMCWOoOUwAs0WA08FmWvReRtksDJ3nK87xRKVbUgW2ip33XMvO5HwmjANpOaqgO+JwEh7sVtQ7iEIvIXxExY3UgsrU3kBn9vniqhmalB5rCMF4HoqEReXeSjmw0hCLWNXWAmRtilmXZMc6D89SSqBW8+7YAUAdg5IrCdQTVzlc0DglAch0YUPWAZSoPQEAuHBd67lEJt/MoYsn6ZqgtOU9MAgByJmi1UgoRcBnjQmsvHJMXp/JAj274upBrWDZS1EIv4OJjG1ViTI1ogYR0+q8xptcHOLfh7uAIhhljLsKdWHEWNq7IUCJEBHJ9p0KMeSDa0IeyYWJDs7Hh/otjCslgS+lPMp/PL0Xkv/bkJ0qLOwEwprFGxd3Yyg7EETPPYqpz6CWlFArgoOsT0D1pretYP4KQzrLsHQjlSnk9YZFuSF3Xv30BYCp53Y8GpdTP3gWHiNwZYz6vY3ykG4smg3XmHBrb40IYWNc1OL5/85hcC1wNwBVVJwaMHbxtgATXAc+rLvmuwn40XNilV9wrd9oRKHksFW9jdPtuED742XY3LdbajmO0LyCcQNjyPLe+Ae6aFbf50JCWZJhN2tEUcNbaf93XA76nL5YuurXWoMzdPZc/McC4v3HRHRW90BMopZI5VIrhGDNwY/RYFMVJ71MDV/4btneojyOcJ0jn0Y89QAtwlTT0ScEbAwPVR5/SaE5RAK2BTkxtPrcJ5e19gnB3dHeg0eG3GP8DxPGF4K0O1rgAAAAASUVORK5CYII=";

        list.add(new com.dopave.diethub_vendor.Models.ProviderIMages.Image(base46));
        list.add(new com.dopave.diethub_vendor.Models.ProviderIMages.Image(base46));
        list.add(new com.dopave.diethub_vendor.Models.ProviderIMages.Image(base46));
        Common.getAPIRequest().updateImages("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",new ImagesProvider(new MainImage(base46),list)).enqueue(new Callback<Defualt>() {
            @Override
            public void onResponse(Call<Defualt> call, Response<Defualt> response) {
                if (response.code() == 200)
                    Toast.makeText(Modify_ImagesActivity.this, "ok", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<Defualt> call, Throwable t) {

            }
        });
    }
    private void getInitchild1() {
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
}
