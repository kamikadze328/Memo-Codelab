package com.kamikadze328.memo.feature.memo.details.composable

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.kamikadze328.memo.navigation.ChooseLocation
import com.kamikadze328.memo.navigation.Home
import com.kamikadze328.memo.navigation.MemoDetails
import com.kamikadze328.memo.navigation.MemoDetailsDeepLink
import com.kamikadze328.memo.navigation.memo.details.MemoDetailsArgs
import com.kamikadze328.memo.navigation.memo.details.MemoDetailsArgs.OpeningType

fun NavGraphBuilder.registerCreateGraph(navController: NavController) {
    composable<MemoDetails>(typeMap = MemoDetailsArgs.typeMap) {
        MemoDetailsUi(
            onChooseLocation = { args ->
                navController.navigate(ChooseLocation(args))
            },
            onFinish = {
                navController.popBackStack()
            },
            navController = navController,
        )
    }

    composable<MemoDetailsDeepLink>(
        deepLinks = listOf(
            navDeepLink { uriPattern = "codelab://com.kamikadze328.memo/detail?id={memoId}" }
        )
    ) { backStackEntry ->
        LaunchedEffect(backStackEntry) {
            val deepLinkMemoId =
                backStackEntry.savedStateHandle.get<String>("memoId")?.toLongOrNull()
            val args = deepLinkMemoId?.let { MemoDetailsArgs(OpeningType.View(it)) }

            if (args == null) {
                navController.navigate(Home) {
                    launchSingleTop = true
                    popUpTo(MemoDetailsDeepLink) { inclusive = true }
                }
            } else {
                navController.navigate(MemoDetails(args)) {
                    popUpTo(MemoDetailsDeepLink) { inclusive = true }
                }
            }
        }
    }
}