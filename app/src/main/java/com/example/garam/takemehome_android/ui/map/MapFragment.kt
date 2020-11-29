package com.example.garam.takemehome_android.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.ui.SharedViewModel
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment : Fragment(),  MapView.POIItemEventListener, MapView.MapViewEventListener {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_map, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val range = sharedViewModel.getRange()
        val mapView = MapView(activity)
        mapView.setMapViewEventListener(this)
        mapView.setPOIItemEventListener(this)

        when {
            requireActivity().let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED ||
                    requireActivity().let {
                        ContextCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    } != PackageManager.PERMISSION_GRANTED -> {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION), 100 )
            }
            else -> {
                val shared = sharedViewModel.getLocation()
                val lm : LocationManager = requireActivity()
                    .getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val latitude = location.latitude
                val longitude = location.longitude
                val currentLocation = MapPoint.mapPointWithGeoCoord(latitude, longitude)
                Log.e("위치정보: ", "$latitude , $longitude")
                val marker = MapPOIItem()
                marker.itemName = "내 위치"
                marker.mapPoint = currentLocation
                for (i in 0 until shared.size) {
                    val mark = MapPOIItem()
                    mark.mapPoint = MapPoint.mapPointWithGeoCoord(
                        shared[i].y.toDouble(), shared[i].x.toDouble() )
                    mark.itemName = "$i 가게"
                    Log.e("$i 가게", "${shared[i].x} , ${shared[i].y}")
                    mapView.addPOIItem(mark)
                }
                mapView.setMapCenterPointAndZoomLevel(currentLocation, 3, true)
                mapView.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithMarkerHeadingWithoutMapMoving
                mapView.setShowCurrentLocationMarker(true)
                mapView.setCurrentLocationRadius(range)
                mapView.setCurrentLocationRadiusStrokeColor(Color.RED)
                mapView.mapType = MapView.MapType.Standard
                //mapView.addPOIItem(marker)

                val mapViewContainer = root.findViewById<ConstraintLayout>(R.id.frameTest)
                mapViewContainer.addView(mapView)
            }
        }
        return root
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewInitialized(p0: MapView?) {
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
    }
}