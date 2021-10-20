package com.example.projectpiton.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.projectpiton.R
import com.example.projectpiton.databinding.ActivityDetailBinding
import com.example.projectpiton.viewmodel.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_upload.*

class DetailActivity : AppCompatActivity() {
    private lateinit var viewModel : DetailViewModel
    private lateinit var binding : ActivityDetailBinding
    private var priority = "More"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val id:String = intent.getStringExtra("idOfWork").toString()
        val categ:String = intent.getStringExtra("categoryOfWork").toString()
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.getData(categ,id)

        binding.detailBackButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.deletebutton.setOnClickListener {
            viewModel.deleteData(categ,id)
            Toast.makeText(this,"Work deleted", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
            finish()
        }
        binding.updatebutton.setOnClickListener {
            if (binding.detailDescription.text.isNotEmpty() && binding.detailName.text.isNotEmpty()

            ) {

                val hashMapWork = hashMapOf(
                    "workName" to binding.detailName.text.toString(),
                    "workDescription" to binding.detailDescription.text.toString(),

                    "workPriority" to priority,
                )
                viewModel.updateData(categ,id,hashMapWork)
                Toast.makeText(this,"Work Updated", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        observeLiveData()
    }

    override fun onResume() {
        super.onResume()
        val priorities = resources.getStringArray(R.array.prioritys)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,priorities)
        binding.autoCompleteTextView4.setAdapter(arrayAdapter)

        autoCompleteTextView4.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                priority = priorities.get(position).toString()
            }

    }


    private fun observeLiveData(){
        viewModel.workLiveData.observe(this, Observer { work->
           work?.let {
                detail_name.setText(work.workName)
                detail_description.setText(work.workDescription)
                autoCompleteTextView4.setText(work.workPriority)
                detail_category.text=work.workCategory
                Picasso.get().load(work.workImage).resize(250,250).into(detailImage)

               val priorities = resources.getStringArray(R.array.prioritys)
               val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,priorities)
               binding.autoCompleteTextView4.setAdapter(arrayAdapter)

               autoCompleteTextView4.onItemClickListener =
                   AdapterView.OnItemClickListener { parent, view, position, id ->
                       priority = priorities.get(position).toString()
                   }

            }

        })
    }
}