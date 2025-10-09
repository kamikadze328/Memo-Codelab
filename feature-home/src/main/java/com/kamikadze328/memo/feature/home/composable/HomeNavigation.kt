package com.kamikadze328.memo.feature.home.composable

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.kamikadze328.memo.navigation.Home
import com.kamikadze328.memo.navigation.MemoDetails


fun NavGraphBuilder.registerHomeGraph(navController: NavController) {
    composable<Home>(
        deepLinks = listOf(navDeepLink { uriPattern = "codelab://com.kamikadze328.memo/home" })
    ) {
        HomeUi(
            navigateDetailsMemo = { args ->
                navController.navigate(MemoDetails(args))
            }
        )
    }
}