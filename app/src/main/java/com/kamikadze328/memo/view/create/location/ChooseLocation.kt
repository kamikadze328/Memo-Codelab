package com.kamikadze328.memo.view.create.location

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kamikadze328.memo.databinding.ActivityChooseLocationBinding
import com.kamikadze328.memo.model.MemoLocation
import kotlinx.coroutines.launch
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker


class ChooseLocation : AppCompatActivity() {
    private lateinit var binding: ActivityChooseLocationBinding
    private lateinit var model: ChooseLocationViewModel
    private var userMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        model = ViewModelProvider(this)[ChooseLocationViewModel::class.java]
        model.initArgs(ChooseLocationContract.getArgs(intent))

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.data.collect { uiState ->
                    updateUI(uiState)
                }
            }
        }
        initMap()
        initListeners()
    }

    private fun initListeners() {
        binding.contentChooseLocation.save.setOnClickListener {
            finishWithResult()
        }

        onBackPressedDispatcher.addCallback {
            finishWithResult()
        }
    }

    private fun finishWithResult() {
        setResult(
            RESULT_OK,
            ChooseLocationContract.createResult(ChooseLocationResult(model.data.value))
        )
        finish()
    }

    private fun initMap() {
        with(binding.contentChooseLocation.map) {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            val location = model.data.value
            controller.setCenter(GeoPoint(location.latitude, location.longitude))
            val mapEventsReceiver = object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    if (p != null) {
                        model.updateLocation(p.latitude, p.longitude)
                    }
                    return true
                }

                override fun longPressHelper(p: GeoPoint?): Boolean = false
            }
            val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
            overlays.add(mapEventsOverlay)
        }
    }

    private fun updateUI(location: MemoLocation) {
        with(binding.contentChooseLocation) {
            val geoPoint = GeoPoint(location.latitude, location.longitude)

            userMarker?.let { map.overlays.remove(it) }

            userMarker = Marker(map)
                .apply {
                    position = geoPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                .also {
                    map.overlays.add(it)
                }

            map.controller.animateTo(geoPoint)
            coordinats.text = "${location.latitude}, ${location.longitude}"
        }
    }

    override fun onResume() {
        super.onResume()
        binding.contentChooseLocation.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.contentChooseLocation.map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.contentChooseLocation.map.onDetach()
    }
}