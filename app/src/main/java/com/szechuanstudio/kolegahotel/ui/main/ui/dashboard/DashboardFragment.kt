package com.szechuanstudio.kolegahotel.ui.main.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.ui.dashboard.accepted.AcceptedActivity
import com.szechuanstudio.kolegahotel.ui.dashboard.active.ActiveActivity
import com.szechuanstudio.kolegahotel.ui.dashboard.history.HistoryActivity
import com.szechuanstudio.kolegahotel.ui.dashboard.pending.PendingActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.jetbrains.anko.support.v4.startActivity

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_active_jobs.setOnClickListener {
            startActivity<ActiveActivity>()
        }
        btn_accepted_jobs.setOnClickListener {
            startActivity<AcceptedActivity>()
        }
        btn_history_jobs.setOnClickListener {
            startActivity<HistoryActivity>()
        }
        btn_pending_jobs.setOnClickListener {
            startActivity<PendingActivity>()
        }
    }
}