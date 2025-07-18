package com.app.emilockerapp.uilayer.views.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.emilockerapp.coordinator.BaseChildNavGraph
import com.app.emilockerapp.utils.ComposeBaseExtensions.routeWithArgs

class HomeScreen(private val navHostController: NavHostController) : BaseChildNavGraph {

    object Routes {
        const val lockerHomeScreen = "emi_locker/homeScreen"
    }

    companion object {
        val route = Routes.lockerHomeScreen
    }

    override fun createChildNavGraphBuilder(): NavGraphBuilder.() -> Unit {
        val output: NavGraphBuilder.() -> Unit = {
            composable(routeWithArgs(Routes.lockerHomeScreen)) {
                HomeUIScreen()
            }
        }
        return output
    }

    @Composable
    fun HomeUIScreen() {
        var selectedIndex by remember { mutableIntStateOf(0) }
        Scaffold(
            bottomBar = {
                CustomBottomNavigation(
                    selectedIndex,
                    onItemClick = { index ->
                        selectedIndex = index
                    },
                   navHostController
                )
            },
            content = { contentPadding ->
                Box(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize()
                ) {
                   DashboardScreen()
                }
            },
        )
    }

}

@Preview
@Composable
fun PreviewHomeScreen() {
    val mNavHostController = rememberNavController()
    HomeScreen(mNavHostController).HomeUIScreen()
}