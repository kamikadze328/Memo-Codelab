package com.kamikadze328.memo.background.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.kamikadze328.memo.domain.model.Memo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class LocationNotificationHelper @Inject constructor(
    @param:ApplicationContext
    private val applicationContext: Context
) {
    companion object {
        const val NOTIFICATION_LOCATION_SERVICE_ID = 1
        private const val LOCATION_SERVICE_CHANNEL_ID = "location_service_channel"
        private const val MEMO_NEARBY_CHANNEL_ID = "memo_location_channel"
        private const val MAX_LENGTH_LOCATED_MEMO_TEXT = 140
    }

    init {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        val locationServiceChannel = NotificationChannel(
            LOCATION_SERVICE_CHANNEL_ID,
            applicationContext.getString(R.string.location_notification_channel),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val memoNearbyChannel = NotificationChannel(
            MEMO_NEARBY_CHANNEL_ID,
            applicationContext.getString(R.string.memo_location_notification_channel),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager?.createNotificationChannel(locationServiceChannel)
        notificationManager?.createNotificationChannel(memoNearbyChannel)
    }

    fun createMemoLocatedNotification(memo: Memo): Notification {
        return NotificationCompat.Builder(applicationContext, MEMO_NEARBY_CHANNEL_ID)
            .setContentTitle(memo.title)
            .setContentText(memo.description.take(MAX_LENGTH_LOCATED_MEMO_TEXT))
            .setSmallIcon(R.drawable.ic_memo_location_notification_24dp)
            .setContentIntent(makeIntent())
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }

    fun createLocationServiceNotification(): Notification {
        return NotificationCompat.Builder(applicationContext, LOCATION_SERVICE_CHANNEL_ID)
            .setContentTitle(applicationContext.getString(R.string.location_notification_title))
            .setContentText(applicationContext.getString(R.string.location_notification_text))
            .setSmallIcon(R.drawable.ic_location_notification_24)
            .setContentIntent(makeIntent())
            .setPriority(NotificationManager.IMPORTANCE_LOW)
            .build()
    }

    private fun makeIntent(): PendingIntent? {
        val uri = "codelab://memo/home".toUri()
        val viewIntent = Intent(Intent.ACTION_VIEW, uri)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }

        return TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(viewIntent)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}