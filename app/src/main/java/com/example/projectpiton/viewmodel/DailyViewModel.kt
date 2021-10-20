package com.example.projectpiton.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectpiton.model.Work
import com.example.projectpiton.service.WorkFirebaseService

class DailyViewModel : ViewModel() {
    val works = MutableLiveData<List<Work>>()
    val workError = MutableLiveData<Boolean>()
    val workLoading = MutableLiveData<Boolean>()
    private val workArrayList = ArrayList<Work>()
    private var workFirebaseService = WorkFirebaseService()

    fun getWorks(orderValue : String){
        workFirebaseService.getData("Daily",orderValue,workLoading,workError,workArrayList,works)
    }
}