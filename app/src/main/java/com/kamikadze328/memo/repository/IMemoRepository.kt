package com.kamikadze328.memo.repository

import com.kamikadze328.memo.model.Memo
import kotlinx.coroutines.flow.Flow

/**
 * Interface for a repository offering memo related CRUD operations.
 */
internal interface IMemoRepository {

    /**
     * Saves the given memo to the database.
     */
    fun saveMemo(memo: Memo)

    /**
     * @return all memos currently in the database.
     */
    fun getAll(): List<Memo>

    /**
     * @return all memos currently in the database, except those that have been marked as "done".
     */
    fun getOpen(): List<Memo>

    /**
     * @return all memos currently in the database, except those that have been marked as "done".
     */
    fun collectOpened(): Flow<List<Memo>>

    /**
     * @return the memo whose id matches the given id.
     */
    fun getMemoById(id: Long): Memo
}