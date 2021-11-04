package com.unlam.tupartidito.ui.detail_club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.unlam.tupartidito.R
import com.unlam.tupartidito.adapter.SchedulesAdapter
import com.unlam.tupartidito.common.observe
import kotlinx.android.synthetic.main.fragment_schedule_club.view.*

private const val ID_CLUB = "idClub"

class ScheduleClubFragment : Fragment() {

    private var idClub: String? = null
    private lateinit var viewModel: DetailClubFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idClub = it.getString(ID_CLUB)
        }
        viewModel = ViewModelProvider(requireActivity()).get(DetailClubFragmentViewModel::class.java)

        with(viewModel) {
            observe(clubData) { club ->
                adapterSchedule = SchedulesAdapter()
                adapterSchedule.setDataSchedules(club?.schedules!!)
                requireView().rv_schedules.setHasFixedSize(true)
                requireView().rv_schedules.layoutManager = LinearLayoutManager(requireView().context)
                requireView().rv_schedules.adapter = adapterSchedule
            }
        }
        viewModel.getClubData(idClub)
    }

    private lateinit var adapterSchedule: SchedulesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_club, container, false)
        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(idClub: String) =
            ScheduleClubFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_CLUB, idClub)
                }
            }
    }
}