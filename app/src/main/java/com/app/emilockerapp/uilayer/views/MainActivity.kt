package com.app.emilockerapp.uilayer.views

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.app.emilockerapp.coordinator.BaseChildNavGraph
import com.app.emilockerapp.coordinator.BaseNavCoordinator
import com.app.emilockerapp.datalayer.viewmodels.MainViewmodel
import com.app.emilockerapp.services.DeviceAdminReceiver
import com.app.emilockerapp.services.LockService
import com.app.emilockerapp.ui.theme.EmiLockerAppTheme
import com.app.emilockerapp.uilayer.views.dashboard.HomeScreen
import com.app.emilockerapp.uilayer.views.emi.EmiScreen
import com.app.emilockerapp.uilayer.views.security.SecurityScreen
import com.app.emilockerapp.uilayer.views.auth.LoginScreen
import com.app.emilockerapp.uilayer.views.test.TestClass
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var adminComponent: ComponentName

    private lateinit var viewModel: MainViewmodel

    private val testClass : TestClass by inject()

    private val adminRequestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Device admin enabled", Toast.LENGTH_SHORT).show()
//            setupKioskMode()
        } else {
            Toast.makeText(this, "Device admin not enabled", Toast.LENGTH_SHORT).show()
        }
    }

    private var _appCoordinator: EmiLockerNavCoordinator? = null
    private val appCoordinator get() = _appCoordinator!!
    private var bottomPadding = 0.dp

    private val lockDevice = true

    companion object {
        private const val CHANNEL_ID = "ForegroundServiceChannel"
        const val POST_NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        startKioskMode(this)
        val result = testClass.perform()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MainViewmodel::class.java]

        enableEdgeToEdge()
        setupDeviceAdmin()
        checkAndRequestPermissions()
        startBackgroundService()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        setContent {
            WelcomeScreen(result)
//            MainView()
        }
        if (lockDevice) {
            Toast.makeText(this, "Locking device...", Toast.LENGTH_SHORT).show()
            lockDevice()
        } else {
            Toast.makeText(this, "Unlocking device...", Toast.LENGTH_SHORT).show()
            unlockDevice()
        }

    }

    override fun onResume() {
        super.onResume()
//        startKioskMode(this@MainActivity)

    }

    fun runRootCommand(command: String): String {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()
            output
        } catch (e: Exception) {
            e.printStackTrace()
            "Error"
        }
    }

    fun lockDevice() {
        // Kill system UI (removes status bar, nav bar)
        runRootCommand("pkill com.android.systemui")

        // Optional: Disable settings app
        runRootCommand("pm disable-user --user 0 com.android.settings")

        // Optional: Launch kiosk app again
        runRootCommand("am start -n com.app.emilockerapp/.MainActivity")
    }

    fun unlockDevice() {
        // Restart system UI
        runRootCommand("am startservice -n com.android.systemui/.SystemUIService")

        // Enable settings again
        runRootCommand("pm enable com.android.settings")
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkAndRequestPermissions() {
        // Check if SMS permissions are granted

        // Check if notification permission is granted
        val notificationPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED


        // If permissions are not granted, request them
        val permissionsToRequest = mutableListOf<String>()


        if (!notificationPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }


        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                POST_NOTIFICATION_PERMISSION_REQUEST_CODE // permission @nahid Use the same request code for all permissions
            )
        }
    }

    private fun startBackgroundService() {
        createNotificationChannel()
        val intent = Intent(this, LockService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setupDeviceAdmin() {
        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        adminComponent = ComponentName(this, DeviceAdminReceiver::class.java)

        if (!devicePolicyManager.isAdminActive(adminComponent)) {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent)
                putExtra(
                    DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "This app requires device admin permissions to function as a kiosk."
                )
            }
            adminRequestLauncher.launch(intent)
        } else {
//            setupKioskMode()
        }
    }

    private fun disableDeviceAdmin() {
        devicePolicyManager.removeActiveAdmin(adminComponent)
        Toast.makeText(this, "Device Admin Disabled", Toast.LENGTH_SHORT).show()
    }

    private fun setupKioskMode() {
        if (devicePolicyManager.isAdminActive(adminComponent)) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )

            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            if (devicePolicyManager.isLockTaskPermitted(packageName)) {
                startLockTask()
            }
        }
    }

    private fun lockApp() {
        if (devicePolicyManager.isAdminActive(adminComponent)) {
            startLockTask()
        }
        Toast.makeText(this, "App locked", Toast.LENGTH_SHORT).show()
    }

    private fun unlockApp() {
        if (devicePolicyManager.isAdminActive(adminComponent)) {
            stopLockTask()
        }
        Toast.makeText(this, "App unlocked", Toast.LENGTH_SHORT).show()
    }

    private fun makeEmergencyCall() {
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:911")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:911")
                }
                startActivity(dialIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Emergency call failed", Toast.LENGTH_SHORT).show()
        }
    }

    @Composable
    fun WelcomeScreen(content : String){
        Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = content)
    }

    @Composable
    fun MainView() {
        EmiLockerAppTheme(darkTheme = false) {
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                bottomPadding = innerPadding.calculateBottomPadding()
                val mainModifier = Modifier.padding(innerPadding)

                val mNavHostController = androidx.navigation.compose.rememberNavController()
                _appCoordinator = EmiLockerNavCoordinator(
                    mNavHostController,
                    mainModifier,
                    createListOfScreensInsideComposable(mNavHostController),
                    LoginScreen.Routes.emi_Login_Screen,
                    this
                )
                appCoordinator.EmiLockerNavHost()
            }
        }
    }

    private fun createListOfScreensInsideComposable(
        mNavHostController: NavHostController,
    ): List<BaseChildNavGraph> {

        val mList = mutableListOf(
            HomeScreen(mNavHostController),
            EmiScreen(mNavHostController),
            LoginScreen(mNavHostController),
            SecurityScreen(mNavHostController)
        )
        return mList
    }
}


class EmiLockerNavCoordinator(
    mNavHostController: NavHostController,
    mainModifier: Modifier,
    listOfChildNavGraphs: List<BaseChildNavGraph>,
    mStartDestination: String,
    mActivity: AppCompatActivity
) : BaseNavCoordinator(
    mNavHostController, mainModifier, listOfChildNavGraphs, mStartDestination, mActivity
) {

    @Composable
    fun EmiLockerNavHost() {
        BaseNavHost()
    }
}