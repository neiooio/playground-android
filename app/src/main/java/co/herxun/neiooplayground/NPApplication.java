package co.herxun.neiooplayground;

import android.app.Application;

import co.herxun.neiooplayground.neioo.NeiooController;

/**
 * Created by chiao on 15/7/23.
 */
public class NPApplication extends Application{

    @Override
    public void onCreate(){
        NeiooController.getInstance().setUp(this,getString(R.string.neioo_appkey));
    }
}
