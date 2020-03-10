package com.szechuanstudio.kolegahotel.ui.dashboard.active

import com.szechuanstudio.kolegahotel.data.model.Model

interface ActiveView {
    fun showJobs(activeJobs: Model.JobDetail?)
    fun showCheckedTodolist(todolist: List<Model.ToDoList>?)
    fun reject(message: String?)
}