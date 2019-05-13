package com.example.fatcalapplication;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment {

    GoogleMap pGoogleMap;
    MapView pMapView;
    private View vMaps;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vMaps = inflater.inflate(R.layout.fragment_maps, container, false);
        pMapView = (MapView)vMaps.findViewById(R.id.fatCalMap);
        pMapView.onCreate(savedInstanceState);

        pMapView.onResume();
try {
    MapsInitializer.initialize(getActivity().getApplicationContext());
}catch(Exception e)
{
    e.printStackTrace();
}
        pMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                pGoogleMap = googleMap;

                pGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                pGoogleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247,-74.044502)).title("Statue Of Liberty").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                CameraPosition Liberty = CameraPosition.builder().target(new LatLng(40.689247,-74.044502)).zoom(16).bearing(0).tilt(45).build();
                pGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));

            }
        });

        return vMaps;
    }

}
