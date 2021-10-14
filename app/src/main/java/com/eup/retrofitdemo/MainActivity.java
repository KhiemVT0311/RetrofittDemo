package com.eup.retrofitdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eup.retrofitdemo.databinding.ActivityMainBinding;
import com.eup.retrofitdemo.model.SinhVien;
import com.eup.retrofitdemo.retrofit.APIUtils;
import com.eup.retrofitdemo.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String[] PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String username="";
    private String password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        if (checkPermission()){
            initView();
        } else {
            requestPermission();
        }
    }


    private void initView() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = binding.edtUsername.getText().toString();
                password = binding.edtPassword.getText().toString();

                if (username.length()>0 && password.length()>0){
                    DataClient dataClient = APIUtils.getData();
                    retrofit2.Call<List<SinhVien>> callback = dataClient.LoginData(username,password);
                    callback.enqueue(new Callback<List<SinhVien>>() {
                        @Override
                        public void onResponse(Call<List<SinhVien>> call, Response<List<SinhVien>> response) {
                            ArrayList<SinhVien> list = (ArrayList<SinhVien>) response.body();
                            if (list.size() > 0){
                                Log.d("Log",list.get(0).getUsername());
                                Log.d("Log",list.get(0).getPassword());
                                Log.d("Log",list.get(0).getImage());
                                Intent intent = new Intent(MainActivity.this,UserInfomationActivity.class);
                                intent.putExtra("listSinhVien",list);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SinhVien>> call, Throwable t) {
                            Toast.makeText(MainActivity.this,"Account or password does not exist",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    private boolean checkPermission() {
        if (android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.M) {
            for (String p : PERMISSION) {
                int status = checkSelfPermission(p);
                if (status == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSION, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission()) {
            initView();
        } else {
            finish();
        }
    }
}