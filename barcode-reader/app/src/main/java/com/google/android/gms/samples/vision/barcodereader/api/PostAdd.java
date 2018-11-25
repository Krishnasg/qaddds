package com.google.android.gms.samples.vision.barcodereader.api;

import com.google.android.gms.samples.vision.barcodereader.api.requests.postadd.PostAddResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostAdd {
    @Multipart
    @POST("post/add")
    Call<PostAddResponse> postAdd(
            @Header("Authorization") String api_key,
            @Part("postData") String testDetails,
            @Part List<MultipartBody.Part> files
    );
}

