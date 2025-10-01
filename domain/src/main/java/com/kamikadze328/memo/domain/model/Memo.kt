package com.kamikadze328.memo.domain.model

/**
 * Represents a memo.
 *
 * @param id The unique identifier of the memo.
 * @param title The title of the memo.
 * @param description The description of the memo.
 * @param reminderDate The date and time when the memo should be reminded.
 * @param reminderLocation The location where the memo should be reminded.
 * @param isDone Indicates whether the memo is marked as done.
 */
data class Memo(
    val id: Long,
    val title: String,
    val description: String,
    val reminderDate: Long,
    val reminderLocation: MemoLocation?,
    val isDone: Boolean,
) {
    companion object {
        val EMPTY = Memo(
            id = 0,
            title = "",
            description = "",
            reminderDate = 0,
            reminderLocation = null,
            isDone = false,
        )
    }
}