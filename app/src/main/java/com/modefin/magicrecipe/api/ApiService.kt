package com.modefin.magicrecipe.api

import com.modefin.magicrecipe.model.FoodListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("Content-Type:application/json")
    @GET("api/")
    suspend fun getFoodItems( @Query("i")ingredients:String,
                              @Query("p")pageNo:Int): Response<FoodListResponse>

}