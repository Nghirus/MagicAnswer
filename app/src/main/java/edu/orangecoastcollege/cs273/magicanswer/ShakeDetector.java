package edu.orangecoastcollege.cs273.magicanswer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by vnguyen468 on 10/31/2017.
 */

public class ShakeDetector implements SensorEventListener {

    private static final long ELAPSED_TIME = 2000;
    //Accelerometer's data uses float
    private static final float THRESHOLD = 300000000;
    private long previousShake;
    private OnShakeListener mListener;

    public ShakeDetector (OnShakeListener listener)
    {
        mListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        //Ignore all events, except ACCELEROMETERS
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            //neutralize gravity (subtract gravity from each value
            float gForceX = x - SensorManager.GRAVITY_EARTH;
            float gForceY = y - SensorManager.GRAVITY_EARTH;
            float gForceZ = z - SensorManager.GRAVITY_EARTH;

            float netForce = (float) Math.sqrt(Math.pow(gForceX,2) + Math.pow(gForceY,2
                    + Math.pow(gForceZ,2)));
            if (netForce >= THRESHOLD)
            {
                //Get the current time
                long currentTime = System.currentTimeMillis();
                if (currentTime >= previousShake + ELAPSED_TIME)
                {
                    //reset previous shake to the current time.
                    previousShake = currentTime;

                    //Register a shake event (it qualifies)
                    mListener.onShake();
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //do nothing, not being used
    }

    //Define an interface for others to implement whenever a true shakes occur.
    //Interface = contract (method declaration without implementation.
    //Some other class has to implement.

    public interface OnShakeListener
    {
        void onShake();
    }

}
