package com.dicoding.picodiploma.submission2annisa.Option

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.submission2annisa.MainActivity
import com.dicoding.picodiploma.submission2annisa.R
import java.util.*

class AlarmReminder: BroadcastReceiver() {
    companion object{
        const val ALARM_MASSAGE = "alarm_massage"
        const val ALARM_REPEATING = "alarm_repeating"
        const val EXTRA_TYPES = "extra_types"

        private const val ID_REPEATING = 101
        private const val BODY_MESSAGE = "09:00 Silahkan Periksa Kembali User Favorit Anda."
    }


    override fun onReceive(context: Context, intent: Intent?) {
        val notif = intent?.getStringExtra(ALARM_MASSAGE)
        notif.let { showMessage(context, it) }
    }
    private fun showMessage( context: Context, Notif: String?) {

        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val Sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val build = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
            .setContentTitle("Github Users")
            .setContentText(Notif)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentIntent(pendingIntent)
            .setSound(Sound)
            .setAutoCancel(true)
            .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            build.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = build.build()
        notificationManagerCompat.notify(ID_REPEATING, notification)
    }
        fun setRepeating(context: Context, type: String){

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReminder::class.java)
            intent.putExtra(ALARM_MASSAGE, BODY_MESSAGE)
            intent.putExtra(EXTRA_TYPES, type)
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 9)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            val delayedIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, delayedIntent)

            //set
            Toast.makeText(context, "Reminder set up",
                Toast.LENGTH_SHORT).show()
        }
    fun setAlarm(context: Context): Boolean {
        val intent = Intent(context, AlarmReminder::class.java)
        return PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_NO_CREATE) != null
    }
    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReminder::class.java)
        val delayIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        delayIntent.cancel()
        alarmManager.cancel(delayIntent)
        Toast.makeText(context, "Reminder Cancelled",
            Toast.LENGTH_SHORT).show()
    }
}