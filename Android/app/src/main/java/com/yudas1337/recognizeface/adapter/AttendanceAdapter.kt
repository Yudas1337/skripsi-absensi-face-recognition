package com.yudas1337.recognizeface.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.constants.AttendanceStatus
import com.yudas1337.recognizeface.database.AttendanceData
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.helpers.CalendarHelper
import kotlinx.android.synthetic.main.attendance_adapter.view.break_time
import kotlinx.android.synthetic.main.attendance_adapter.view.name
import kotlinx.android.synthetic.main.attendance_adapter.view.present
import kotlinx.android.synthetic.main.attendance_adapter.view.return_break
import kotlinx.android.synthetic.main.attendance_adapter.view.return_home
import kotlinx.android.synthetic.main.attendance_adapter.view.school
import kotlinx.android.synthetic.main.attendance_adapter.view.status

class AttendanceAdapter(private val dbHelper: DBHelper, private val results: List<AttendanceData>) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.attendance_adapter, parent, false)

        return ViewHolder(v)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.setData(result)

    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return results.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(result: AttendanceData)
        {
            val day = dbHelper.getScheduleDay(CalendarHelper.getToday())

            itemView.name!!.text = result.name
            itemView.status!!.text = result.status
            itemView.school!!.text = result.school

            itemView.status!!.setTypeface(null, Typeface.BOLD)
            itemView.present!!.setTypeface(null, Typeface.BOLD)
            itemView.break_time!!.setTypeface(null, Typeface.BOLD)
            itemView.return_break!!.setTypeface(null, Typeface.BOLD)
            itemView.return_home!!.setTypeface(null, Typeface.BOLD)


            if (result.status == AttendanceStatus.DEFAULT_ATTENDANCE_STATUS) {
                itemView.status.setTextColor(Color.parseColor("#86C38F"))
            } else {
                itemView.status!!.text = AttendanceStatus.ALFA_STATUS
                itemView.status.setTextColor(Color.parseColor("#EC1616"))
            }

            if(result.present.isNotEmpty() && day.moveToFirst()){
                val presentTime = CalendarHelper.convertToHourAndSecond(result.present)
                if(presentTime.toString() <= CalendarHelper.addLimitAttendance(day, 1)){
                    itemView.present.setTextColor(Color.parseColor("#86C38F"))
                } else{
                    itemView.present.setTextColor(Color.parseColor("#F1B44C"))
                }
                itemView.present!!.text = CalendarHelper.convertToHours(result.present).toString()
            } else{
                itemView.present!!.text = ""
            }

            itemView.break_time!!.text = CalendarHelper.convertToHours(result.breakTime).toString()
            itemView.return_break!!.text = CalendarHelper.convertToHours(result.returnBreak).toString()
            itemView.return_home!!.text = CalendarHelper.convertToHours(result.returnHome).toString()


            if(result.breakTime.isNotEmpty()){
                itemView.break_time.setTextColor(Color.parseColor("#86C38F"))
            }

            if(result.returnBreak.isNotEmpty()){
                itemView.return_break.setTextColor(Color.parseColor("#86C38F"))
            }

            if(result.returnHome.isNotEmpty()){
                itemView.return_home.setTextColor(Color.parseColor("#86C38F"))
            }

        }

        override fun onClick(view: View) {
        }
    }
}