package com.eup.retrofitdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.eup.retrofitdemo.databinding.ActivityUserInfomationBinding;
import com.eup.retrofitdemo.model.SinhVien;
import com.eup.retrofitdemo.retrofit.APIUtils;
import com.eup.retrofitdemo.retrofit.DataClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfomationActivity extends AppCompatActivity {
    private ActivityUserInfomationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_infomation);
        initView();

    }

    private void initView() {
        Intent intent = getIntent();
        ArrayList<SinhVien> sinhVienArrayList = intent.getParcelableArrayListExtra("listSinhVien");
        Log.d("Log",sinhVienArrayList.get(0).getUsername());

        binding.tvUsernameUser.setText("username: "+sinhVienArrayList.get(0).getUsername());
        binding.tvPasswordUser.setText("password: "+sinhVienArrayList.get(0).getPassword());
        Picasso.get().load(sinhVienArrayList.get(0).getImage()).into(binding.imgViewUser);

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameFolder = sinhVienArrayList.get(0).getImage();
                nameFolder = nameFolder.substring(nameFolder.lastIndexOf("/"));

                Log.d("Log",nameFolder);

                DataClient dataClient = APIUtils.getData();
                Call<String> call = dataClient.DeleteData(sinhVienArrayList.get(0).getId(),nameFolder);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        if (result.equals("ok")){
                            Toast.makeText(UserInfomationActivity.this,"Delete success ", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }
}