package com.example.projectpiton.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectpiton.R
import com.example.projectpiton.adapter.RecyclerAdapter
import com.example.projectpiton.model.Work
import com.example.projectpiton.viewmodel.DailyViewModel
import com.example.projectpiton.viewmodel.MonthlyViewModel
import kotlinx.android.synthetic.main.fragment_daily.*


class MonthlyFragment : Fragment() {

    private lateinit var viewModel: MonthlyViewModel
    private val workAdapter = RecyclerAdapter(arrayListOf())
    private var linearLayoutManager: LinearLayoutManager? = null
    private var orderBy = "Date"
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_monthly, container, false)
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView2)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderbys = resources.getStringArray(R.array.orderby)
        val arrayAdapter =
            activity?.let { ArrayAdapter(it.applicationContext, R.layout.dropdown_item, orderbys) }

        autoCompleteTextView.setAdapter(arrayAdapter)

        autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                orderBy = orderbys.get(position).toString()
                viewModel = ViewModelProvider(this).get(MonthlyViewModel::class.java)
                viewModel.getWorks(orderBy)
            }

        viewModel = ViewModelProvider(this).get(MonthlyViewModel::class.java)
        viewModel.getWorks(orderBy)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_works.layoutManager = linearLayoutManager
        recycler_works.adapter = workAdapter


        swipeRefreshLayout.setOnRefreshListener {
            recycler_works.visibility = View.GONE
            error_works.visibility = View.GONE
            workLoading.visibility = View.VISIBLE
            viewModel.getWorks(orderBy)
            swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()

    }



    private fun observeLiveData() {
        viewModel.works.observe(viewLifecycleOwner, Observer { works ->
            works?.let {
                error_works.visibility = View.GONE
                recycler_works.visibility = View.VISIBLE
                workAdapter.updateProductList(works as ArrayList<Work>)

            }
        })
        viewModel.workError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    error_works.visibility = View.VISIBLE
                } else {
                    error_works.visibility = View.GONE
                }
            }
        })
        viewModel.workLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading.let {
                if (it) {
                    workLoading.visibility = View.VISIBLE
                    recycler_works.visibility = View.GONE
                    error_works.visibility = View.GONE
                } else {
                    workLoading.visibility = View.GONE
                }
            }
        })
    }
}