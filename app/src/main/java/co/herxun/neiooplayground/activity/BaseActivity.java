package co.herxun.neiooplayground.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import co.herxun.neioo.Neioo;
import co.herxun.neioo.callback.NeiooCallback;
import co.herxun.neioo.exception.NeiooException;
import co.herxun.neioo.model.NeiooAction;
import co.herxun.neioo.model.NeiooBeacon;
import co.herxun.neioo.model.NeiooCampaign;
import co.herxun.neioo.model.NeiooSpace;
import co.herxun.neiooplayground.NPApplication;
import co.herxun.neiooplayground.neioo.NeiooActionPerformer;
import co.herxun.neiooplayground.utils.BluetoothManager;
import co.herxun.neiooplayground.utils.BluetoothStateChangedCallback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by chiao on 15/7/23.
 */
public class BaseActivity extends ActionBarActivity implements NeiooCallback{
    private Neioo neioo;
    private static BaseActivity lastActivity;
    private static BluetoothManager mBluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        neioo = ((NPApplication)getApplication()).neioo;
        if(mBluetoothManager==null){
            mBluetoothManager = new BluetoothManager();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        lastActivity = this;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected static final BaseActivity getLastResumeActivity(){
        return lastActivity;
    }

    protected final void enableNeioo(final NeiooEnabledCallback cbk){
        if(mBluetoothManager.isBluetoothEnabled()){
            boolean enabled = neioo.enable();
            if(cbk!=null && enabled){
                neioo.setCallback(this);
                cbk.enabled();
            }
        }else{
            mBluetoothManager.startMonitor(this, new BluetoothStateChangedCallback(){
                @Override
                public void onStateOn() {
                    boolean enabled = neioo.enable();
                    if(cbk!=null && enabled){
                        neioo.setCallback(BaseActivity.this);
                        cbk.enabled();
                    }
                }
            });
            mBluetoothManager.requestBluetooth(this);
        }
    }

    protected final void disableNeioo(){
        mBluetoothManager.stopMonitor(this);
        neioo.disable();
        neioo.setCallback(null);
    }

    protected final void showNotificationCampaignIfNeeded(){
        try {
            NeiooCampaign campaign = getNeioo().getNotificationCampaign(getIntent());
            for(NeiooAction action : campaign.getActions()){
                NeiooActionPerformer.getInstance().perform(this,action);
            }
        } catch (NeiooException e) {
            e.printStackTrace();
        }
    }

    protected final Neioo getNeioo(){
        return neioo;
    }


    @Override
    public void onCampaignTriggered(NeiooBeacon neiooBeacon, NeiooCampaign neiooCampaign) {

    }

    @Override
    public void inShakeRange(NeiooCampaign neiooCampaign) {

    }

    @Override
    public void outShakeRange(NeiooCampaign neiooCampaign) {

    }

    @Override
    public void onEnterSpace(NeiooSpace neiooSpace) {

    }

    @Override
    public void onExitSpace(NeiooSpace neiooSpace) {

    }


    public interface NeiooEnabledCallback{
        void enabled();
    }
}
