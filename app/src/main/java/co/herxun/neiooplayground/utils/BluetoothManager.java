package co.herxun.neiooplayground.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import co.herxun.neiooplayground.activity.BaseActivity;

/**
 * Created by chiao on 15/7/28.
 */
public class BluetoothManager{
    private BTBroadcastReceiver mBTBroadcastReceiver;
    private BluetoothAdapter mBluetoothAdapter;

    public BluetoothManager(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void startMonitor(Context context, final BluetoothStateChangedCallback lsr){
        stopMonitor(context);
        mBTBroadcastReceiver = new BTBroadcastReceiver(lsr);
        context.registerReceiver(mBTBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    public void stopMonitor(Context context){
        try {
            context.unregisterReceiver(mBTBroadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void requestBluetooth(BaseActivity act){
        if (mBluetoothAdapter!=null && !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            act.startActivityForResult(enableBtIntent, 0);
        }
    }

    public boolean isBluetoothEnabled(){
        return mBluetoothAdapter!=null && mBluetoothAdapter.isEnabled();
    }

    public interface BluetoothStateListener{
        void onStateOff();
        void onStateOn();
        void onTurningOn();
        void onTurningOff();
    }
    private class BTBroadcastReceiver extends BroadcastReceiver{
        private BluetoothStateChangedCallback mBluetoothStateListener;
        public BTBroadcastReceiver(BluetoothStateChangedCallback lsr){
            mBluetoothStateListener = lsr;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if(mBluetoothStateListener!=null){
                            mBluetoothStateListener.onStateOff();
                        }
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if(mBluetoothStateListener!=null){
                            mBluetoothStateListener.onTurningOff();
                        }
                        break;
                    case BluetoothAdapter.STATE_ON:
                        if(mBluetoothStateListener!=null){
                            mBluetoothStateListener.onStateOn();
                        }

                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        if(mBluetoothStateListener!=null){
                            mBluetoothStateListener.onTurningOn();
                        }
                        break;
                }
            }
        }
    }
}
