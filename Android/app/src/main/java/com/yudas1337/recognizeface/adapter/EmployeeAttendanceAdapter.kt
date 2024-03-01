package com.yudas1337.recognizeface.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yudas1337.recognizeface.R
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.break_time
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.present
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.return_break
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.return_home
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.status
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.name

class EmployeeAttendanceAdapter(private val context: Context, private val results: List<String>) : RecyclerView.Adapter<EmployeeAttendanceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.attendance_employee_adapter, parent, false)

        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.setData(result)

    }

    override fun getItemCount(): Int {
        return results.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun setData(result: String)
        {
            itemView.name!!.text = result
            itemView.status!!.text = result
            itemView.present!!.text = result
            itemView.break_time!!.text = result
            itemView.return_break!!.text = result
            itemView.return_home!!.text = result
        }
        override fun onClick(view: View) {
        }
    }
}
