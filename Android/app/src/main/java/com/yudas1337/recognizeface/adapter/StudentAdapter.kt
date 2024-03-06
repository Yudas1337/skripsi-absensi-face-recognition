package com.yudas1337.recognizeface.adapter

import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.constants.FaceFolder
import com.yudas1337.recognizeface.constants.Gender
import com.yudas1337.recognizeface.network.Result
import kotlinx.android.synthetic.main.student_adapter.view.name
import kotlinx.android.synthetic.main.student_adapter.view.school
import kotlinx.android.synthetic.main.student_adapter.view.student_photo
import java.io.File

class StudentAdapter(private val context: Context, private val results: List<Result>) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.student_adapter, parent, false)

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
            itemView.school!!.text = result.school

            val photo = File(FaceFolder.profileDir, "${FaceFolder.STUDENTS_DIR_FACES_NAME}/${result.photo}")

            val gender = if(result.gender == Gender.MALE){ R.drawable.ic_student_male } else{ R.drawable.ic_student_female }

            Picasso.get()
                .load(photo)
                .placeholder(gender)
                .into(itemView.student_photo)
        }


        override fun onClick(view: View) {


        }
    }

}