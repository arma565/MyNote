package com.android_learn.mynote

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_learn.mynote.activity.SearchActivity
import com.android_learn.mynote.activity.SettingActivity
import com.android_learn.mynote.activity.addactivity.AddNoteActivity
import com.android_learn.mynote.adapter.HomeAdapter
import com.android_learn.mynote.databinding.ActivityMainBinding
import com.android_learn.mynote.db.AppDatabase
import com.android_learn.mynote.models.Type
import com.android_learn.mynote.util.AppConfig
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var appDatabase: AppDatabase
    lateinit var config : AppConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        config = AppConfig(this@MainActivity)
        val locale = Locale(config.getLanguage())
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales.get(0)
        }else{
            config.locale
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
        }
        ////////////////////////////////////////////////////
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        appDatabase = AppDatabase.getInstance(this)
        binding.fab.setOnClickListener {
            val intentAdd = Intent(applicationContext, AddNoteActivity::class.java)
            intentAdd.putExtra("type" , 0)
            startActivity(intentAdd)
        }
        showAll()
    }

    private fun showAll() {
        binding.recHome.adapter = HomeAdapter(applicationContext, appDatabase.noteDao().noteList())
        binding.recHome.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun onRestart() {
        super.onRestart()
        showAll()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.item_add ->
            {
                val intentAdd = Intent(applicationContext, AddNoteActivity::class.java)
                intentAdd.putExtra("type" , Type.ADD.type)
                startActivity(intentAdd)
            }
            R.id.item_search ->
            {
                val intentSearch = Intent(applicationContext, SearchActivity::class.java)
                startActivity(intentSearch)
            }
            R.id.item_setting ->
            {
                val intentSetting = Intent(applicationContext, SettingActivity::class.java)
                startActivity(intentSetting)
            }
            R.id.item_exit ->
            {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}