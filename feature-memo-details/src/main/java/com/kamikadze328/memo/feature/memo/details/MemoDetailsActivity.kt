package com.kamikadze328.memo.feature.memo.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kamikadze328.memo.core.ui.theme.AppTheme
import com.kamikadze328.memo.feature.memo.details.composable.MemoDetailsUi
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity that allows a user to create a new Memo.
 */
@AndroidEntryPoint
class MemoDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MemoDetailsUi(
                    args = MemoDetailsContract.parseResult(intent),
                    onFinish = {
                        setResult(RESULT_OK)
                        finish()
                    }
                )
            }
        }
    }
}