package com.app.lab3_20210850.api;

import com.app.lab3_20210850.model.RequestLogin;
import com.app.lab3_20210850.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService {
    @POST("auth/login")
    Call<Usuario> login(@Body RequestLogin loginRequest);
}