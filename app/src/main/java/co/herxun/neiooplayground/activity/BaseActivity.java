package co.herxun.neiooplayground.activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by chiao on 15/7/23.
 */
public class BaseActivity extends ActionBarActivity {
    private boolean isInForeground = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onPause(){
        super.onPause();
        isInForeground = false;
    }

    @Override
    public void onResume(){
        super.onResume();
        isInForeground = true;
    }


    public boolean isInForeground(){
        return isInForeground;
    }
}
