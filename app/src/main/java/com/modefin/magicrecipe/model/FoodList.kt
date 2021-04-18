package com.modefin.magicrecipe.model

import com.google.gson.annotations.SerializedName

 class FoodList {

     @SerializedName("title")
     val title: String?=""
     @SerializedName("href")
     var href: String?=""
     @SerializedName("ingredients")
     var ingredients: String=""
     @SerializedName("thumbnail")
     var thumbnail: String=""
 }
