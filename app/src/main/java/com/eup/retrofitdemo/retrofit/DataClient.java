package com.eup.retrofitdemo.retrofit;

import com.eup.retrofitdemo.model.SinhVien;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataClient {// quản lý các phương thức sẽ đẩy lên server

    @Multipart
    @POST("uploadhinhanh.php")
    Call<String> UploadPhoto(@Part MultipartBody.Part photo);//khi server trả lại cho mình 1 dữ liệu bất kỳ  với kiểu dữ liệu...
    //lắng nghe việc gửi dữ liệu lên trên server

    @FormUrlEncoded
    @POST("insert.php")
    Call<String> InsertData (@Field("username") String username, @Field("password") String password, @Field("image") String image);

    @FormUrlEncoded
    @POST("login.php")
    Call<List<SinhVien>> LoginData (@Field("username") String username, @Field("password") String password);

    @GET("delete.php")
    Call<String> DeleteData(@Query("id") String id, @Query("image") String image);

}
