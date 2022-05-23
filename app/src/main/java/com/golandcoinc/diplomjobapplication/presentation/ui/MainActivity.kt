package com.golandcoinc.diplomjobapplication.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.golandcoinc.diplomjobapplication.R
import com.golandcoinc.diplomjobapplication.databinding.ActivityMainBinding
import com.golandcoinc.diplomjobapplication.utils.NavigateUtil
import com.golandcoinc.diplomjobapplication.viewmodels.MainViewModel
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

    private fun checkPermissions() {
        val permissionGranted =
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
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
            showApplication()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ), 1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("RESULT", "${grantResults[0]}")
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showApplication()
        } else {
            Toast.makeText(this, getString(R.string.permissions_denied), Toast.LENGTH_SHORT).show()
            this.finish()
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