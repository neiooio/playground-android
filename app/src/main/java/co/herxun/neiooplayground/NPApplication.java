package co.herxun.neiooplayground;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import co.herxun.neiooplayground.neioo.NeiooController;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by chiao on 15/7/23.
 */
public class NPApplication extends Application{

    @Override
    public void onCreate(){
        NeiooController.getInstance().setUp(this,getString(R.string.neioo_appkey));

        Fresco.initialize(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/DroidSansFallback.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
