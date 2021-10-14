package com.eup.retrofitdemo.retrofit;

public class APIUtils {//dùng để cung cấp đường dẫn ra

    public static final String Base_Url = "http://192.168.1.3/Quanlysinhvien/";

    public static DataClient getData(){//nhận và gửi dữ liệu đi
        return RetrofitClient.getClient(Base_Url).create(DataClient.class);
    }
}
