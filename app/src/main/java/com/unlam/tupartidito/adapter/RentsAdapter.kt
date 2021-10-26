package com.unlam.tupartidito.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unlam.tupartidito.R
import com.unlam.tupartidito.data.model.user.Rent
import kotlinx.android.synthetic.main.item_club.view.*

class RentsAdapter : RecyclerView.Adapter<RentsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private var _rents = listOf<Rent>()

    fun setRents(data: List<Rent>) {
        _rents = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_club, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return _rents.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rent = _rents[position]
        with(holder.itemView){
            txtClubName.text = rent.id_club
            txtDay.text = rent.slot
            txtLocation.text = rent.location
            txtPrice.text = rent.price
            txtDuration.text = " (60min.)"
        }
    }
}