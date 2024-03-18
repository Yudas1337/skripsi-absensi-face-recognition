package com.yudas1337.recognizeface.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.constants.AttendanceStatus
import com.yudas1337.recognizeface.database.AttendanceData
import com.yudas1337.recognizeface.helpers.CalendarHelper
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.break_time
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.present
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.return_break
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.return_home
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.status
import kotlinx.android.synthetic.main.attendance_employee_adapter.view.name

class EmployeeAttendanceAdapter(private val context: Context, private val results: List<AttendanceData>) : RecyclerView.Adapter<EmployeeAttendanceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.attendance_employee_adapter, parent, false)

        return ViewHolder(v)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.setData(result)

    }

    override fun getItemCount(): Int {
        return results.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(result: AttendanceData)
        {
            itemView.name!!.text = result.name
            itemView.status!!.text = result.status
            if (result.status == AttendanceStatus.DEFAULT_ATTENDANCE_STATUS) {
                itemView.status.setTextColor(Color.parseColor("#34EC16"))
                itemView.status.setBackgroundResource(R.drawable.bg_success)
            } else {
                itemView.status!!.text = AttendanceStatus.ALFA_STATUS
                itemView.status.setTextColor(Color.parseColor("#EC1616"))
                itemView.status.setBackgroundResource(R.drawable.bg_error)
            }

            itemView.present!!.text = CalendarHelper.convertToHours(result.present).toString() ?: ""
            itemView.break_time!!.text = CalendarHelper.convertToHours(result.breakTime).toString() ?: ""
            itemView.return_break!!.text = CalendarHelper.convertToHours(result.returnBreak).toString() ?: ""
            itemView.return_home!!.text = CalendarHelper.convertToHours(result.returnHome).toString() ?: ""
        }

        override fun onClick(view: View) {
        }
    }
}
