package com.kamikadze328.memo.repository

import androidx.room.Room
import android.content.Context
import androidx.annotation.WorkerThread
import com.kamikadze328.memo.model.Memo
import com.kamikadze328.memo.repository.migrations.MIGRATION_1_2

private const val DATABASE_NAME: String = "codelab"

/**
 * The repository is used to retrieve data from a data source.
 */
internal object Repository : IMemoRepository {

    private lateinit var database: Database

    fun initialize(applicationContext: Context) {
        database = Room
            .databaseBuilder(applicationContext, Database::class.java, DATABASE_NAME)
            .addMigrations(
                MIGRATION_1_2,
            )
            .build()
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