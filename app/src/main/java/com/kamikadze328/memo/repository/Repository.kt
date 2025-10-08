package com.kamikadze328.memo.repository

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.kamikadze328.memo.model.Memo
import com.kamikadze328.memo.repository.migrations.MIGRATION_1_2
import kotlinx.coroutines.flow.Flow

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
    override fun collectOpened(): Flow<List<Memo>> = database.getMemoDao().collectOpened()

    @WorkerThread
    override fun getOpen(): List<Memo> = database.getMemoDao().getOpen()

    @WorkerThread
    override fun getAll(): List<Memo> = database.getMemoDao().getAll()

    @WorkerThread
    override fun getMemoById(id: Long): Memo = database.getMemoDao().getMemoById(id)

    @WorkerThread
    override suspend fun findNearMemoByFlatDistance(
        latitude: Double,
        longitude: Double,
        radiusMeters: Double
    ): List<Memo> {
        val latCos = kotlin.math.cos(Math.toRadians(latitude))
        val degPerLat = radiusMeters / 111_320.0
        val degPerLon = radiusMeters / (111_320.0 * latCos)

        return database.getMemoDao().findNearOpenedMemoByFlatDistance(
            lat = latitude,
            lon = longitude,
            latCos = latCos,
            degPerLat = degPerLat,
            degPerLon = degPerLon,
            radiusMeters = radiusMeters
        )
    }
}