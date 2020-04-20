package com.example.dashboard;

/*
Welcome to the Step Main Class!!
This class is the User Interface for the Movement Pattern Section of the app.
It is a fragment, and contains the User's current walking information.
It also contains buttons for the user to interact with the movement pattern tracker.

Student Name: Rowan Armstrong
Student ID: s1541585
 */

//Imports to get context
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

//Imports to get sensors up and running
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

//Imports to build our layout
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//Imports for fragments
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

//Our Movement Pattern Fragment
public class StepMain extends Fragment implements SensorEventListener, StepCounter {

    //Declarations: We need to activate the sensors so we know when to update the step values
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "m/s";
    private int numSteps;
    private String speedstring;
    private long TotalTimeBetweenSteps;
    private TextView TvSteps;
    private TextView TvGaitSpeed;

    //Creating the view for our Movement Pattern Fragment. The layout can be observed in the step_counter.xml file.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step_counter, container, false);
    }

    //Once the view is created, we need to populate it with our step counter buttons and values.
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        //Lets tie our .xml elements to this class.
        TvSteps = Objects.requireNonNull(getView()).findViewById(R.id.tv_steps);
        TvGaitSpeed = getView().findViewById(R.id.tv_gait_speed);
        Button btnStart = getView().findViewById(R.id.btn_start);
        Button btnStop = getView().findViewById(R.id.btn_stop);

        //We need to register this listener to tell when we need to update the step values.
        sensorManager.registerListener(StepMain.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

        //Check if the service is running. Switchh here?
        boolean serviceCheck = isMyServiceRunning(StepService.class);

        //Start the step service (We need to put a switch here)
        Objects.requireNonNull(getActivity()).startService(new Intent(getActivity(), StepService.class));


        // Restore Movement Pattern values from our service.
        SharedPreferences settings = getActivity().getSharedPreferences("yeo", 0);
        numSteps = settings.getInt("silentMode", numSteps);
        String sint = String.valueOf(numSteps);
        //Lets show the user their steps.
        TvSteps.setText(sint);

        // Restore Movement Pattern Gait Speed.
        SharedPreferences settings3 = getActivity().getSharedPreferences("yeo3", 0);
        speedstring = settings3.getString("silentMode", speedstring);
        //Show the user their Gait Speed.
        TvGaitSpeed.setText(speedstring);

        //We need to listen to our buttons. arg0 is the reset button.
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //Reset our steps and save the value so that the service can puck up on it.
                TotalTimeBetweenSteps = 0;
                SharedPreferences settings2 = getActivity().getSharedPreferences("yeo2", 0);
                SharedPreferences.Editor editor2 = settings2.edit();
                editor2.putLong("silentMode", TotalTimeBetweenSteps);

                // Reset steps and let service know
                numSteps = 0;
                String sint = String.valueOf(numSteps);
                SharedPreferences settings = getActivity().getSharedPreferences("yeo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("silentMode", numSteps);
                Log.i("STEP", "Reset Values");
                speedstring = "0";

                // Commit the edits!
                editor.apply();
                editor2.apply();

                //Show the new values to the user.
                TvSteps.setText(sint);
                TvGaitSpeed.setText(speedstring);

            }
        });

        //Know we listen to the 'Upload to cloud' button
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            }
        });
    }

    //onCreate lets us get our StepDetector service and our accelerometer listener.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) this.getActivity().getSystemService(Activity.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
    }

    // This is where we check if the service is running. This lets us make sure that the service isnt stopped unnecessarily or started twice by accident.
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    //When the fragment is destroyed, we need to stop listening to the step detector.
    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(StepMain.this);
        Log.i("MAINACT", "onDestroy!");
    }

    //OnPause, invoke super onPause
    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    //A step from the Step Detector means that the values on the screen need to be updated. To do so, we need to access the app's shared preferences.
    @Override
    public void step(long timeNs) {

        // Restore Movement Pattern - number of steps
        SharedPreferences settings = getActivity().getSharedPreferences("yeo", 0);
        numSteps = settings.getInt("silentMode", numSteps);
        String sint = String.valueOf(numSteps);
        TvSteps.setText(sint);

        //Restore movement pattern - Gait Speed
        SharedPreferences settings3 = getActivity().getSharedPreferences("yeo3", 0);
        speedstring = settings3.getString("silentMode", speedstring);
        TvGaitSpeed.setText(speedstring);

    }
}

