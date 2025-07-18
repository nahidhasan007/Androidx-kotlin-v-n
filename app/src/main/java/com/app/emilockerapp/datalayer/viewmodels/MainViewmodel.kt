package com.app.emilockerapp.datalayer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewmodel(application: Application) : AndroidViewModel(application){
    private val _isLocked = MutableStateFlow(false)
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    private val _showUnlockDialog = MutableStateFlow(false)
    val showUnlockDialog: StateFlow<Boolean> = _showUnlockDialog.asStateFlow()

    companion object {
        const val UNLOCK_PASSWORD = "admin123" // Change this to your desired password
    }

    fun toggleLock() {
        if (_isLocked.value) {
            _showUnlockDialog.value = true
        } else {
            _isLocked.value = true
        }
    }

    fun unlockApp() {
        _isLocked.value = false
        _showUnlockDialog.value = false
    }

    fun lockApp() {
        _isLocked.value = true
        _showUnlockDialog.value = false
    }

    fun attemptUnlock(password: String): Boolean {
        return if (password == UNLOCK_PASSWORD) {
            _isLocked.value = false
            _showUnlockDialog.value = false
            true
        } else {
            false
        }
    }

    fun hideUnlockDialog() {
        _showUnlockDialog.value = false
    }
}