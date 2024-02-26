package com.yudas1337.recognizeface.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.network.Result
import kotlinx.android.synthetic.main.employee_adapter.view.employee_photo
import kotlinx.android.synthetic.main.employee_adapter.view.name
import kotlinx.android.synthetic.main.employee_adapter.view.position

class EmployeeAdapter(private val context: Context, private val results: List<Result>) : RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.employee_adapter, parent, false)

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

        fun setData(result: Result)
        {
            itemView.name!!.text = result.name

            itemView.position!!.text = result.position
            Picasso.get().load(result.photo).into(itemView.employee_photo)
        }


        override fun onClick(view: View) {
        }
    }
}