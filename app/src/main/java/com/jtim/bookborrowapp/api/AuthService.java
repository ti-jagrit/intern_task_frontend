package com.jtim.bookborrowapp.api;

import com.jtim.bookborrowapp.models.LoginRequest;
import com.jtim.bookborrowapp.models.LoginResponseWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("login")
    Call<LoginResponseWrapper> login(@Body LoginRequest request);
}
