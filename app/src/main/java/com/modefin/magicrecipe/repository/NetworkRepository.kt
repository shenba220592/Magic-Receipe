package com.modefin.magicrecipe.repository


import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.modefin.magicrecipe.api.ApiService
import com.modefin.magicrecipe.api.RetrofitBuilder
import com.modefin.magicrecipe.api.RetrofitBuilder.apiService
import com.modefin.magicrecipe.exception.NoInternetException
import com.modefin.magicrecipe.model.FoodListResponse
import com.modefin.magicrecipe.utils.CommonUtils


import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkRepository(){



    suspend fun getFoodList(ingredeients:String,pageNo:Int): FoodListResponse?{
        var response: Response<FoodListResponse>?=null
        try{
            response= apiService.getFoodItems(ingredeients,pageNo)
        }catch (e: UnknownHostException) {
            Log.e("NetworkRepo: ", e.message.toString())

        } catch (e: SocketTimeoutException) {
            Log.e("NetworkRepo: ", e.message.toString())

        } catch (e: Exception) {
            Log.e("NetworkRepo: ", e.message.toString())

        }catch (e: NoInternetException) {
            Log.e("NetworkRepo: ", e.message.toString())

        }


        return response?.body()
    }


}