package com.szechuanstudio.kolegahotel.ui.dashboard.accepted.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.utils.Constant
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_todolist.*
import org.jetbrains.anko.support.v4.act
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodolistFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_todolist, container, false)
        dialog?.setTitle("To Do List")
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RetrofitClient.getInstance().getTodolist(arguments?.getInt(Constant.KEY_IMAGE_JOB), PreferenceUtils.getToken(act.applicationContext))
            .enqueue(object : Callback<Model.ToDoListResponse>{
                override fun onFailure(call: Call<Model.ToDoListResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<Model.ToDoListResponse>,
                    response: Response<Model.ToDoListResponse>
                ) {
                    if (response.isSuccessful){
                        rv_todo.layoutManager = LinearLayoutManager(act.baseContext)
                        rv_todo.adapter = response.body()?.todolist?.let { TodoAdapter(it) }
                        loading_todo.visibility = View.GONE
                    }
                }

            })
    }

}
