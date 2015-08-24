package co.herxun.neiooplayground.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by chiao on 15/7/23.
 */
public class DesignUtils {
    public static float dp2px(float dp, Context context){
        float px = dp * getDensity(context);
        return px;
    }

    public static float px2dp(float px, Context context){
        float dp = px / getDensity(context);
        return dp;
    }

    public static float getDensity(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

    public static float getScreenWidth(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static float getScreenHeight(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }
}
