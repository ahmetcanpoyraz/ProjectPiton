package com.example.projectpiton.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpiton.R
import com.example.projectpiton.model.Work
import com.example.projectpiton.service.WorkFirebaseService
import com.example.projectpiton.view.DetailActivity
import com.squareup.picasso.Picasso

class RecyclerAdapter(var arrayList: ArrayList<Work>) : RecyclerView.Adapter<RecyclerAdapter.ItemHolder>() {
    private var workFirebaseService = WorkFirebaseService()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var  work: Work = arrayList.get(position)
        holder.nameWork.text = work.workName
        holder.priorityWork.text = work.workPriority.toString()
        Picasso.get().load(work.workImage).resize(250,250).into(holder.imageWork)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("idOfWork",arrayList.get(position).workId)
            intent.putExtra("categoryOfWork",arrayList.get(position).workCategory)
            holder.itemView.context.startActivity(intent)
        }

        holder.imageDelete.setOnClickListener{
            arrayList.get(position).workCategory?.let { it1 ->
                arrayList.get(position).workId?.let { it2 ->
                    workFirebaseService.deleteDataFromFirebase(
                        it1, it2

                    )
                }
            }
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var imageWork = itemView.findViewById<ImageView>(R.id.item_imageView)
        var nameWork = itemView.findViewById<TextView>(R.id.item_name)
        var priorityWork = itemView.findViewById<TextView>(R.id.item_priority)
        var imageDelete = itemView.findViewById<ImageView>(R.id.image_delete)

    }

    fun updateProductList(newarrayList: ArrayList<Work>){

        arrayList.clear()
        arrayList.addAll(newarrayList)
        notifyDataSetChanged()

    }


}