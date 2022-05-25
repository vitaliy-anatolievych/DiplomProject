package com.golandcoinc.diplomjobapplication.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.golandcoinc.diplomjobapplication.R
import com.golandcoinc.diplomjobapplication.databinding.ActivityMainBinding
import com.golandcoinc.diplomjobapplication.utils.NavigateUtil
import com.golandcoinc.diplomjobapplication.viewmodels.MainViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        checkPermissions()
    }

    override fun onBackPressed() {
        if (NavigateUtil.currentFragment is StatsFragment) NavigateUtil.navigateTo(
            supportFragmentManager,
            MainFragment.newInstance()
        )
        else moveTaskToBack(true)
    }


    private fun checkGoogleServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()

        val checkGooglePlayServices =
            apiAvailability.isGooglePlayServicesAvailable(this)

        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {

            Toast.makeText(
                this,
                getString(R.string.not_enabled_google_services),
                Toast.LENGTH_SHORT
            ).show()
            finish()

            return false
        }

        return true
    }

    private fun checkPermissions() {
        if (!checkGoogleServices()) return

        var permissionGranted =
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NETWORK_STATE
            ) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WAKE_LOCK
            ) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionGranted =
                permissionGranted && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        }


        if (permissionGranted) {
            showApplication()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ), 1
                )
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WAKE_LOCK,
                    ), 1
                )
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED
        if (requestCode == 1&& grantResults.isNotEmpty()) {
            var permissionsGranted = true
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionsGranted = false
                }
            }

            if (permissionsGranted) {
                showApplication()
            } else {
                Toast.makeText(this, getString(R.string.permissions_denied), Toast.LENGTH_SHORT).show()
                this.finish()
            }
        }
    }

    private fun showApplication() {
        mainViewModel.getRecommendedSpeed()

        mainViewModel.recommendedSpeed.observe(this) {
            if (it != null) {
                mainViewModel.isHaveNotesTripJournal()
                NavigateUtil.navigateTo(supportFragmentManager, MainFragment.newInstance())
            } else {
                NavigateUtil.navigateTo(supportFragmentManager, WelcomeFragment.newInstance())
            }
        }
    }
}