package com.kamikadze328.memo.feature.memo.details

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.IntentCompat

class MemoDetailsContract : ActivityResultContract<MemoDetailsArgs, Boolean>() {
    companion object {
        private const val EXTRA = "args"

        fun parseResult(intent: Intent): MemoDetailsArgs {
            val dataUri = intent.data
            val memoIdFromDeepLink = dataUri?.getQueryParameter("id")?.toLongOrNull()
            if (memoIdFromDeepLink != null) {
                return MemoDetailsArgs(
                    openingType = MemoDetailsArgs.OpeningType.View(memoIdFromDeepLink)
                )
            }

            return IntentCompat.getParcelableExtra(intent, EXTRA, MemoDetailsArgs::class.java)
                ?: MemoDetailsArgs(openingType = MemoDetailsArgs.OpeningType.CreateNew)
        }
    }

    override fun createIntent(context: Context, input: MemoDetailsArgs): Intent {
        return Intent(context, MemoDetailsActivity::class.java).apply {
            putExtra(EXTRA, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?) = resultCode == RESULT_OK
}