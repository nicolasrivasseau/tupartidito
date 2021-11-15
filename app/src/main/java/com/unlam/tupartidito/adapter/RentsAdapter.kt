package com.unlam.tupartidito.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.unit.Constraints
import androidx.recyclerview.widget.RecyclerView
import com.unlam.tupartidito.R
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.ui.detail_rent.DetailRentActivity
import kotlinx.android.synthetic.main.item_rent.view.*

class RentsAdapter(var activity: Activity) : RecyclerView.Adapter<RentsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private var _rents = listOf<Rent>()

    fun setRents(data: List<Rent>) {
        _rents = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_rent, parent, false)
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
            txtPrice.text = "$${rent.price}"
            txtDuration.text = "(60min)"
            this.setOnClickListener {
                val data = rent.id_rent
                clickCard(data)
            }
        }
    }
    private fun clickCard(data: String?) {
        val intent = Intent(activity, DetailRentActivity::class.java)
        intent.putExtra(Constants.RENT_DATA, data)
        intent.putExtra(Constants.RENT_IS_RESERVED, true)
        activity.startActivity(intent)
    }

}