package dev.itchybit.thunderbuddy

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import dev.itchybit.thunderbuddy.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSIONS_REQUEST_LOCATION = 100
    }

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
        setupLocationService()
    }

    private fun setupUi() = with(binding) {
        setupDrawer()
    }

    private fun setupDrawer() = with(binding) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        drawerLayout.setScrimColor(
            ContextCompat.getColor(
                this@MainActivity, android.R.color.transparent
            )
        )
        actionBarDrawerToggle.syncState()
    }

    private fun setupLocationService() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermissions()) {
            requestLocation()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 0
            )
        }
    }

    private fun checkPermissions(): Boolean = ContextCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation()
            } else {
                Toast.makeText(this, "Location permission is required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            Log.d("Main", "${it.latitude} ${it.longitude}")
            viewModel.getCurrentWeather(it.latitude.toString(), it.longitude.toString())
            viewModel.get5DayForecast(it.latitude.toString(), it.longitude.toString())
        }
    }
}