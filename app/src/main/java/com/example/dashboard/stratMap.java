package com.example.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class stratMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationlistener;
    String bestprovider;
    double tlat;
    double tlong;
    double HomeLat;
    double HomeLnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strat_map);
        //Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Location Listener
        locationlistener = new myLocationListener();
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this,"Open GPS",Toast.LENGTH_LONG).show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
            }
        }
        //It returns the name of the provider that best meets the given criteria. Only providers
        //that are permitted to be accessed by the calling activity will be returned. If several
        //providers meet the criteria, the one with the best accuracy is returned.
        //Criteria is the criteria that need to be matched
        bestprovider = locationManager.getBestProvider(getCriteria(), true);
        //Update the current activity periodically
        locationManager.requestLocationUpdates(bestprovider, 1000, 1, locationlistener);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }
    @Override
    protected void onPause(){
        super.onPause();
        //Remove the listenser when the activity is not in use
        locationManager.removeUpdates(locationlistener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setUpMapIfNeeded();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        //bestprovider=locationManager.getBestProvider(getCriteria(),true);
        locationManager.requestLocationUpdates(bestprovider, 0, 0, locationlistener);
    }

    //Create the mylocationlistener class, which implements LocationListener interface.
    class myLocationListener implements LocationListener {
        //Get the value of longitude and latitude and transfer it to updateMap() method
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                tlat = location.getLatitude();
                tlong = location.getLongitude();
                updateMap();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private void updateMap() {
        LatLng latLng = new LatLng(tlat, tlong);
        mMap.addMarker(new MarkerOptions().position(latLng).title("My location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
    }
    private void setUpMapIfNeeded() {
        if(mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync( this);
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //Get the message from the intent
        Intent intent = getIntent();
        //Extract the message delivered by Mainactivity
        String message = intent.getStringExtra(TakemehomeMain.EXTRA_MESSAGE);
        //Initialize address list
        List<Address> addressList = null;
        //Instantiate the Geocoder service
        Geocoder geocoder = new Geocoder(this);
        try{
            //Get the corresponding geographic location information according to the address entered by MainActivity.
            //Parameter 1: Address; Parameter 2: Number of returned addresses
            addressList = geocoder.getFromLocationName(message, 1);
        } catch (IOException e) {
            Toast.makeText(this, "Errors", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        //Because we set to return only one address before, only the first address needs to be obtained
        Address address = addressList.get(0);
        //Transfer the address to latitude and longitude.
        HomeLat = address.getLatitude();
        HomeLnt = address.getLongitude();
        // Add a marker in my home and move the camera
        LatLng LatLngInput = new LatLng(address.getLatitude(),address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(LatLngInput).title("My Home"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngInput,12));
        //Enable the use of location button
        mMap.setMyLocationEnabled(true);
        //Set the Google map type
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //Set the necessary options
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
    }
}
