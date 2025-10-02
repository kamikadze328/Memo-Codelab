package com.kamikadze328.memo.feature.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kamikadze328.memo.core.ui.theme.AppTheme
import com.kamikadze328.memo.feature.home.composable.HomeUi
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity of the app. Shows a list of recorded memos and lets the user add new memos.
 */
@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                HomeUi()
            }
        }
    }
}
