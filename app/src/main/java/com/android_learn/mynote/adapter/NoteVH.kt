package com.android_learn.mynote.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.android_learn.mynote.R

class NoteVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

     val txt_title : AppCompatTextView = itemView.findViewById(R.id.txt_title)
     val txt_description : AppCompatTextView = itemView.findViewById(R.id.txt_description)
     val txt_time : AppCompatTextView = itemView.findViewById(R.id.txt_time)
     val txt_date : AppCompatTextView = itemView.findViewById(R.id.txt_date)
     val img_delete : AppCompatImageView = itemView.findViewById(R.id.img_delete)
     val img_edit : AppCompatImageView = itemView.findViewById(R.id.img_edit)


}