package com.djohnsoftware.adsmanager.retrofit;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("get_data_field")
    Call<SplashResponse> getAppData(@Field("packagename") String str);
}
