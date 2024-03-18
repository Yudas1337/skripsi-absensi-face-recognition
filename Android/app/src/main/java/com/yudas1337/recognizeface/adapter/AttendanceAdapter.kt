package com.yudas1337.recognizeface.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.database.AttendanceData
import kotlinx.android.synthetic.main.attendance_adapter.view.break_time
import kotlinx.android.synthetic.main.attendance_adapter.view.name
import kotlinx.android.synthetic.main.attendance_adapter.view.present
import kotlinx.android.synthetic.main.attendance_adapter.view.return_break
import kotlinx.android.synthetic.main.attendance_adapter.view.return_home
import kotlinx.android.synthetic.main.attendance_adapter.view.school
import kotlinx.android.synthetic.main.attendance_adapter.view.status

class AttendanceAdapter(private val context: Context, private val results: List<AttendanceData>) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.attendance_adapter, parent, false)

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

        fun setData(result: AttendanceData)
        {
            itemView.name!!.text = result.name
            itemView.school!!.text = result.school
            itemView.status!!.text = "Masuk"
            if (itemView.status!!.text.equals("Masuk")) {
                itemView.status.setTextColor(Color.parseColor("#34EC16"))
                itemView.status.setBackgroundResource(R.drawable.bg_success)
            } else {
                itemView.status.setTextColor(Color.parseColor("#EC1616"))
                itemView.status.setBackgroundResource(R.drawable.bg_error)
            }

            itemView.present!!.text = result.present
            itemView.break_time!!.text = result.breakTime
            itemView.return_break!!.text = result.returnBreak
            itemView.return_home!!.text = result.returnHome
        }

        override fun onClick(view: View) {
        }
    }
}