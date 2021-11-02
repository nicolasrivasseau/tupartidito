package com.unlam.tupartidito.ui.detail_club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unlam.tupartidito.R
import com.unlam.tupartidito.data.model.club.Club
import kotlinx.android.synthetic.main.fragment_schedule_club.view.*

class ScheduleClubFragment(clubData:Club) : Fragment() {

    private var club : Club = clubData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_club,container,false)
//        view.txtClubName.text = club.id
        return view
    }
}