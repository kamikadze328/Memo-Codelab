package com.kamikadze328.memo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kamikadze328.memo.feature.choose.location.compose.registerChooseLocationGraph
import com.kamikadze328.memo.feature.home.composable.registerHomeGraph
import com.kamikadze328.memo.feature.memo.details.composable.registerCreateGraph
import com.kamikadze328.memo.navigation.Home

@Composable
fun MainUi() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home,
    ) {
        registerHomeGraph(navController)
        registerCreateGraph(navController)
        registerChooseLocationGraph(navController)
    }
}