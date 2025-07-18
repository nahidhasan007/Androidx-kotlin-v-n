package com.app.emilockerapp.uilayer.views.security

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.emilockerapp.coordinator.BaseChildNavGraph
import com.app.emilockerapp.utils.ComposeBaseExtensions.routeWithArgs

class SecurityScreen(navHostController: NavHostController) : BaseChildNavGraph {
    object Routes {
        const val emiSecurityScreen = "emi_locker/security"
    }

    override fun createChildNavGraphBuilder(): NavGraphBuilder.() -> Unit {
        val output: NavGraphBuilder.() -> Unit = {
            composable(routeWithArgs(Routes.emiSecurityScreen)) {
                EmiSecurityScreenUI()
            }

        }
        return output
    }

    @Composable
    fun EmiSecurityScreenUI() {
        SecurityScreenView()
    }
}


@Preview
@Composable
fun PreviewEmiSecurityScreenUI() {
    val mNavHostController = rememberNavController()
    SecurityScreen(mNavHostController).EmiSecurityScreenUI()
}