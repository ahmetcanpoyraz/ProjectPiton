package com.example.projectpiton.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.projectpiton.service.WorkFirebaseService


class UploadViewModel : ViewModel() {

    private var workFirebaseService = WorkFirebaseService()


    fun upload(uri: Uri, category : String, priority : String, name : String, description : String ){

        workFirebaseService.uploadService(uri,category,priority,name,description)
    }
}