package com.example.rgs_chequepickup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        Objects.requireNonNull(supportMapFragment).getMapAsync(this);

        mapInitialize();

        return view;
    }

    private void mapInitialize() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(16);
        locationRequest.setFastestInterval(3000);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        gMap = googleMap;

        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                gMap.setMyLocationEnabled(true);
                                //return;
                                fusedLocationProviderClient.getLastLocation().addOnFailureListener(e -> Toast.makeText(getContext(),"error"+e.getMessage(),Toast.LENGTH_SHORT).show()).addOnSuccessListener(location -> {
                                    if(location != null){
                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                                        //gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 17));
                                    }
                                    else{
                                        Toast.makeText(getContext(), "Current Location Unknown", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getContext(), "Please turn on your location Services", Toast.LENGTH_SHORT).show();
                                askPermission();
                            }
                        }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        Toast.makeText(getContext(), "Permission"
                                +permissionDeniedResponse.getPermissionName()+""+"was denied!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    private void askPermission(){
        ActivityCompat.requestPermissions(requireActivity(),new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(!(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(getContext(),"Required Permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}