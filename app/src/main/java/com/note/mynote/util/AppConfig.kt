package com.android_learn.mynote.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class AppConfig {

    var sh: SharedPreferences
    private var editor: SharedPreferences.Editor


    @SuppressLint("CommitPrefEdits")
    constructor(context: Context) {
        sh = context.getSharedPreferences("setting", Context.MODE_PRIVATE)
        editor = sh.edit()
    }


    fun setLanguage(lang: String) {
        editor.putString("language", lang)
        editor.commit()
    }

    fun getLanguage() : String
    {
        return sh.getString("language" , "en")!!
    }


}