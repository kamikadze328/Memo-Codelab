package com.kamikadze328.memo.domain.repository

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.kamikadze328.memo.data.db.Database
import com.kamikadze328.memo.domain.model.Memo

private const val DATABASE_NAME: String = "codelab"

/**
 * The repository is used to retrieve data from a data source.
 */
internal object Repository : IMemoRepository {

    private lateinit var database: Database

    fun initialize(applicationContext: Context) {
        database =
            Room.databaseBuilder(applicationContext, Database::class.java, DATABASE_NAME).build()
    }

    @WorkerThread
    override fun saveMemo(memo: Memo) {
        database.getMemoDao().insert(memo)
    }

    @WorkerThread
    override fun getOpen(): List<Memo> = database.getMemoDao().getOpen()

    @WorkerThread
    override fun getAll(): List<Memo> = database.getMemoDao().getAll()

    @WorkerThread
    override fun getMemoById(id: Long): Memo = database.getMemoDao().getMemoById(id)
}