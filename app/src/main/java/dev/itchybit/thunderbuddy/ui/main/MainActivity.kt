package dev.itchybit.thunderbuddy.ui.main

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import dev.itchybit.thunderbuddy.R
import dev.itchybit.thunderbuddy.databinding.ActivityMainBinding
import dev.itchybit.thunderbuddy.io.api.service.WeatherService

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSIONS_REQUEST_LOCATION = 100
    }

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
        setupLocationService()
        observeViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun setupUi() = with(binding) {
        setupDrawer()
    }

    private fun setupDrawer() = with(binding) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        actionBarDrawerToggle = ActionBarDrawerToggle(
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

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.map -> findNavController(R.id.nav_host_fragment).let {
                    if (it.currentDestination?.id == R.id.mainFragment) {
                        it.navigate(MainFragmentDirections.toMapFragment())
                    }
                }

                R.id.settings -> findNavController(R.id.nav_host_fragment).navigate(R.id.settingsFragment)

                else -> {}
            }
            drawerLayout.closeDrawers()
            true
        }
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
            viewModel.getCurrentWeather(it.latitude, it.longitude, getWeatherUnitsFromPreferences())
            viewModel.get5DayForecast(it.latitude, it.longitude, getWeatherUnitsFromPreferences())
        }
    }

    private fun observeViewModel() = with(viewModel) {
        onMainFragmentResumeEvent.observe(this@MainActivity) {
            binding.appBarLayout.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun getWeatherUnitsFromPreferences(): WeatherService.Units =
        when (PreferenceManager.getDefaultSharedPreferences(this).getString("units", "metric")) {
            "standard" -> WeatherService.Units.STANDARD
            "metric" -> WeatherService.Units.METRIC
            "imperial" -> WeatherService.Units.IMPERIAL
            else -> WeatherService.Units.METRIC
        }
}