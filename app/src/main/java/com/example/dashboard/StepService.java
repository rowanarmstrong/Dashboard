package com.example.dashboard;

/*
Welcome to the Step Service!!
The Step Service acts as a bridge between the Step Fragment and the Step Detector Algorithm.
The Step Service works in the background, and allows the user to access their movement pattern on their
android device.

Student Name: Rowan Armstrong
Student ID: s1541585

 */

//Imports for our service
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

//Get our notification imports
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

//Lets start our StepSrvice class
public class StepService extends JobIntentService implements SensorEventListener, StepCounter {
        //Declaratios- we have quite a few variables. Maybe convert these to floats?
        private StepDetector simpleStepDetector;
        private int numSteps;
        private long TimeBetweenSteps = 0;
        private long LastStepTime = 0;
        private long TotalTimeBetweenSteps = 0;
        public String speedstring;
        private int start = 0;

            //onCreate, let the terminal know that the service has been created
            @Override
            public void onCreate() {
                super.onCreate();
                System.out.println("Step Service is Created");
            }

            //onStartCommand we need to get our service set up
            @Override
            public int onStartCommand(Intent intent, int flags, int startId) {
                super.onStartCommand(intent, flags, startId);

                // Get an instance of the SensorManager
                SensorManager sensorManager = (SensorManager) this.getSystemService(Activity.SENSOR_SERVICE);
                Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                simpleStepDetector = new StepDetector();
                simpleStepDetector.registerListener(this);
                sensorManager.registerListener(StepService.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

                // Restore preferences from the last time the service was used
                SharedPreferences settings = getSharedPreferences("yeo", 0);
                numSteps = settings.getInt("silentMode", numSteps);

                // Restore preferences from the last time the service was used
                SharedPreferences settings2 = getSharedPreferences("yeo2", 0);
                TotalTimeBetweenSteps = settings2.getLong("silentMode", TotalTimeBetweenSteps);

                // Restore preferences from the last time the service was used
                SharedPreferences settings3 = getSharedPreferences("yeo3", 0);
                speedstring = settings3.getString("silentMode", speedstring);

                //Its good to let the user know that this service is running in the background. If they arent happy with it, they can go into the app and turn it off.
                Notification notification = new NotificationCompat.Builder(this, "StepChannel")
                        .setSmallIcon(R.drawable.ic_movement)
                        .setContentTitle("Step Tracker")
                        .setContentText("Your Steps are being tracked").build();
                startForeground(1, notification);

                return START_STICKY;
            }

            //Before we destroy the service we need to save the current values, quickly!
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

                //Let the terminal know that our service is dead (RIP)
                Log.i("EXIT", "ondestroy!");
                Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
                sendBroadcast(broadcastIntent);
                //save the value
            }

            //Necessary for a JobService
            @Override
            protected void onHandleWork(@NonNull Intent intent) {
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            //onAccuracy changed, we need to update our step detector algorithm values.
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    simpleStepDetector.updateAccel(
                            event.timestamp, event.values[0], event.values[1], event.values[2]);
                }
            }

            //We have to handle what happens when the algorithm picks up a step
            @Override
            public void step(long timeNs) {

                // Restore preferences
                SharedPreferences settings = getSharedPreferences("yeo", 0);
                numSteps = settings.getInt("silentMode", numSteps);

                // Restore preferences
                SharedPreferences settings2 = getSharedPreferences("yeo2", 0);
                TotalTimeBetweenSteps = settings2.getLong("silentMode", TotalTimeBetweenSteps);

                //Lets convert the time value into hundredth of seconds, so that humans can understand it
                if(start == 1) {
                    TimeBetweenSteps = ((timeNs - LastStepTime)) / 10000000;
                }

                //If 3 seconds has passed, don't include this step in our average gait speed (assume that user has stopped walking)
                if(TimeBetweenSteps < 300) {
                    TotalTimeBetweenSteps = TotalTimeBetweenSteps + TimeBetweenSteps;
                }

                //Store the value for next time
                SharedPreferences.Editor editor2 = settings2.edit();
                editor2.putLong("silentMode", TotalTimeBetweenSteps);
                numSteps++;

                //Store the value for next time
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("silentMode", numSteps);


                //Lets do some Maths! Speed = Distance / Time and we assume that the disatnce for each stride is 0.7 metres. Maybe this could be personalised for each user?
                double averageTimeBetweenSteps = TotalTimeBetweenSteps / numSteps;
                averageTimeBetweenSteps = averageTimeBetweenSteps / 100;
                //This is measure in metres - it is the distance moved over one step
                double mGaitDistance = 0.7;
                double speeeddddddd = (mGaitDistance / averageTimeBetweenSteps);
                speedstring = String.format("%.2f" , speeeddddddd);

                //Store our final value of Gait Speed
                SharedPreferences settings3 = getSharedPreferences("yeo3", 0);
                SharedPreferences.Editor editor3 = settings3.edit();
                editor3.putString("silentMode", speedstring);

                // Commit the edits!
                editor.apply();
                editor2.apply();
                editor3.apply();

                //This is used later
                LastStepTime = timeNs;
                start = 1;

                //Tell the terminal that a step has occured.
                Log.i("STEP", "Picked up a step");
            }
    }

