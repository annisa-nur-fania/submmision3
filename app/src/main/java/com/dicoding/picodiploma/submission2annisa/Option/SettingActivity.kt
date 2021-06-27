package com.dicoding.picodiploma.submission2annisa.Option

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.dicoding.picodiploma.submission2annisa.R
import com.dicoding.picodiploma.submission2annisa.databinding.ActivityMainBinding
import com.dicoding.picodiploma.submission2annisa.databinding.ActivitySettingBinding
import com.dicoding.picodiploma.submission2annisa.databinding.ItemRowAvatarBinding

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val Shared = applicationContext.getSharedPreferences(getString(R.string.key), Context.MODE_PRIVATE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnBahasa.setOnClickListener(this)
        binding.pilihBahsa.setOnClickListener(this)

        val edit = Shared.edit()
        val alarm = AlarmReminder()
        val RemainderPosition =  Shared.getBoolean(getString(R.string.statusreminder), false)
        binding.fiturReminder.isChecked = RemainderPosition
        binding.fiturReminder.setOnCheckedChangeListener { _, isChecked ->
            edit.putBoolean(getString(R.string.statusreminder), isChecked)
            edit.apply()

            if (!isChecked) {
                if (alarm.setAlarm(this)) {
                    alarm.cancelAlarm(this)
                }
            } else {
                alarm.setRepeating(this, AlarmReminder.ALARM_REPEATING)
            }
        }



    }

    override fun onClick(v: View?){
        if (v?.id == R.id.pilih_bahsa) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        } else if (v?.id == R.id.btn_bahasa) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}