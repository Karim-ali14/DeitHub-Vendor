package com.dopave.diethub_vendor.UI.Add_Delegate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.Cities.CityRow;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_DelegateActivity extends AppCompatActivity {
    ConstraintLayout Layout_AddDelegate;
    EditText NameDelegate_Add,EmailDelegate_Add,PhoneNumber_Add;
    Spinner spinnerCity;
    CityRow cityRow;
    TextView CitySelected;
    Button ButtonAddDelivery;
//    DeliveryRow row;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__delegate);
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        ButtonAddDelivery = findViewById(R.id.ButtonAddDelivery);
        CitySelected = findViewById(R.id.CitySelected);
        spinnerCity = findViewById(R.id.spinnerCity);
        Layout_AddDelegate = findViewById(R.id.Layout_AddDelegate);
        NameDelegate_Add = findViewById(R.id.NameDelegate_Add);
        EmailDelegate_Add = findViewById(R.id.EmailDelegate_Add);
        PhoneNumber_Add = findViewById(R.id.PhoneNumber_Add);
        PhoneNumber_Add = findViewById(R.id.PhoneNumber_Add);
        getCities();
        Layout_AddDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
            }
        });
        ButtonAddDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NameDelegate_Add.getText().toString().isEmpty())
                    Toast.makeText(Add_DelegateActivity.this, "Enter delivery name", Toast.LENGTH_SHORT).show();
                else if (EmailDelegate_Add.getText().toString().isEmpty())
                    Toast.makeText(Add_DelegateActivity.this, "Enter delivery email", Toast.LENGTH_SHORT).show();
                else if (PhoneNumber_Add.getText().toString().isEmpty())
                    Toast.makeText(Add_DelegateActivity.this, "Enter delivery phone", Toast.LENGTH_SHORT).show();
                else{
                    if (getIntent().getExtras().getString("type").equals("update")){
                        update();
                    }else
                        addDelivery();
                }

            }
        });
       /* if (getIntent().getExtras().getString("type").equals("update")){
             row = getIntent().getExtras().getParcelable("data");
            setDataToUpdate();
        }*/
    }

  /*  private void setDataToUpdate() {
        NameDelegate_Add.setText(row.getName());
        EmailDelegate_Add.setText(row.getEmail());
        PhoneNumber_Add.setText(row.getMobilePhone());
        spinnerCity.setSelection(row.getId());
    }*/

    private void update() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();
//        Common.getAPIRequest().updateDeliveryByProvider("Bearer "+
//                        Common.currentPosition.getData().getToken().getAccessToken()
//                ,new UpdateDeliveryRequest(
//                        EmailDelegate_Add.getText().toString()
//                        ,PhoneNumber_Add.getText().toString()
//                        ,NameDelegate_Add.getText().toString()
//                        ,false,true,"active",cityRow.getId()
//                ),
//                Common.currentPosition.getData().getProvider().getId()+""
//                ,row.getId()+"").enqueue(new Callback<DeliveryByProvider>() {
//            @Override
//            public void onResponse(Call<DeliveryByProvider> call, Response<DeliveryByProvider> response) {
//                if (response.code() == 200){
//                    dialog.dismiss();
//                    Toast.makeText(Add_DelegateActivity.this, "success", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(Add_DelegateActivity.this,
//                            HomeActivity.class).putExtra("type",
//                            "Add_DelegateActivity").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                            Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                }else {
//                    dialog.dismiss();
//                    try {
//                        String message = new JSONObject(response.errorBody()
//                                .string()).getString("message");
//                        Log.i("TTTTTTT",message);
//                        Toast.makeText(Add_DelegateActivity.this.getApplicationContext(),message, Toast.LENGTH_SHORT).show();
//                    } catch (IOException | JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DeliveryByProvider> call, Throwable t) {
//                dialog.dismiss();
//            }
//        });
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        NameDelegate_Add.clearFocus();
        EmailDelegate_Add.clearFocus();
        PhoneNumber_Add.clearFocus();
    }

    public void BackButton(View view) {
        finish();
    }

    private void addDelivery(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();
//        Common.getAPIRequest().createDeliveryByProvider("Bearer "+
//                        Common.currentPosition.getData().getToken().getAccessToken(),
//                new DeliveryByProviderRequest(PhoneNumber_Add.getText().toString(),"gggggghgggg",
//                        NameDelegate_Add.getText().toString(),EmailDelegate_Add.getText().toString()
//                        ,cityRow.getId()),
//                Common.currentPosition.getData().getProvider().getId()+"")
//                .enqueue(new Callback<DeliveryByProvider>() {
//                    @Override
//                    public void onResponse(Call<DeliveryByProvider> call, Response<DeliveryByProvider> response) {
//                        dialog.dismiss();
//                        if (response.code() == 201 ){
//                            Toast.makeText(Add_DelegateActivity.this, "success", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(Add_DelegateActivity.this,
//                                    HomeActivity.class).putExtra("type",
//                                    "Add_DelegateActivity").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                        } else {
//                            try {
//                                String message = new JSONObject(response.errorBody()
//                                        .string()).getString("message");
//                                Toast.makeText(Add_DelegateActivity.this, message, Toast.LENGTH_SHORT).show();
//                                Log.i("TTTTTTT",new JSONObject(response.errorBody()
//                                        .string()).getString("message")+response.code()+"dfsfsdfs");
//                            } catch (IOException | JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<DeliveryByProvider> call, Throwable t) {
//                        dialog.dismiss();
//                    }
//                });
    }

    private void getCities (){
//        Common.getAPIRequest().getAllCties().enqueue(new Callback<Cities>() {
//            @Override
//            public void onResponse(Call<Cities> call, Response<Cities> response) {
//                if (response.code() == 200){
//                    AdapterOfSpinner arrayAdapter = new AdapterOfSpinner(Add_DelegateActivity.this,
//                            R.layout.city_item,response.body().getData().getCityRows());
//                    spinnerCity.setAdapter(arrayAdapter);
//                    spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            cityRow = ((CityRow) parent.getItemAtPosition(position));
//                            CitySelected.setText(((CityRow) parent.getItemAtPosition(position)).getName());
//                            CitySelected.setTextColor(getResources().getColor(R.color.black));
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Cities> call, Throwable t) {
//
//            }
//        });

    }

    public class AdapterOfSpinner extends ArrayAdapter<CityRow> {
        List<CityRow> list;
        LayoutInflater inflater;
        public AdapterOfSpinner(Activity context, int id , List<CityRow> list)
        {
            super(context,id,list);
            this.list=list;
            inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vv = inflater.inflate(R.layout.city_item,parent,false);
            TextView Tname =(TextView)vv.findViewById(R.id.item);

            return vv;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View vv = inflater.inflate(R.layout.city_item,parent,false);
            TextView Tname =(TextView)vv.findViewById(R.id.item);
            Tname.setText(list.get(position).getName());
            return vv;
        }

    }
}
