package co.herxun.neiooplayground.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import co.herxun.neiooplayground.R;
import co.herxun.neiooplayground.utils.Constant;
import co.herxun.neiooplayground.widget.NaviBubble;


public class MainActivity extends BaseActivity {
    private NaviBubble nbProximity, nbBuy, nbShake, nbTarget;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        nbProximity = (NaviBubble) findViewById(R.id.nb_proximity);
        nbProximity.setTitle(getString(R.string.title_proximity_marketing));
        nbProximity.setIcon(R.drawable.menuicon_proximity);
        nbProximity.setColor(getResources().getColor(R.color.c1));
        nbProximity.setRipple(getResources().getColor(R.color.c5), 26);
        nbProximity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProximityMarketingActivity.class));
                overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
            }
        });

        nbShake = (NaviBubble) findViewById(R.id.nb_shake);
        nbShake.setTitle(getString(R.string.title_shake));
        nbShake.setIcon(R.drawable.menuicon_shake);
        nbShake.setColor(getResources().getColor(R.color.c15));
        nbShake.setRipple(getResources().getColor(R.color.c5), 26);
        nbShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShakeActivity.class));
                overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
            }
        });

        nbTarget = (NaviBubble) findViewById(R.id.nb_target);
        nbTarget.setTitle(getString(R.string.title_target_marketing));
        nbTarget.setIcon(R.drawable.menuicon_target);
        nbTarget.setColor(getResources().getColor(R.color.c14));
        nbTarget.setRipple(getResources().getColor(R.color.c5), 26);
        nbTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TargetMarketingActivity.class));
                overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
            }
        });

        nbBuy = (NaviBubble) findViewById(R.id.nb_buy);
        nbBuy.setTitle(getString(R.string.title_buy_new_beacon));
        nbBuy.setIcon(R.drawable.menuicon_buy);
        nbBuy.setColor(getResources().getColor(R.color.c16));
        nbBuy.setRipple(getResources().getColor(R.color.c5), 26);
        nbBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WebViewActivity.class);
                i.putExtra(Constant.INTENT_EXTRA_KEY_WEB_URL,getString(R.string.neioo_url));
                startActivity(i);
                overridePendingTransition(R.anim.push_up_in, android.R.anim.fade_out);
            }
        });

        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                adjustBubblePosition();
            }
        });
    }

    private void adjustBubblePosition(){
        View row2 = findViewById(R.id.row_2);
        LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) row2.getLayoutParams();
        llp.bottomMargin = -(nbShake.getWidth()/2);
        row2.setLayoutParams(llp);
    }

}
