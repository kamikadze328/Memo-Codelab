package com.kamikadze328.memo.feature.choose.location.compose

import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kamikadze328.memo.core.ui.MemoAppBar
import com.kamikadze328.memo.core.ui.theme.AppTheme
import com.kamikadze328.memo.domain.model.MemoLocation
import com.kamikadze328.memo.feature.choose.location.ChooseLocationArgs
import com.kamikadze328.memo.feature.choose.location.ChooseLocationResult
import com.kamikadze328.memo.feature.choose.location.ChooseLocationUiState
import com.kamikadze328.memo.feature.choose.location.ChooseLocationViewModel
import com.kamikadze328.memo.feature.choose.location.R
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker


@Composable
internal fun ChooseLocationUi(
    viewModel: ChooseLocationViewModel = viewModel(),
    args: ChooseLocationArgs,
    onBack: (ChooseLocationResult) -> Unit,
) {
    val uiState by viewModel.data.collectAsStateWithLifecycle()

    BackHandler(
        enabled = true,
        onBack = {
            onBack(ChooseLocationResult(location = uiState.initialLocation))
        }
    )

    ChooseLocationUi(
        uiState = uiState,
        onPick = viewModel::updateLocation,
        onSave = {
            onBack(ChooseLocationResult(location = uiState.location))
        },
    )

    LaunchedEffect(args) {
        viewModel.initArgs(args)
    }
}

@Composable
private fun ChooseLocationUi(
    uiState: ChooseLocationUiState,
    onPick: (MemoLocation) -> Unit,
    onSave: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            MemoAppBar(title = stringResource(id = R.string.choose_location_title))
        },
        content = { paddingValues ->
            Content(
                uiState = uiState,
                modifier = Modifier.padding(paddingValues),
                onPick = onPick,
                onSave = onSave,
            )
        }
    )
}

@Composable
private fun Content(
    uiState: ChooseLocationUiState,
    onPick: (location: MemoLocation) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Map(
            uiState = uiState,
            onPick = onPick,
            modifier = Modifier.fillMaxSize()
        )
        MapInfoAndActions(
            uiState = uiState,
            onSave = onSave,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun Map(
    uiState: ChooseLocationUiState,
    onPick: (location: MemoLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isInPreview = LocalInspectionMode.current
    if (isInPreview) {
        MapPreview(modifier = modifier)
        return
    }

    val location = uiState.location
    var mapView: MapView? by remember { mutableStateOf(null) }
    var marker by remember { mutableStateOf<Marker?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(location.latitude, location.longitude))
            }.also { mapView = it }
        },
        update = { view ->
            mapView = view
        }
    )

    LaunchedEffect(uiState.canChoose, mapView, onPick) {
        val view = mapView ?: return@LaunchedEffect
        view.overlays.removeAll { it is MapEventsOverlay }

        if (uiState.canChoose) {
            val receiver = object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    p ?: return false
                    onPick(MemoLocation(p.latitude, p.longitude))
                    return true
                }

                override fun longPressHelper(p: GeoPoint?) = false
            }
            view.overlays.add(MapEventsOverlay(receiver))
        }
    }
    LaunchedEffect(location, mapView) {
        val p = GeoPoint(location.latitude, location.longitude)
        mapView?.let { view ->
            marker?.let { view.overlays.remove(it) }
            marker = Marker(view).apply {
                position = p
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                isDraggable = false
            }.also { view.overlays.add(it) }
            view.controller.animateTo(p)
            view.invalidate()
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView?.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView?.onPause()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            mapView?.onPause()
            mapView?.onDetach()
        }
    }
}

@Composable
private fun MapPreview(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(color = Color.Gray),
    )
}

@Composable
private fun MapInfoAndActions(
    uiState: ChooseLocationUiState,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.primary,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(12.dp)
        ) {
            MapInfo(
                modifier = Modifier.fillMaxWidth(),
                location = uiState.location
            )

            MapActions(
                modifier = Modifier.fillMaxWidth(),
                onSave = onSave,
                canChooseLocation = uiState.canChoose
            )
        }
    }
}

@Composable
private fun MapInfo(
    location: MemoLocation,
    modifier: Modifier = Modifier
) {
    Text(
        text = "${location.latitude}, ${location.longitude}",
        color = MaterialTheme.colors.onPrimary,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(bottom = 8.dp)
    )
}

@Composable
private fun MapActions(
    onSave: () -> Unit,
    canChooseLocation: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onSave,
        enabled = canChooseLocation,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary
        ),
        modifier = modifier,
    ) {
        Text(text = stringResource(id = R.string.save_coordinates))
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ChooseLocationUi(
            uiState = ChooseLocationUiState(
                location = MemoLocation(41.6413, 41.6359),
                canChoose = true,
                initialLocation = MemoLocation(41.6413, 41.6359)
            ),
            onPick = { },
            onSave = { },
        )
    }
}