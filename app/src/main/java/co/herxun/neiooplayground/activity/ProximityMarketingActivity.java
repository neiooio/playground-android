package co.herxun.neiooplayground.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import co.herxun.neioo.model.NeiooAction;
import co.herxun.neioo.model.NeiooBeacon;
import co.herxun.neioo.model.NeiooCampaign;
import co.herxun.neioo.model.NeiooSpace;
import co.herxun.neiooplayground.R;
import co.herxun.neiooplayground.neioo.NeiooActionPerformer;
import co.herxun.neiooplayground.neioo.NeiooController;
import co.herxun.neiooplayground.neioo.NeiooObserver;
import co.herxun.neiooplayground.widget.RadarView;

public class ProximityMarketingActivity extends BaseActivity{
    private RadarView mRadarView;
    private NeiooObserver mNeiooObserver = new NeiooObserver() {
        @Override
        public void onCampaignTriggered(NeiooBeacon neiooBeacon, final NeiooCampaign neiooCampaign) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(NeiooAction action : neiooCampaign.getActions()){
                        Log.e("onCampaignTriggered",action.getName());
                        NeiooActionPerformer.getInstance().perform(ProximityMarketingActivity.this, action);
                    }
                }
            });
        }
        @Override
        public void onEnterSpace(final NeiooSpace neiooSpace) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("onEnterSpace", neiooSpace.getName());
                    setRadarInRange(true);
                }
            });
        }

        @Override
        public void onExitSpace(final NeiooSpace neiooSpace) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("onExitSpace", neiooSpace.getName());
                    setRadarInRange(false);
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView(){
        setContentView(R.layout.activity_radar);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textTitle = (TextView) findViewById(R.id.toolbar_title);
        textTitle.setText(R.string.sub_title_proximity_marketing);

        mRadarView = (RadarView) findViewById(R.id.view_radar);
        setRadarInRange(false);

    }

    private void initData(){
        enableNeioo();
    }

    private void enableNeioo(){
        NeiooController.getInstance().addObserver(mNeiooObserver);
        NeiooController.getInstance().enable(this, new NeiooController.NeiooEnabledCallback() {
            @Override
            public void enabled() {
                mRadarView.startAnimation();
            }
        });
    }

    private void disableNeioo(){
        NeiooController.getInstance().disable(this);
        NeiooController.getInstance().deleteObserver(mNeiooObserver);
    }

    private void setRadarInRange(boolean in){
        if(in){
            mRadarView.setIcon(R.drawable.phone_green);
            mRadarView.setColor(getResources().getColor(R.color.c1));
        }else{
            mRadarView.setIcon(R.drawable.phone_grey);
            mRadarView.setColor(getResources().getColor(R.color.c10));
        }
    }

    @Override
    public void onBackPressed(){
        disableNeioo();
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
