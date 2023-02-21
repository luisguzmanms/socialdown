package com.lamesa.socialdown.downloader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.lamesa.socialdown.R
import com.lamesa.socialdown.ui.view.main.MainActivity
import java.util.*

class NotificationDownloading(val context: Context) {

    enum class NotificationID(val value: String) {
        ID("666"),
        CHANNELID("SDownloader")
    }

    internal fun provideNotificationBuilder(): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            val notificationChannel = NotificationChannel(
                NotificationID.CHANNELID.value,
                "SocialDown",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationManager!!.createNotificationChannel(notificationChannel)

            val intentMainActivity = Intent(context, MainActivity::class.java)
            intentMainActivity.action = Intent.ACTION_MAIN
            intentMainActivity.addCategory(Intent.CATEGORY_LAUNCHER)
            intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

            val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(
                    context,
                    Random().nextInt(),
                    intentMainActivity,
                    FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                )
            } else {
                PendingIntent.getActivity(context, Random().nextInt(), intentMainActivity, 0)
            }

            return NotificationCompat.Builder(context, NotificationID.CHANNELID.value)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("SocialDown")
                .setContentText(context.getString(R.string.text_downloading))
                .setContentIntent(pendingIntent)

        } else {

            val intentMainActivity = Intent(context, MainActivity::class.java)
            intentMainActivity.action = Intent.ACTION_MAIN
            intentMainActivity.addCategory(Intent.CATEGORY_LAUNCHER)
            intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

            val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(
                    context,
                    Random().nextInt(),
                    intentMainActivity,
                    FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                )
            } else {
                PendingIntent.getActivity(context, Random().nextInt(), intentMainActivity, 0)
            }

            return NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("SocialDown")
                .setContentText(context.getString(R.string.text_downloading))
                .setContentIntent(pendingIntent)

        }
    }

    internal fun notify(builder: NotificationCompat.Builder) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        return notificationManager!!.notify(NotificationID.ID.value.toInt(), builder.build())
    }

}