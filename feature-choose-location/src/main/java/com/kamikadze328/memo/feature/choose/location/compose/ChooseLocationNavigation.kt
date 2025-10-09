package com.kamikadze328.memo.feature.choose.location.compose

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kamikadze328.memo.navigation.ChooseLocation
import com.kamikadze328.memo.navigation.choose.location.ChooseLocationArgs
import com.kamikadze328.memo.navigation.setResult

fun NavGraphBuilder.registerChooseLocationGraph(navController: NavController) {
    composable<ChooseLocation>(typeMap = ChooseLocationArgs.typeMap) { backStackEntry ->
        ChooseLocationUi(
            onBack = { result ->
                navController.setResult(result)
                navController.popBackStack()
            }
        )
    }
}