package com.ydhnwb.latihanmvvm.webservices

import com.ydhnwb.latihanmvvm.models.Recipe
import com.ydhnwb.latihanmvvm.models.User
import com.ydhnwb.latihanmvvm.utils.WrappedListResponse
import com.ydhnwb.latihanmvvm.utils.WrappedResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("api/post")
    fun allRecipe(@Header("Authorization") token : String) : Call<WrappedListResponse<Recipe>>

    @GET("api/post/{id}")
    fun getOneRecipe(@Header("Authorization") token : String, @Path("id") id : String) : Call<WrappedResponse<Recipe>>

    @FormUrlEncoded
    @POST("api/post")
    fun createRecipe(@Header("Authorization") token : String, @Field("title") title : String,
                     @Field("content") content : String) : Call<WrappedResponse<Recipe>>

    @FormUrlEncoded
    @PUT("api/post/{id}")
    fun updateRecipe(@Header("Authorization") token : String,
                     @Path("id") id : String,
                     @Field("title") title : String,
                     @Field("content") content : String) : Call<WrappedResponse<Recipe>>

    @DELETE("api/post/{id}")
    fun deleteRecipe(@Header("Authorization") token : String, @Path("id") id : String) : Call<WrappedResponse<Recipe>>


    @FormUrlEncoded
    @POST("api/login")
    fun login(@Field("email") email : String, @Field("password") password : String) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("api/register")
    fun register(@Field("name") name : String,@Field("email") email : String, @Field("password") password : String) : Call<WrappedResponse<User>>

}