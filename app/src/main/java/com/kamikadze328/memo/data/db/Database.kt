package com.kamikadze328.memo.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.kamikadze328.memo.domain.model.Memo

/**
 * That database that is used to store information.
 */
@Database(
    entities = [Memo::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
internal abstract class Database : RoomDatabase() {

    abstract fun getMemoDao(): MemoDao
}