package com.example.dashboard;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class System_Service extends Service implements SensorEventListener {
    private int mInterval = 300;//the internal time
    private long LastTime;
    private SensorManager sensorManager;
    private Vibrator vibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service is Created");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //Vibration service
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_LONG).show();
        if (sensorManager != null) { // register Listener
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Listener in TYPE_ACCELEROMETER
     */
    public void onSensorChanged(SensorEvent event) {
        // when values of sensor change
        long NowTime = System.currentTimeMillis();//Test the running time of the program
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

        if ((Math.abs(x) > 22 || Math.abs(y) > 22 || Math.abs(z) > 18)) {
            vibrator.vibrate(100);
            Toast.makeText(getApplicationContext(), "timekeeper", Toast.LENGTH_LONG).show();
            Intent activityIntent = new Intent(System_Service.this, WarningAlarm.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //start activity
            startActivity(activityIntent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

