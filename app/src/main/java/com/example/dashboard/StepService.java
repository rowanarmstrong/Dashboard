package com.example.dashboard;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

public class StepService extends JobIntentService implements SensorEventListener, StepCounter {


        private StepDetector simpleStepDetector;
        private SensorManager sensorManager;
        private Sensor accel;

        private int numSteps;
        private long TimeBetweenSteps = 0;
        private long LastStepTime = 0;
        private long TotalTimeBetweenSteps = 0;
        private double mGaitDistance = 0.7; //This is measure in metres - it is the distance moved over one step
        private double speeeddddddd = 0;
        public String speedstring;
        private double AverageTimeBetweenSteps = 0;
        private int start = 0;


            @Override
            public void onCreate() {
                super.onCreate();
                System.out.println("Step Service is Created");



            }



            @Override
            public int onStartCommand(Intent intent, int flags, int startId) {
                super.onStartCommand(intent, flags, startId);

                // Get an instance of the SensorManager
                sensorManager = (SensorManager) this.getSystemService(Activity.SENSOR_SERVICE);
                accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                simpleStepDetector = new StepDetector();
                simpleStepDetector.registerListener(this);
                sensorManager.registerListener(StepService.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

                // Restore preferences
                SharedPreferences settings = getSharedPreferences("yeo", 0);
                numSteps = settings.getInt("silentMode", numSteps);

                // Restore preferences
                SharedPreferences settings2 = getSharedPreferences("yeo2", 0);
                TotalTimeBetweenSteps = settings2.getLong("silentMode", TotalTimeBetweenSteps);

                // Restore preferences
                SharedPreferences settings3 = getSharedPreferences("yeo3", 0);
                speedstring = settings3.getString("silentMode", speedstring);

                Notification notification = new NotificationCompat.Builder(this, "StepChannel")
                        .setSmallIcon(R.drawable.ic_movement)
                        .setContentTitle("Step Tracker")
                        .setContentText("Your Steps are being tracked").build();

                startForeground(1, notification);

                return START_STICKY;
            }

            @Override
            public void onDestroy() {
                super.onDestroy();

                SharedPreferences settings = getSharedPreferences("yeo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("silentMode", numSteps);

                SharedPreferences settings2 = getSharedPreferences("yeo2", 0);
                SharedPreferences.Editor editor2 = settings2.edit();
                editor2.putLong("silentMode", TotalTimeBetweenSteps);

                SharedPreferences settings3 = getSharedPreferences("yeo3", 0);
                SharedPreferences.Editor editor3 = settings3.edit();
                editor3.putString("silentMode", speedstring);



                // Commit the edits!
                editor.apply();
                editor2.apply();
                editor3.apply();

                Log.i("EXIT", "ondestroy!");
                Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
                sendBroadcast(broadcastIntent);
                //save the value

            }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

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

                // Restore preferences
                SharedPreferences settings = getSharedPreferences("yeo", 0);
                numSteps = settings.getInt("silentMode", numSteps);

                // Restore preferences
                SharedPreferences settings2 = getSharedPreferences("yeo2", 0);
                TotalTimeBetweenSteps = settings2.getLong("silentMode", TotalTimeBetweenSteps);

                if(start == 1) {
                    TimeBetweenSteps = ((timeNs - LastStepTime)) / 10000000;
                }

                if(TimeBetweenSteps < 300) {
                    TotalTimeBetweenSteps = TotalTimeBetweenSteps + TimeBetweenSteps;
                }



                SharedPreferences.Editor editor2 = settings2.edit();
                editor2.putLong("silentMode", TotalTimeBetweenSteps);


                numSteps++;

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("silentMode", numSteps);


                AverageTimeBetweenSteps = TotalTimeBetweenSteps / numSteps;
                AverageTimeBetweenSteps = AverageTimeBetweenSteps / 100;
                speeeddddddd =  (mGaitDistance / AverageTimeBetweenSteps);
                speedstring = String.format("%.2f" , speeeddddddd);

                SharedPreferences settings3 = getSharedPreferences("yeo3", 0);
                SharedPreferences.Editor editor3 = settings3.edit();
                editor3.putString("silentMode", speedstring);

                // Commit the edits!
                editor.apply();
                editor2.apply();
                editor3.apply();

                LastStepTime = timeNs;
                start = 1;

                Log.i("STEP", "Picked up a step");
            }


    }

