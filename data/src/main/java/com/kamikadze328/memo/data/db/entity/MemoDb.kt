package com.kamikadze328.memo.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class MemoDb(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "reminderDate")
    val reminderDate: Long,
    @ColumnInfo(name = "reminderLatitude")
    val reminderLatitude: Double?,
    @ColumnInfo(name = "reminderLongitude")
    val reminderLongitude: Double?,
    @ColumnInfo(name = "isDone")
    val isDone: Boolean = false,
)