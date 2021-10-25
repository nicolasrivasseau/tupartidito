package com.unlam.tupartidito.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unlam.tupartidito.R
import com.unlam.tupartidito.data.model.sport.Sport
import kotlinx.android.synthetic.main.item_club.view.*

class SportAdapter : RecyclerView.Adapter<SportAdapter.ViewHolder>() {

    private var dataList = listOf<Sport>()

    fun setDataList(data:List<Sport>){
        dataList = data
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_club, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var club = dataList[position]
        holder.itemView.itemClub.text = club.url
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}