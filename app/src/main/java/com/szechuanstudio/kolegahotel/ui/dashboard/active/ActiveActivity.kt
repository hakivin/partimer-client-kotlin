package com.szechuanstudio.kolegahotel.ui.dashboard.active

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.activity_active.*
import kotlinx.android.synthetic.main.content_active.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast


class ActiveActivity : AppCompatActivity(), ActiveView {

    private lateinit var presenter: ActivePresenter
    private lateinit var adapter: TodolistAdapter
    private lateinit var job: Model.JobDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active)
        presenter = ActivePresenter(this, RetrofitClient.getInstance(), applicationContext)
        initToolbar()
        loadContent()
    }

    private fun loadContent(){
        presenter.getActiveJobs()
        done_button.visibility = View.GONE
        showLoading(true)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun showJobs(activeJobs: Model.JobDetail?) {
        if (activeJobs != null) {
            tv_position_todolist.text = activeJobs.posisi?.nama_posisi
            tv_hotel_name_todolist.text = activeJobs.hotel?.profile?.nama
            tv_date_todolist.text = Utils.convertDate(activeJobs.tanggal_mulai)
            tv_time_end_todolist.text = "Finish before ${Utils.convertTime(activeJobs?.tanggal_mulai, activeJobs?.waktu_selesai)}"

            job = activeJobs
            presenter.getCheckedTodolist(job.id)
        }
    }

    override fun showCheckedTodolist(todolist: List<Model.ToDoList>?) {
        if (todolist != null) {
            adapter = TodolistAdapter(job.todolist, todolist)
            rv_todolist.layoutManager = LinearLayoutManager(this)
            rv_todolist.adapter = adapter
        }
        showLoading(false)
        done_button.setOnClickListener {
            presenter.doneJob(job.id)
            it.isEnabled = false
        }
        done_button.visibility = View.VISIBLE
    }

    override fun reject(message: String?) {
        showLoading(false)
        done_button.isEnabled = true
        message?.let { longToast(it) }
    }

    override fun success(message: String?) {
        message?.let { longToast(it) }
        finish()
    }

    private fun showLoading(state: Boolean){
        if (state)
            loading_active.visibility = View.VISIBLE
        else
            loading_active.visibility = View.GONE
    }
}
