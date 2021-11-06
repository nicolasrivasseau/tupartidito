package com.unlam.tupartidito.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.unlam.tupartidito.R
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.club.Schedule
import com.unlam.tupartidito.ui.detail_club.ScheduleClubFragment
import com.unlam.tupartidito.ui.detail_rent.DetailRentActivity
import kotlinx.android.synthetic.main.item_schedule.view.*

class SchedulesAdapter(var fa: Fragment, var club: Club) : RecyclerView.Adapter<SchedulesAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private var dataList = listOf<Schedule>()
    fun setDataSchedules(data: List<Schedule>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_schedule, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = dataList[position]
        with(holder.itemView) {
            tv_id_schedulde.text = schedule.id
            tv_slot.text = schedule.slot
            tv_duration.text = "(60 min)"
            tv_price.text = "$${schedule.price}"
            if (schedule.reserved) card_info.setCardBackgroundColor(Color.RED)
            val data = arrayOf(schedule.id, club.id, club.location,  schedule.price, schedule.slot)
            card_info.setOnClickListener {

                if (schedule.reserved) goToDetailtRent(data)
            }
        }
    }
    fun goToDetailtRent(data: Array<String?>){
        val intent = Intent(fa.context, DetailRentActivity::class.java)
        intent.putExtra("data",data)
        fa.activity?.startActivity(intent)
    }
    override fun getItemCount(): Int {
        return dataList.size
    }

}