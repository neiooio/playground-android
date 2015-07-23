package co.herxun.neiooplayground.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import co.herxun.neioo.model.NeiooBeacon;
import co.herxun.neioo.model.NeiooCampaign;
import co.herxun.neioo.model.NeiooSpace;
import co.herxun.neiooplayground.R;
import co.herxun.neiooplayground.neioo.NeiooController;
import co.herxun.neiooplayground.neioo.NeiooObserver;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView(){
        setContentView(R.layout.activity_main);
    }

    private void initData(){
        NeiooController.getInstance().enable();
        NeiooController.getInstance().addObserver(new NeiooObserver(){
            @Override
            public void onCampaignTriggered(NeiooBeacon neiooBeacon, NeiooCampaign neiooCampaign) {
                Log.e("onCampaignTriggered",neiooBeacon.getName());
            }

            @Override
            public void inShakeRange(NeiooCampaign neiooCampaign) {
                Log.e("inShakeRange",neiooCampaign.getId());
            }

            @Override
            public void outShakeRange(NeiooCampaign neiooCampaign) {
                Log.e("outShakeRange",neiooCampaign.getId());
            }

            @Override
            public void onEnterSpace(NeiooSpace neiooSpace) {
                Log.e("onEnterSpace", neiooSpace.getName());
            }

            @Override
            public void onExitSpace(NeiooSpace neiooSpace) {
                Log.e("onExitSpace", neiooSpace.getName());
            }
        });
    }
}
