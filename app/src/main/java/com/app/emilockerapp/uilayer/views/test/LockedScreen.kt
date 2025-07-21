package com.app.emilockerapp.uilayer.views.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.emilockerapp.coordinator.BaseChildNavGraph
import com.app.emilockerapp.utils.ComposeBaseExtensions.routeWithArgs

class LockedScreen(navHostController: NavHostController) : BaseChildNavGraph {
    object Routes {
        const val emi_Test_Screen = "emi_locker/test"
    }

    override fun createChildNavGraphBuilder(): NavGraphBuilder.() -> Unit {
        val output: NavGraphBuilder.() -> Unit = {
            composable(routeWithArgs(Routes.emi_Test_Screen)) {
                EmiTestScreenUI()
            }
        }
        return output
    }

    @Composable
    fun EmiTestScreenUI() {
        TestUiScreen()
    }
}

@Composable
fun TestUiScreen() {
    test() // This calls your beautiful lock screen UI
}

@Preview
@Composable
fun PreviewEmiTestScreenUI() {
    val mNavHostController = rememberNavController()
    LockedScreen(mNavHostController).EmiTestScreenUI()
}