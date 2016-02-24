package co.herxun.neiooplayground;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import co.herxun.neioo.Neioo;
import co.herxun.neioo.exception.NeiooException;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by chiao on 15/7/23.
 */
public class NPApplication extends Application{
    public Neioo neioo;
    @Override
    public void onCreate(){
        super.onCreate();
        try {
            neioo = Neioo.setUp(this,getString(R.string.neioo_appkey));
        } catch (NeiooException e) {
            throw new RuntimeException(e);
        }

        Fresco.initialize(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/DroidSansFallback.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
