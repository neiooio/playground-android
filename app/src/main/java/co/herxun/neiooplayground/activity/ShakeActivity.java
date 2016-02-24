package co.herxun.neiooplayground.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.herxun.neioo.model.NeiooAction;
import co.herxun.neioo.model.NeiooCampaign;
import co.herxun.neioo.model.NeiooSpace;
import co.herxun.neiooplayground.R;
import co.herxun.neiooplayground.neioo.NeiooActionPerformer;
import co.herxun.neiooplayground.utils.ShakeDetector;
import co.herxun.neiooplayground.widget.RadarView;
import co.herxun.neiooplayground.widget.TipDialog;

/**
 * Created by chiao on 15/7/27.
 */
public class ShakeActivity extends BaseActivity implements ShakeDetector.OnShakeListener{
    private RadarView mRadarView;
    private Map<String,TipDialog> campaignShowingTip;
    private MediaPlayer soundShake, soundPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        showNotificationCampaignIfNeeded();
    }

    private void initView(){
        setContentView(R.layout.activity_radar);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textTitle = (TextView) findViewById(R.id.toolbar_title);
        textTitle.setText(R.string.sub_title_shake);

        mRadarView = (RadarView) findViewById(R.id.view_radar);
        setRadarInRange(false);

    }

    private void initData(){
        ShakeDetector.getInstance(this).setOnShakeListener(this);
        ShakeDetector.getInstance(this).startDetection();
        campaignShowingTip = new HashMap<>();
        soundShake = MediaPlayer.create(this, R.raw.shake);
        soundPop = MediaPlayer.create(this, R.raw.pop);
        NeiooActionPerformer.getInstance().setMultipleImagesRule(NeiooActionPerformer.IMAGE_RULE_RANDOM);

        enableNeioo(new NeiooEnabledCallback() {
            @Override
            public void enabled() {
                mRadarView.startAnimation();
            }
        });
    }

    private void setRadarInRange(boolean in){
        if(in){
            mRadarView.setIcon(R.drawable.phone_yellow);
            mRadarView.setColor(getResources().getColor(R.color.c15));
        }else{
            mRadarView.setIcon(R.drawable.phone_grey);
            mRadarView.setColor(getResources().getColor(R.color.c10));
        }
    }

    private void showTip(NeiooCampaign campaign){
        if(campaignShowingTip.containsKey(campaign.getId())){
            return;
        }
        String customFields = campaign.getCustomField();
        try {
            JSONObject tipContent = new JSONObject(customFields.split("\\|")[1]);
            String imgUrl = tipContent.getString("image_url");
            String title = tipContent.getString("title");
            String description = tipContent.getString("description");
            TipDialog mTipDialog = new TipDialog(this, imgUrl, title, description);
            campaignShowingTip.put(campaign.getId(), mTipDialog);
            mTipDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissTip(NeiooCampaign campaign){
        TipDialog tip = campaignShowingTip.get(campaign.getId());
        if(tip!=null){
            tip.dismiss();
            campaignShowingTip.remove(campaign.getId());
        }
    }

    @Override
    public void onBackPressed(){
        NeiooActionPerformer.getInstance().setMultipleImagesRule(NeiooActionPerformer.IMAGE_RULE_FIRST);
        ShakeDetector.getInstance(this).removeOnShakeListener(this);
        ShakeDetector.getInstance(this).stopDetection();
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

    @Override
    public void onShake() {
        if(campaignShowingTip.size()==0){
            return;
        }

        if(soundShake.isPlaying()){
            soundShake.seekTo(0);
        }
        soundShake.start();

        for(NeiooCampaign campaign : getNeioo().getShakeCampaigns()){
            for(NeiooAction action : campaign.getActions()){
                if(soundPop.isPlaying()){
                    soundPop.seekTo(0);
                }
                soundPop.start();
                NeiooActionPerformer.getInstance().perform(this, action, false);
            }
        }
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

    @Override
    public void inShakeRange(final NeiooCampaign neiooCampaign) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("inShakeRange", neiooCampaign.getId());
                showTip(neiooCampaign);
            }
        });
    }

    @Override
    public void outShakeRange(final NeiooCampaign neiooCampaign) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("outShakeRange", neiooCampaign.getId());
                dismissTip(neiooCampaign);
            }
        });
    }
}