package com.android_learn.mynote.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.android_learn.mynote.R
import com.android_learn.mynote.databinding.ActivitySettingBinding
import com.android_learn.mynote.models.Language
import com.android_learn.mynote.util.AppConfig
import com.android_learn.mynote.util.Utility

class SettingActivity : AppCompatActivity() {
    lateinit var binding : ActivitySettingBinding
    var position : Int = 0
    private var language : String = ""
    lateinit var appConfig : AppConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appConfig = AppConfig(this)

        binding.spiLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, i: Int, id: Long) {
                position = i
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnSave.setOnClickListener {
            language = if (position == Language.PERSIAN.language) {
                Log.e("" , "")
                "fa"
            } else {
                "en"
            }
            appConfig.setLanguage(language)
            Utility().triggerRebirth(this)
        }

        binding.imgBack.setOnClickListener{
            finish()
        }






    }
}