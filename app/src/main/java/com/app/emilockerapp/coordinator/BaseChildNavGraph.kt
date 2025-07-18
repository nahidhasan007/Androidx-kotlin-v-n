package com.app.emilockerapp.coordinator

import androidx.navigation.NavGraphBuilder

interface BaseChildNavGraph {
    fun createChildNavGraphBuilder(): (NavGraphBuilder.() -> Unit)
}
