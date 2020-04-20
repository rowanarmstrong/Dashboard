package com.example.dashboard;
/* This file provides the service that detect falling by using  accelerate sensor.
* When the falling is triggered, system will start a time counter, counting 30s.
* If users doesn't stop the counter in 30s, a warning message will sent to their guardian*/
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.TextUtils;
import android.widget.Toast;

import static com.example.dashboard.Setting.number_to_warning_alarm;
import static com.example.dashboard.Setting.weight_to_system_service;
import androidx.annotation.Nullable;

public class System_Service extends Service implements SensorEventListener {
    private int mInterval = 300;//the internal time
    private long LastTime;
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private float f,weight;
    private String WeightText;
    NotificationManager manager;
    private String weight_text="";
    private String number_text="";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) { //work on the background
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service is Created");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);   //get sensor from system
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //Vibration service
        manager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_LONG).show(); //show a notification when the fall detection start
        if (sensorManager != null) {    // register Listener
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL); //activate the acceleration sensor
        }
        weight_text = intent.getStringExtra(weight_to_system_service);
        //addNotification();
        return START_STICKY;    //restart the programme on the background
    }

    private void addNotification() {    //show a notification
        Notification.Builder builder;   //build a notification
        String channelId = "notification_simple";
        NotificationChannel channel = new NotificationChannel(channelId, "simple", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
        builder = new Notification.Builder(this, channelId);
        builder.setSmallIcon(R.drawable.fall_icon);
        manager.notify(1, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Listener in TYPE_ACCELEROMETER
     */
    public void onSensorChanged(SensorEvent event) {    // when values of sensor change
        long NowTime = System.currentTimeMillis();  //Test the running time of the program
        long times = NowTime - LastTime;
        if ((times) < mInterval) {
            return;
        } else {
            LastTime = NowTime;
        }

        float[] values = event.values;
        float x = values[0]; // x  right positive
        float y = values[1]; // y  front
        float z = values[2]; // z  up

        f = Float.valueOf(weight_to_system_service); //get the value from setting
//        WeightText=Float.toString(f);   //transform the string to float
        if(TextUtils.isEmpty(WeightText)){  //Determines if it is null
            weight = 60;    //default user's weight
        }else{
            weight = f;     //use the weight that users type in seting
        }
        if ((Math.abs(x) > (weight/10+15) || Math.abs(y) > (weight/10+15) || Math.abs(z) > (weight/10+10))) { //the sensitive of sensors at x,y,z axis
            vibrator.vibrate(100);  //Vibration time
            Toast.makeText(getApplicationContext(), "timekeeper", Toast.LENGTH_LONG).show();  //show a notification that the time counter is on
            Intent activityIntent = new Intent(System_Service.this, WarningAlarm.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //start activity
            activityIntent.putExtra(number_to_warning_alarm, number_text);
            startActivity(activityIntent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

