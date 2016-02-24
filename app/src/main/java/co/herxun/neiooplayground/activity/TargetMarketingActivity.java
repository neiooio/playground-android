package co.herxun.neiooplayground.activity;

import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import co.herxun.neioo.model.NeiooAction;
import co.herxun.neioo.model.NeiooBeacon;
import co.herxun.neioo.model.NeiooCampaign;
import co.herxun.neioo.model.NeiooSpace;
import co.herxun.neiooplayground.R;
import co.herxun.neiooplayground.neioo.NeiooActionPerformer;
import co.herxun.neiooplayground.utils.DesignUtils;
import co.herxun.neiooplayground.widget.RadarView;

/**
 * Created by chiao on 15/7/27.
 */
public class TargetMarketingActivity extends BaseActivity{
    private Dialog criteriaDialog;
    private RadarView mRadarView;
    private TextView textName;
    private View btnAddCriteriaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        showNotificationCampaignIfNeeded();
    }

    private void initView(){
        setContentView(R.layout.activity_radar);

        RelativeLayout root = (RelativeLayout) findViewById(R.id.view_root);
        btnAddCriteriaData = genCriteriaButton();
        root.addView(btnAddCriteriaData);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textTitle = (TextView) findViewById(R.id.toolbar_title);
        textTitle.setText(R.string.sub_title_target_marketing);

        mRadarView = (RadarView) findViewById(R.id.view_radar);
        setRadarInRange(false);

        textName = (TextView) findViewById(R.id.text_name);

        criteriaDialog = new MaterialDialog.Builder(this)
                .typeface("DroidSansFallback.ttf", "DroidSansFallback.ttf")
                .title(R.string.enter_your_name)
                .content(R.string.name)
                .input(0, 0, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog materialDialog, CharSequence input) {
                        if(input.toString().trim().length()>0){
                            setNameCriteria(input.toString());
                        }
                    }
                })
                .positiveText(R.string.add)
                .positiveColorRes(R.color.c14)
                .negativeText(R.string.cancel)
                .negativeColorRes(R.color.c11)
                .widgetColorRes(R.color.c14)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onAny(MaterialDialog dialog) {
                        criteriaDialog.dismiss();
                    }
                })
                .show();
    }

    private View genCriteriaButton(){
        float corner = (int) DesignUtils.dp2px(24, this);

        FrameLayout frame = new FrameLayout(this);

        TextView btnAddCriteria = new TextView(this);
        btnAddCriteria.setTextColor(getResources().getColor(R.color.c12));
        btnAddCriteria.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        btnAddCriteria.setText(R.string.add_your_name);
        btnAddCriteria.setGravity(Gravity.CENTER);
        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(getResources().getColor(R.color.c8));
        gd1.setCornerRadii(new float[]{corner, corner, corner, corner, corner, corner, corner, corner});
        btnAddCriteria.setBackground(gd1);

        final View btnTouch = new View(this);
        btnTouch.setAlpha(0);
        GradientDrawable gd2 = new GradientDrawable();
        gd2.setColor(getResources().getColor(R.color.c9));
        gd2.setCornerRadii(new float[]{corner, corner, corner, corner, corner, corner, corner, corner});
        btnTouch.setBackground(gd2);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(-1, (int) DesignUtils.dp2px(48,this));
        rlp.leftMargin = (int) DesignUtils.dp2px(24,this);
        rlp.rightMargin = (int) DesignUtils.dp2px(24, this);
        rlp.bottomMargin = (int) DesignUtils.dp2px(56, this);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        frame.addView(btnAddCriteria);
        frame.addView(btnTouch);
        frame.setLayoutParams(rlp);

        btnTouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnTouch.setAlpha(0.1f);
                        break;
                    case MotionEvent.ACTION_UP:
                        btnTouch.setAlpha(0);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        btnTouch.setAlpha(0);
                        break;
                }
                return false;
            }
        });
        btnTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criteriaDialog.show();
            }
        });

        return frame;
    }

    private void setRadarInRange(boolean in){
        if(in){
            mRadarView.setIcon(R.drawable.phone_blue);
            mRadarView.setColor(getResources().getColor(R.color.c14));
        }else{
            mRadarView.setIcon(R.drawable.phone_grey);
            mRadarView.setColor(getResources().getColor(R.color.c10));
        }
    }

    private void setNameCriteria(String name){
        getNeioo().addCriteriaData("name", name);
        textName.setText(name);
        btnAddCriteriaData.setVisibility(View.GONE);
        enableNeioo(new NeiooEnabledCallback() {
            @Override
            public void enabled() {
                mRadarView.startAnimation();
            }
        });
    }

    @Override
    public void onBackPressed(){
        getNeioo().removeCriteriaData("name");
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
    public void onCampaignTriggered(NeiooBeacon neiooBeacon, final NeiooCampaign neiooCampaign) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(neiooCampaign.getCriterias().size()!=0){
                    for(NeiooAction action : neiooCampaign.getActions()){
                        NeiooActionPerformer.getInstance().perform(TargetMarketingActivity.this,action);
                    }
                }
            }
        });
    }
}
