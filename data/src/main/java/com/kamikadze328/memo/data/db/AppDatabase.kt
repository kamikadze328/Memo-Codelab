package com.kamikadze328.memo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kamikadze328.memo.data.db.entity.MemoDb

/**
 * That database that is used to store information.
 */
@Database(
    entities = [MemoDb::class],
    version = 1,
    exportSchema = false,
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
}