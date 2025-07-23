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
        // test()
        TestUiScreen() // load test() here
    }
}

@Composable // don't need extra function for this
fun TestUiScreen() {
    test() // This calls your beautiful lock screen UI
}

@Preview
@Composable
fun PreviewEmiTestScreenUI() {
    val mNavHostController = rememberNavController()
    LockedScreen(mNavHostController).EmiTestScreenUI()
}