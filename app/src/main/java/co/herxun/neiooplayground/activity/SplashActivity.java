package co.herxun.neiooplayground.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import co.herxun.neioo.exception.NeiooException;
import co.herxun.neiooplayground.R;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getNeioo().getNotificationCampaign(getIntent());
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            i.putExtras(getIntent().getExtras());
            startActivity(i);
            finish();
        } catch (NeiooException e) {
            e.printStackTrace();
            initView();
        }
    }

    private void initView(){
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }

}
