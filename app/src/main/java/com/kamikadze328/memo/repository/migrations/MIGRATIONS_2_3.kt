package com.kamikadze328.memo.repository.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_memo_isDone ON memo(isDone)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_memo_isDone_lat_lon ON memo(isDone, reminderLatitude, reminderLongitude)")
    }
}