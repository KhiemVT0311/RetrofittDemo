package com.eup.retrofitdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eup.retrofitdemo.databinding.ActivityRegisterBinding;
import com.eup.retrofitdemo.retrofit.APIUtils;
import com.eup.retrofitdemo.retrofit.DataClient;
import com.eup.retrofitdemo.retrofit.RetrofitClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    int REQUEST_CODE_IMAGE = 1;
    String realPath = "";
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);

        initView();
    }

    private void initView() {
        binding.imgRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);//lấy ảnh từ máy
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = binding.edtRegisterUsername.getText().toString();
                password = binding.edtRegisterPassword.getText().toString();

                if (username.length()>0 && password.length()>0){
                    File file = new File(realPath);
                    String file_path = file.getAbsolutePath();
                    String[] listFileName = file_path.split("\\.");
                    file_path = listFileName[0] + System.currentTimeMillis() + "."+listFileName[1];//tránh các hình ảnh có tên trung nhau
                    Log.d("Log",file_path);

                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/from-data"),file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file",file_path,requestBody);
                    DataClient dataClient = APIUtils.getData();
                    retrofit2.Call<String> callBack = dataClient.UploadPhoto(body);
                    callBack.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            //trả về kết quả lỗi
                            if (response != null){
                                String message = response.body();
                                Log.d("Log",message);
                                if (message.length() > 0){
                                    DataClient insertData = APIUtils.getData();
                                    retrofit2.Call<String> callback = insertData.InsertData(username,password,APIUtils.Base_Url + "image/" + message);
                                    callback.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String result = response.body();
                                            Toast.makeText(RegisterActivity.this,"Success",Toast.LENGTH_SHORT).show();
                                            if (result.equals("Success")){
                                                Log.d("Log",username+"  "+password);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(RegisterActivity.this,"Fail",Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            //trả về kết quả lỗi khi ko kết nối đc sever
                            Log.d("Log",t.getMessage());
                        }
                    });//trả về sự kiện call
                } else {
                    Toast.makeText(RegisterActivity.this,"Please enter enough information",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                binding.imgRegister.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}