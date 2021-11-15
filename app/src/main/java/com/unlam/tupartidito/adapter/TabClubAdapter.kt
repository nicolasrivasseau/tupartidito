package com.unlam.tupartidito.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.unlam.tupartidito.ui.detail_club.DetailClubFragment
import com.unlam.tupartidito.ui.detail_club.ScheduleClubFragment

class TabClubAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val ID_CLUB = "idClub"
    private val RENTS = "rentsList"
    lateinit var idCLub: String
    lateinit var rents: ArrayList<String>

    lateinit var idClub: String
    fun setDataClub(clubData: String, rentsUser: ArrayList<String>) {
        idClub = clubData
        rents = rentsUser
    }

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = ScheduleClubFragment()
                val args = Bundle()
                args.putString(ID_CLUB,idClub)
                args.putStringArrayList(RENTS, rents)
                fragment.arguments = args
                return  fragment
            }
            1 -> DetailClubFragment()
            else -> ScheduleClubFragment()
        }
    }
}