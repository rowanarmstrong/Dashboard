package com.example.dashboard;

/* This file get the location of users and share it with guardian by sending message*/

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import static com.example.dashboard.ReminderMain.context;
import static com.example.dashboard.Setting.number_to_warning_alarm;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;

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
    private String phonenumber;
    private Button Call;
    private Button Share;
    private String number_text="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {  //get access from dashboard
        return inflater.inflate(R.layout.activity_emergency__information, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE); //get positioning service
        smsManager = SmsManager.getDefault();   //get massage manager
        locationlistener = new mylocationlistener();    //location monitor
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //check if the user open the GPS service
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);   //get location through GPS
        bestprovider = locationManager.getBestProvider(getcriteria(), true);    //Get the best service
        locationManager.requestLocationUpdates(bestprovider, 1000, 5, locationlistener);  //update location every second

        Intent  intent = getActivity().getIntent();
        number_text =  intent.getStringExtra(number_to_warning_alarm);
        if(TextUtils.isEmpty(number_text)){  //Determines if it is null
            phonenumber = "+44 7421271993";  ////default phone number
        }else{
            phonenumber = number_text;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textLat = (TextView) getView().findViewById(R.id.textlat); //text view for latitude
        textLong = (TextView) getView().findViewById(R.id.textlong);  //text view for longitude
        Call = (Button) getView().findViewById(R.id.Call);  //emergency call button
        Share = (Button) getView().findViewById(R.id.Location); //share location button
        Call.setOnClickListener(this); //monitor if the button is clicked
        Share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == Call) {
            Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phonenumber, null));
            startActivity(intent1);

//            Intent intent = new Intent(Intent.ACTION_CALL); //start a new intent that making a phone call
//            Uri data = Uri.parse("tel:" + phonenumber); // set the phone number
//            intent.setData(data);
//            startActivity(intent);
        }
        if (v == Share) {
            String message = getResources().getString(R.string.location); //get the format of massage from string
            message = String.format(message, Latitude, Longitude); //put the value of Latitude and Longitude to the message
            smsManager.sendTextMessage(phonenumber, null, message, null, null); //send the message
        }
    }

    private Criteria getcriteria() {    //Parameter setting for location location service
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
                double tlat = location.getLatitude();   //get latitude and set it as double
                double tlong = location.getLongitude();  //get longitude and set it as double
                textLat.setText(Double.toString(tlat));     //show the latitude on the layout
                textLong.setText(Double.toString(tlong));   //show the longitude on the layout
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
        locationManager.removeUpdates(locationlistener);    //stop positioning service
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
        locationManager.requestLocationUpdates(bestprovider, 0, 0, locationlistener);   //recover updating location
    }
}
