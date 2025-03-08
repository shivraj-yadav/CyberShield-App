package com.example.cybershield_;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {
    @Multipart
    @POST("detect_spam")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file);
}
