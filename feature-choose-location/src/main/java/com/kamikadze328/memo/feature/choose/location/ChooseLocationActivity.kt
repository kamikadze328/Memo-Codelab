package com.kamikadze328.memo.feature.choose.location

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kamikadze328.memo.core.ui.theme.AppTheme
import com.kamikadze328.memo.feature.choose.location.compose.ChooseLocationUi
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChooseLocationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                ChooseLocationUi(
                    args = ChooseLocationContract.getArgs(intent),
                    onBack = {
                        setResult(
                            RESULT_OK,
                            ChooseLocationContract.createResult(it)
                        )
                        finish()
                    }
                )
            }
        }
    }
}