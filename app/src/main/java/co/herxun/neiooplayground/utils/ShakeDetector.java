package co.herxun.neiooplayground.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;
import android.util.Log;

/**
 * Created by chiao on 15/6/30.
 */
public class ShakeDetector implements SensorEventListener {

    private static final float SHAKE_THRESHOLD_GRAVITY = 2F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 1000;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;

    private boolean isDetecting = false;

    private static ShakeDetector instance;
    public static ShakeDetector getInstance(Context context){
        if(instance==null){
            instance = new ShakeDetector(context);
        }
        return instance;
    }

    private ShakeDetector(Context context){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void startDetection(){
        if(isDetecting){
            return;
        }
        Log.e("ShakeDetector","startDetection");
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        isDetecting = true;
    }

    public void stopDetection(){
        Log.e("ShakeDetector", "stopDetection");
        mSensorManager.unregisterListener(this);
        isDetecting = false;
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public void removeOnShakeListener(OnShakeListener listener) {
        this.mListener = null;
    }

    public boolean isDetecting(){
        return isDetecting;
    }

    public interface OnShakeListener {
        void onShake();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            float gForce = FloatMath.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                mShakeTimestamp = now;

                mListener.onShake();
            }
        }
    }
}
