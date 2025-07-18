package com.app.emilockerapp.composecomponents

import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BaseButton(
    modifier: Modifier,
    buttonColors: ButtonColors,
    content: @Composable () -> Unit,
    placeholderText: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {

}