package com.kamikadze328.memo.data

import com.kamikadze328.memo.data.db.entity.MemoDb
import com.kamikadze328.memo.domain.model.Memo
import com.kamikadze328.memo.domain.model.MemoLocation
import javax.inject.Inject

class MemoDbMapper @Inject constructor() {
    fun toDb(memo: Memo): MemoDb {
        return MemoDb(
            id = memo.id,
            title = memo.title,
            description = memo.description,
            reminderDate = memo.reminderDate,
            reminderLatitude = memo.reminderLocation?.latitude,
            reminderLongitude = memo.reminderLocation?.longitude,
            isDone = memo.isDone,
        )
    }

    fun toDomain(memos: List<MemoDb>): List<Memo> {
        return memos.map { toDomain(it) }
    }

    fun toDomain(memoDb: MemoDb): Memo {
        return Memo(
            id = memoDb.id,
            title = memoDb.title,
            description = memoDb.description,
            reminderDate = memoDb.reminderDate,
            reminderLocation = if (memoDb.reminderLatitude != null && memoDb.reminderLongitude != null) {
                MemoLocation(
                    latitude = memoDb.reminderLatitude,
                    longitude = memoDb.reminderLongitude,
                )
            } else {
                null
            },
            isDone = memoDb.isDone,
        )
    }
}