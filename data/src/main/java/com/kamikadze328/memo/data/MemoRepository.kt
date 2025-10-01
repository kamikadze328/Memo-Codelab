package com.kamikadze328.memo.data

import com.kamikadze328.memo.core.android.coroutines.IDispatcherProvider
import com.kamikadze328.memo.data.db.MemoDao
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.repository.IMemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * The repository is used to retrieve data from a data source.
 */
internal class MemoRepository @Inject constructor(
    private val localDataSource: MemoDao,
    private val mapper: MemoDbMapper,
    private val dispatcherProvider: IDispatcherProvider,
) : IMemoRepository {
    override suspend fun saveMemo(memo: Memo) = withContext(dispatcherProvider.io) {
        localDataSource.insert(mapper.toDb(memo))
    }

    override suspend fun getOpen(): List<Memo> = withContext(dispatcherProvider.io) {
        mapper.toDomain(localDataSource.getOpen())
    }

    override fun collectOpened(): Flow<List<Memo>> = localDataSource.collectOpened()
        .map { mapper.toDomain(it) }

    override suspend fun getAll(): List<Memo> = withContext(dispatcherProvider.io) {
        mapper.toDomain(localDataSource.getAll())
    }

    override suspend fun getMemoById(id: Long): Memo = withContext(dispatcherProvider.io) {
        mapper.toDomain(localDataSource.getMemoById(id))
    }
}