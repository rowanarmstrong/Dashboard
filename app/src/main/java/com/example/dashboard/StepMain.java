package com.example.dashboard;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StepMain extends Fragment implements SensorEventListener, StepCounter {
    private TextView textView;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = " ";
    private int numSteps;
    private long TimeBetweenSteps = 0;
    private int TimeBetweenStepsInt = 0;
    private long LastStepTime = 0;
    private long TotalTimeBetweenSteps = 0;
    private double AverageTimeBetweenSteps = 0;
    private int start = 0;
    private TextView TvSteps;
    private TextView TvGaitSpeed;
    private Button BtnStart;
    private Button BtnStop;

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


        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                numSteps = 0;
                sensorManager.registerListener(StepMain.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sensorManager.unregisterListener(StepMain.this);

            }
        });

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

        if(start == 1) {
            TimeBetweenSteps = ((timeNs - LastStepTime)) / 10000000;
        }


        if(TimeBetweenSteps < 300) {
            TotalTimeBetweenSteps = TotalTimeBetweenSteps + TimeBetweenSteps;
        }

        numSteps++;


        AverageTimeBetweenSteps = TotalTimeBetweenSteps / numSteps;
        AverageTimeBetweenSteps = AverageTimeBetweenSteps / 100;



        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
        TvGaitSpeed.setText(TEXT_NUM_STEPS + AverageTimeBetweenSteps);

        LastStepTime = timeNs;
        start = 1;



    }
}