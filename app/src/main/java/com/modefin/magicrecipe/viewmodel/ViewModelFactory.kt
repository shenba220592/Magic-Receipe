package com.modefin.magicrecipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.modefin.magicrecipe.repository.NetworkRepository

class ViewModelFactory (
        private val repository: NetworkRepository
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FoodItemViewModel() as T
    }
}