package co.herxun.neiooplayground.neioo;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import co.herxun.neioo.Neioo;
import co.herxun.neioo.callback.NeiooCallback;
import co.herxun.neioo.exception.NeiooException;
import co.herxun.neioo.model.NeiooBeacon;
import co.herxun.neioo.model.NeiooCampaign;
import co.herxun.neioo.model.NeiooSpace;

/**
 * Created by chiao on 15/7/23.
 */
public class NeiooController extends Observable {
    private static NeiooController instance;
    private NeiooController(){}
    public static NeiooController getInstance(){
        if(instance == null){
            instance = new NeiooController();
        }
        return instance;
    }

    private Neioo neioo;
    private NeiooCallback mNeiooCallback = new NeiooCallback(){
        @Override
        public void onCampaignTriggered(NeiooBeacon neiooBeacon, NeiooCampaign neiooCampaign) {
            Map<String,Object> data = new HashMap<>();
            data.put(Constant.EVENT,Constant.ON_CAMPAIGN_TRIGGERED);
            data.put(Constant.BEACON,neiooBeacon);
            data.put(Constant.CAMPAIGN,neiooCampaign);
            setChanged();
            notifyObservers(data);
        }

        @Override
        public void inShakeRange(NeiooCampaign neiooCampaign) {
            Map<String,Object> data = new HashMap<>();
            data.put(Constant.EVENT,Constant.IN_SHAKE_RANGE);
            data.put(Constant.CAMPAIGN,neiooCampaign);
            setChanged();
            notifyObservers(data);
        }

        @Override
        public void outShakeRange(NeiooCampaign neiooCampaign) {
            Map<String,Object> data = new HashMap<>();
            data.put(Constant.EVENT,Constant.OUT_SHAKE_RANGE);
            data.put(Constant.CAMPAIGN,neiooCampaign);
            setChanged();
            notifyObservers(data);
        }

        @Override
        public void onEnterSpace(NeiooSpace neiooSpace) {
            Map<String,Object> data = new HashMap<>();
            data.put(Constant.EVENT,Constant.ON_ENTER_SPACE);
            data.put(Constant.SPACE,neiooSpace);
            setChanged();
            notifyObservers(data);
        }

        @Override
        public void onExitSpace(NeiooSpace neiooSpace) {
            Map<String,Object> data = new HashMap<>();
            data.put(Constant.EVENT,Constant.ON_EXIT_SPACE);
            data.put(Constant.SPACE,neiooSpace);
            setChanged();
            notifyObservers(data);
        }
    };

    public void setUp(Context context, String appkey){
        try {
            neioo = Neioo.setUp(context, appkey, mNeiooCallback);
        } catch (NeiooException e) {
            e.printStackTrace();
        }
    }

    public void enable(){
        try {
            neioo.enable();
        }catch(NullPointerException e){
            throw new RuntimeException("Neioo hasn't been initialized");
        }
    }

    public void disable(){
        try {
            neioo.disable();
        }catch(NullPointerException e){
            throw new RuntimeException("Neioo hasn't been initialized");
        }
    }
}
