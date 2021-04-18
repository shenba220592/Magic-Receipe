package com.modefin.magicrecipe.model

import com.google.gson.annotations.SerializedName

data class FoodListResponse (

        @SerializedName("title")  val title : String?,
        @SerializedName("results")  val results : ArrayList<FoodList>?

)