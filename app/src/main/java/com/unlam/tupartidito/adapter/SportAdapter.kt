package com.unlam.tupartidito.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unlam.tupartidito.R
import com.unlam.tupartidito.data.model.sport.Sport
import kotlinx.android.synthetic.main.activity_login.view.*
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
        val club = dataList[position]

        with(holder.itemView){
            txtClubName.text = "test"
            txtDay.text = "test"
            txtLocation.text = "test"
            txtPrice.text = "test"
        }
//        holder.itemView.nameSport.text = club.url
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}