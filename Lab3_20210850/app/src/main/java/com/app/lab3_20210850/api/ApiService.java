package com.app.lab3_20210850.api;

import com.app.lab3_20210850.model.RequestLogin;
import com.app.lab3_20210850.model.ToDo;
import com.app.lab3_20210850.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiService {
    @POST("auth/login")
    Call<Usuario> login(@Body RequestLogin loginRequest);

    @GET("todos/user/{userId}")
    Call<List<ToDo>> getTodosByUserId(@Path("userId") int userId);
}