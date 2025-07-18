package com.app.emilockerapp.uilayer.views.emi

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.emilockerapp.coordinator.BaseChildNavGraph
import com.app.emilockerapp.utils.ComposeBaseExtensions.routeWithArgs

class EmiScreen(navHostController: NavHostController) : BaseChildNavGraph {
    object Routes {
        const val emiScreen = "emi_locker/emiScreen"
    }

    override fun createChildNavGraphBuilder(): NavGraphBuilder.() -> Unit {
        val output: NavGraphBuilder.() -> Unit = {
            composable(routeWithArgs(Routes.emiScreen)) {
                EmiScreenUI()
            }

        }
        return output
    }

    @Composable
    fun EmiScreenUI() {
        EMIScreen()
    }
}


@Preview
@Composable
fun PreviewEmiScreenUI() {
    val mNavHostController = rememberNavController()
    EmiScreen(mNavHostController).EmiScreenUI()
}