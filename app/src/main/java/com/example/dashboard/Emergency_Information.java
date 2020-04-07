package com.example.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Emergency_Information extends Fragment implements View.OnClickListener{

    TextView textLat;
    TextView textLong;
    LocationManager locationManager;
    LocationListener locationlistener;
    String bestprovider;
    Criteria criteria;
    private SmsManager smsManager;
    private String Latitude;
    private String Longitude;
    private String PhoneNumber = "+44 7730136257";

    private Button Call;
    private Button Share;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_emergency__information, container, false);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        smsManager = SmsManager.getDefault();
        locationlistener = new mylocationlistener();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getActivity(), "Open GPS", Toast.LENGTH_LONG).show();
        }
        if (getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
        bestprovider = locationManager.getBestProvider(getcriteria(), true);
        locationManager.requestLocationUpdates(bestprovider, 1000, 5, locationlistener);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        textLat = (TextView) getView().findViewById(R.id.textlat);
        textLong = (TextView) getView().findViewById(R.id.textlong);



        Call = (Button) getView().findViewById(R.id.Call);
        Share = (Button) getView().findViewById(R.id.Location);
        Call.setOnClickListener(this);
        Share.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == Call) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + PhoneNumber);
            intent.setData(data);
            startActivity(intent);
        }
        if (v == Share) {
            String message = getResources().getString(R.string.location);
            message = String.format(message, Latitude, Longitude);
            smsManager.sendTextMessage(PhoneNumber, null, message, null, null);
        }

    }





    private Criteria getcriteria() {
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    class mylocationlistener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                double tlat = location.getLatitude();
                double tlong = location.getLongitude();
                textLat.setText(Double.toString(tlat));
                textLong.setText(Double.toString(tlong));
                Latitude = Double.toString(tlat);
                Longitude = Double.toString(tlong);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationlistener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(bestprovider, 0, 0, locationlistener);
    }
}
