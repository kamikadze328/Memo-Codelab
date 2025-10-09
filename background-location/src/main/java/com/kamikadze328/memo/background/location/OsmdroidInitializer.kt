package com.kamikadze328.memo.background.location

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.startup.Initializer
import org.osmdroid.config.Configuration

class OsmdroidInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        with(Configuration.getInstance()) {
            load(
                context,
                PreferenceManager.getDefaultSharedPreferences(context)
            )
            userAgentValue = context.packageName
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
