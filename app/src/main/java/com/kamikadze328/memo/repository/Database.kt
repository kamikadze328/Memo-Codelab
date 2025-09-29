package com.kamikadze328.memo.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kamikadze328.memo.model.Memo

/**
 * That database that is used to store information.
 */
@Database(entities = [Memo::class], version = 1, exportSchema = false)
internal abstract class Database : RoomDatabase() {

    abstract fun getMemoDao(): MemoDao
}