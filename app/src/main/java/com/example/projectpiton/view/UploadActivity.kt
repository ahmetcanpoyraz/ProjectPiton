package com.example.projectpiton.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.projectpiton.R
import com.example.projectpiton.databinding.ActivityUploadBinding
import com.example.projectpiton.viewmodel.UploadViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_upload.*

class UploadActivity : AppCompatActivity() {
    private lateinit var viewModel : UploadViewModel
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture : Uri? = null
    private lateinit var binding : ActivityUploadBinding
    private var category = "Daily"
    private var priority = "More"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.uploadBackButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerLauncher()

        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,categories)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
            category = categories.get(position).toString()
        }

        val priorities = resources.getStringArray(R.array.prioritys)
        val arrayAdapter2 = ArrayAdapter(this,R.layout.dropdown_item,priorities)
        binding.autoCompleteTextView3.setAdapter(arrayAdapter2)

        autoCompleteTextView3.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                priority = priorities.get(position).toString()
            }

    }

    fun onClickUpload(view: View){
        val name = binding.uploadEditName.text.toString()
        val description = binding.uploadEditDescription.text.toString()

        if (selectedPicture != null && category != "" && priority != "" && name != "" && description != ""){
            viewModel = ViewModelProvider(this).get(UploadViewModel::class.java)
            viewModel.upload(selectedPicture!!,category!!,priority,name,description)
            Toast.makeText(this,"Work uploaded succesfully", Toast.LENGTH_LONG).show()
            finish()
        }else {
            Toast.makeText(this,"Please enter all values", Toast.LENGTH_LONG).show()
        }

    }
    fun onClickSelectImage(view: View){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed for gallery!", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let {
                        binding.uploadImageView.setImageURI(it)
                    }

                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->

            if(result){
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                Toast.makeText(this@UploadActivity,"Permission needed!", Toast.LENGTH_LONG).show()
            }

        }
    }
}