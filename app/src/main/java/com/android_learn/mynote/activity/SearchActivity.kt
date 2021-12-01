package com.android_learn.mynote.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_learn.mynote.R
import com.android_learn.mynote.adapter.HomeAdapter
import com.android_learn.mynote.databinding.ActivitySearchBinding
import com.android_learn.mynote.db.AppDatabase
import com.android_learn.mynote.models.Note

class SearchActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchBinding
    lateinit var appDatabase : AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDatabase = AppDatabase.getInstance(applicationContext)
        binding.imgBack.setOnClickListener{
            finish()
        }
        binding.edtSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               Log.e("s:" , s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val noteList : MutableList<Note> = appDatabase.noteDao().searchNote(s.toString())
                binding.recSearch.adapter = HomeAdapter(applicationContext , noteList)
                binding.recSearch.layoutManager = LinearLayoutManager(applicationContext , LinearLayoutManager.VERTICAL , false)
            }

            override fun afterTextChanged(s: Editable?) {
                Log.e("s:" , s.toString())
            }
        })
    }

    override fun onResume() {
        binding.recSearch.adapter = HomeAdapter(applicationContext , appDatabase.noteDao().noteList())
        binding.recSearch.layoutManager = LinearLayoutManager(applicationContext , LinearLayoutManager.VERTICAL , false)
        super.onResume()
    }
}