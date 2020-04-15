package com.example.dashboard;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StepMain extends Fragment implements SensorEventListener, StepCounter {


    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "m/s";
    private int numSteps;
    private String speedstring;
    private long TotalTimeBetweenSteps;
    private TextView TvSteps;
    private TextView TvGaitSpeed;
    private Button BtnStart;
    private Button BtnStop;

    StepService mService;
    boolean mBound = false;
    boolean ServiceCheck = false;
    private StepService mSensorService;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step_counter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        // Date and Time Stuff
        TvSteps = getView().findViewById(R.id.tv_steps);
        TvGaitSpeed = getView().findViewById(R.id.tv_gait_speed);
        BtnStart = getView().findViewById(R.id.btn_start);
        BtnStop = getView().findViewById(R.id.btn_stop);

        sensorManager.registerListener(StepMain.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

        ServiceCheck = isMyServiceRunning(StepService.class);
        // In case app has been running in the background

        getActivity().startService(new Intent(getActivity(), StepService.class));


        // Restore Movement Pattern
        SharedPreferences settings = getActivity().getSharedPreferences("yeo", 0);
        numSteps = settings.getInt("silentMode", numSteps);
        String sint = String.valueOf(numSteps);
        TvSteps.setText(sint);

        SharedPreferences settings3 = getActivity().getSharedPreferences("yeo3", 0);
        speedstring = settings3.getString("silentMode", speedstring);
        TvGaitSpeed.setText(speedstring);

        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                TotalTimeBetweenSteps = 0;
                SharedPreferences settings2 = getActivity().getSharedPreferences("yeo2", 0);
                SharedPreferences.Editor editor2 = settings2.edit();
                editor2.putLong("silentMode", TotalTimeBetweenSteps);


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

                //Show the new values
                TvSteps.setText(sint);
                TvGaitSpeed.setText(speedstring);

            }
        });
        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getActivity().stopService(new Intent(getActivity(), StepService.class));
            }
        });
    }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(StepMain.this);
        getActivity().stopService(new Intent(getActivity(), StepService.class));
        Log.i("MAINACT", "onDestroy!");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) this.getActivity().getSystemService(Activity.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

    }

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

    @Override
    public void step(long timeNs) {

        // Restore Movement Pattern
        SharedPreferences settings = getActivity().getSharedPreferences("yeo", 0);
        numSteps = settings.getInt("silentMode", numSteps);
        String sint = String.valueOf(numSteps);
        TvSteps.setText(sint);

        SharedPreferences settings3 = getActivity().getSharedPreferences("yeo3", 0);
        speedstring = settings3.getString("silentMode", speedstring);
        TvGaitSpeed.setText(speedstring);

    }
}

