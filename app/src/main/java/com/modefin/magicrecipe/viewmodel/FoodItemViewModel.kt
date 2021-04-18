package com.modefin.magicrecipe.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.modefin.magicrecipe.api.Coroutines
import com.modefin.magicrecipe.model.FoodListResponse
import com.modefin.magicrecipe.repository.NetworkRepository
import kotlinx.coroutines.Job

class FoodItemViewModel : ViewModel()  {
    var networkRepository: NetworkRepository? = null
    private val _foodListData = MutableLiveData<FoodListResponse>()
    val foodListData: LiveData<FoodListResponse>
        get() = _foodListData

    private lateinit var job: Job


    init {
        networkRepository = NetworkRepository()
    }

    fun getFoodItems(ingredeients:String,pageNo:Int) {
        job = Coroutines.ioThenMain(
            { networkRepository!!.getFoodList(ingredeients,pageNo) },
            { _foodListData.value = it }

        )
    }

}