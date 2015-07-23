package co.herxun.neiooplayground.neioo;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import co.herxun.neioo.callback.NeiooCallback;
import co.herxun.neioo.model.NeiooBeacon;
import co.herxun.neioo.model.NeiooCampaign;
import co.herxun.neioo.model.NeiooSpace;

/**
 * Created by chiao on 15/7/23.
 */
public class NeiooObserver implements Observer,NeiooCallback {
    @Override
    public void update(Observable observable, Object _data) {
        int event = -1;
        if(_data instanceof Map){
            Map data = ((Map) _data);
            if(data.containsKey(Constant.EVENT)){
                event = (int) data.get(Constant.EVENT);
            }

            switch (event){
                case Constant.ON_CAMPAIGN_TRIGGERED:
                    if(data.containsKey(Constant.BEACON) && data.containsKey(Constant.CAMPAIGN)){
                        onCampaignTriggered((NeiooBeacon)data.get(Constant.BEACON), (NeiooCampaign)data.get(Constant.CAMPAIGN));
                    }
                    break;
                case Constant.IN_SHAKE_RANGE:
                    if(data.containsKey(Constant.CAMPAIGN)){
                        inShakeRange((NeiooCampaign)data.get(Constant.CAMPAIGN));
                    }
                    break;
                case Constant.OUT_SHAKE_RANGE:
                    if(data.containsKey(Constant.CAMPAIGN)){
                        outShakeRange((NeiooCampaign) data.get(Constant.CAMPAIGN));
                    }
                    break;
                case Constant.ON_ENTER_SPACE:
                    if(data.containsKey(Constant.SPACE)){
                        onEnterSpace((NeiooSpace) data.get(Constant.SPACE));
                    }
                    break;
                case Constant.ON_EXIT_SPACE:
                    if(data.containsKey(Constant.SPACE)){
                        onExitSpace((NeiooSpace)data.get(Constant.SPACE));
                    }
                    break;
            }
        }
    }

    @Override
    public void onCampaignTriggered(NeiooBeacon neiooBeacon, NeiooCampaign neiooCampaign) {}

    @Override
    public void inShakeRange(NeiooCampaign neiooCampaign) {}

    @Override
    public void outShakeRange(NeiooCampaign neiooCampaign) {}

    @Override
    public void onEnterSpace(NeiooSpace neiooSpace) {}

    @Override
    public void onExitSpace(NeiooSpace neiooSpace) {}
}
