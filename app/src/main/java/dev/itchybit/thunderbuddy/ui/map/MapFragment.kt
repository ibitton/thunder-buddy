package dev.itchybit.thunderbuddy.ui.map

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.addMarker
import dev.itchybit.thunderbuddy.R
import dev.itchybit.thunderbuddy.databinding.FragmentMapBinding
import dev.itchybit.thunderbuddy.io.db.entity.Favourite
import dev.itchybit.thunderbuddy.ui.main.MainViewModel
import dev.itchybit.thunderbuddy.util.LocationUtil.getFromLocationNameCompat

class MapFragment : Fragment(R.layout.fragment_map) {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var supportMapFragment: SupportMapFragment
    private var currentMarker: Marker? = null
    private val markers = ArrayList<Marker?>()

    private lateinit var favouriteAdapter: FavouriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setupMap()
        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupUi() = with(binding) {
        favouriteAdapter = FavouriteAdapter { viewModel.removeFavourite(it) }
        favourites.adapter = favouriteAdapter

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val geocoder = Geocoder(requireContext())
                    geocoder.getFromLocationNameCompat(query, 1) { addresses ->
                        if (!addresses.isNullOrEmpty()) {
                            requireActivity().runOnUiThread {
                                supportMapFragment.getMapAsync {
                                    val latLng =
                                        LatLng(addresses[0].latitude, addresses[0].longitude)
                                    currentMarker?.remove()
                                    currentMarker = it.addMarker { position(latLng).title(query) }
                                    it.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
                                    markers.add(currentMarker)
                                }
                            }
                        }
                    }
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        addFavourite.setOnClickListener {
            if (currentMarker != null) {
                viewModel.addToFavourites(
                    currentMarker?.title!!,
                    currentMarker?.position?.latitude!!,
                    currentMarker?.position?.longitude!!
                )
            }
        }
    }

    private fun setupMap() = with(binding) {
        supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        supportMapFragment.getMapAsync { googleMap ->
            googleMap.setOnMapLoadedCallback {
                viewModel.currentWeather.value?.weatherResponse?.coord?.let {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.lat, it.lon)))
                }
            }
        }
        viewModel.favourites.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                supportMapFragment.getMapAsync { googleMap ->
                    markers.forEach { it?.remove() }
                    it.forEach {
                        markers.add(googleMap.addMarker {
                            position(
                                LatLng(
                                    it.latitude,
                                    it.longitude
                                )
                            ).title(it.name)
                        })
                    }
                }
            }
        }
    }

    private fun observeViewModel() = with(viewModel) {
        favourites.observe(viewLifecycleOwner) { favouriteAdapter.submitList(it) }
    }

    private fun addFavouriteMarker(favourite: Favourite) {

    }

    private fun removeFavouriteMarker(marker: Marker) {

    }
}