package com.kamikadze328.memo.view.create.location

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.IntentCompat
import com.kamikadze328.memo.model.MemoLocation

class ChooseLocationContract : ActivityResultContract<MemoLocation?, MemoLocation?>() {
    companion object {
        private const val EXTRA = "location"

        fun getArgs(intent: Intent?): MemoLocation? {
            return intent?.let {
                IntentCompat.getParcelableExtra(
                    it,
                    EXTRA,
                    MemoLocation::class.java
                )
            }
        }

        fun createResult(output: MemoLocation): Intent {
            return Intent().apply {
                putExtra(EXTRA, output)
            }
        }
    }

    override fun createIntent(context: Context, input: MemoLocation?): Intent {
        return Intent(context, ChooseLocation::class.java).apply {
            putExtra(EXTRA, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): MemoLocation? {
        return intent?.let { IntentCompat.getParcelableExtra(it, EXTRA, MemoLocation::class.java) }
    }


}