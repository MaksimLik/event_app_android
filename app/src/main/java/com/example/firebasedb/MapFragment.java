package com.example.firebasedb;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapFragment extends Fragment {
    private FloatingActionButton findLocationFAB;

    private FloatingActionButton addReviewFAB;

    MarkerOptions markerOptions = new MarkerOptions();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull com.google.android.gms.maps.model.LatLng latLng) {
                        markerOptions.position(latLng);
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        googleMap.addMarker(markerOptions);
                    }
                });

                findLocationFAB = view.findViewById(R.id.idLocation);
                findLocationFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // if permissions are granted
                        if (!(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                        }
                        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "Looking for location...", Toast.LENGTH_SHORT).show();
                            googleMap.setMyLocationEnabled(true);
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);


                            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                            Location location = googleMap.getMyLocation();

                            if (location == null) {
                                // get location from network provider
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location == null) {
                                    return;
                                }
                            }
                            // Clear the map
                            googleMap.clear();
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new com.google.android.gms.maps.model.LatLng(location.getLatitude(), location.getLongitude()), 15));

                            // Set marker
                            markerOptions = new MarkerOptions();
                            markerOptions.position(new com.google.android.gms.maps.model.LatLng(location.getLatitude(), location.getLongitude()));
                            markerOptions.title(location.getLatitude() + " : " + location.getLongitude());
                            googleMap.addMarker(markerOptions);
                        }
                    }
                });


                addReviewFAB = view.findViewById(R.id.idaddReviewFAB);

                addReviewFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (markerOptions == null) {
                            Toast.makeText(getActivity(), "Please select a location", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Extract markerOptions from map
                        LatLng latLng = markerOptions.getPosition();
                        if (latLng == null) {
                            Toast.makeText(getActivity(), "Please select a location", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                        intent.putExtra("lat", latLng.latitude);
                        intent.putExtra("lng", latLng.longitude);
                        startActivity(intent);
                    }
                });
            }
        });
        return view;
    }
}