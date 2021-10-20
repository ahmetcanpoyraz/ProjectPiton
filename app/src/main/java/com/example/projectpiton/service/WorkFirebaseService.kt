package com.example.projectpiton.service

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.projectpiton.model.Work
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class WorkFirebaseService {
    private lateinit var db : FirebaseFirestore
    val firestore : FirebaseFirestore = Firebase.firestore
    val storage : FirebaseStorage = Firebase.storage

   fun getData(category : String,orderValue : String, workLoading : MutableLiveData<Boolean>, workError : MutableLiveData<Boolean>, workArrayList : ArrayList<Work>,works : MutableLiveData<List<Work>>){
        workLoading.value=true
        db = Firebase.firestore
        db.collection("${category}").orderBy("work"+"$orderValue", Query.Direction.DESCENDING).addSnapshotListener{ value, error ->

            if(error != null){
                workError.value=true
                workLoading.value=false
                error.printStackTrace()
            }else{
                if(value != null){
                    if (!value.isEmpty){
                        val documents = value.documents
                        workArrayList.clear()
                        for (document in documents){
                            val name = document.get("workName") as String
                            val category = document.get("workCategory") as String
                            val priority = document.get("workPriority") as String
                            val image = document.get("downloadUrl") as String
                            val description = document.get("workDescription") as String
                            val id = document.get("workId") as String
                            println(id)
                            val dailys = Work(id,category,name,description,priority,image)
                            workArrayList.add(dailys)
                            works.value=workArrayList
                            workLoading.value=false
                            workError.value=false

                        }
                    }
                }
            }
        }
    }

    fun deleteDataFromFirebase(category : String, workId : String){
        val db = Firebase.firestore
        val query = db.collection("$category").whereEqualTo("workId","$workId").get()
        query.addOnSuccessListener {
            for(document in it){
                db.collection("$category").document(document.id).delete().addOnSuccessListener {

                }
            }
        }

    }

    fun updateDataFromFirebase(category : String, workId : String,workHashMap : HashMap<String,String>){
        val db = Firebase.firestore
        val query = db.collection("$category").whereEqualTo("workId","$workId").get()
        query.addOnSuccessListener {
            for(document in it){
                db.collection("$category").document(document.id).set(workHashMap, SetOptions.merge()).addOnSuccessListener {
                }
            }
        }
    }

    fun getDataForDetail(category : String, workId : String, workError: MutableLiveData<Boolean>, workArrayList: ArrayList<Work>, workLiveData : MutableLiveData<Work>){
        db = Firebase.firestore
        db.collection("$category").whereEqualTo("workId","$workId").addSnapshotListener{ value, error ->

            if(error != null){
                workError.value=true
                error.printStackTrace()
            }else{
                if(value != null){
                    if (!value.isEmpty){
                        val documents = value.documents
                        workArrayList.clear()
                        for (document in documents){
                            val name = document.get("workName") as String
                            val category = document.get("workCategory") as String
                            val priority = document.get("workPriority") as String
                            val image = document.get("downloadUrl") as String
                            val description = document.get("workDescription") as String
                            val id = document.get("workId") as String
                            val work = Work(id,category,name,description,priority,image)

                            workLiveData.value = work
                            workError.value = false
                        }
                    }
                }
            }
        }
    }

    fun uploadService(uri: Uri, category : String, priority : String, name : String, description : String ){
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)
        val uuid2 = UUID.randomUUID()
        val randId="$uuid2"
        if (uri != null){
            imageReference.putFile(uri!!).addOnSuccessListener {
                val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()

                    val postMap = hashMapOf<String,Any>()
                    postMap.put("workId",randId)
                    postMap.put("downloadUrl",downloadUrl)
                    postMap.put("workCategory",category)
                    postMap.put("workPriority",priority)
                    postMap.put("workName",name)
                    postMap.put("workDescription",description)
                    postMap.put("workDate",com.google.firebase.Timestamp.now())

                    firestore.collection("$category").add(postMap).addOnSuccessListener {

                    }.addOnFailureListener{
                        it.printStackTrace()
                    }

                }

            }.addOnFailureListener{
                it.printStackTrace()
            }
        }
    }


}