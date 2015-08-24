package co.herxun.neiooplayground.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import co.herxun.neiooplayground.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },2000);
    }

    private void initView(){
        setContentView(R.layout.activity_splash);
    }
}
