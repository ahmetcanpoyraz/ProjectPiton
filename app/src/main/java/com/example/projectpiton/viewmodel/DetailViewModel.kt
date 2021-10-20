package com.example.projectpiton.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectpiton.model.Work
import com.example.projectpiton.service.WorkFirebaseService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailViewModel: ViewModel() {

    val works = MutableLiveData<List<Work>>()
    val workError = MutableLiveData<Boolean>()
    private lateinit var db : FirebaseFirestore
    private val workArrayList = ArrayList<Work>()
    val workLiveData = MutableLiveData<Work>()
    private var workFirebaseService = WorkFirebaseService()

    fun getData(category : String, workId : String){
      workFirebaseService.getDataForDetail(category,workId,workError,workArrayList,workLiveData)
    }

    fun deleteData(category : String, workId : String){

        workFirebaseService.deleteDataFromFirebase(category,workId)
    }

    fun updateData(category : String, workId : String,workHashMap : HashMap<String,String>){

        workFirebaseService.updateDataFromFirebase(category,workId,workHashMap)
    }


}