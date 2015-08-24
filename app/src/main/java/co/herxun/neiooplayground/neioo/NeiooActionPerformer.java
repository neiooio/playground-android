package co.herxun.neiooplayground.neioo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import co.herxun.neioo.model.NeiooAction;
import co.herxun.neiooplayground.R;
import co.herxun.neiooplayground.activity.BaseActivity;
import co.herxun.neiooplayground.utils.DesignUtils;
import co.herxun.neiooplayground.widget.FullScreenDialog;
import co.herxun.neiooplayground.widget.RippleView;

/**
 * Created by chiao on 15/7/24.
 */
public class NeiooActionPerformer {
    private static NeiooActionPerformer instance;
    private NeiooActionPerformer(){}
    public static NeiooActionPerformer getInstance(){
        if(instance == null){
            instance = new NeiooActionPerformer();
        }
        return instance;
    }

    public final static String ACTION_SHOW_IMAGE = "show_image";
    public final static String ACTION_SHOW_VIDEO = "show_video";
    public final static String ACTION_SHOW_WEBVIEW = "show_webview";
    public final static String ACTION_LOCAL_PUSH = "local_push";

    public final static String ALIGN_TOP = "top";
    public final static String ALIGN_MIDDLE = "middle";
    public final static String ALIGN_BOTTOM = "bottom";
    public final static String ALIGN_LEFT = "left";
    public final static String ALIGN_CENTER = "center";
    public final static String ALIGN_RIGHT = "right";

    public final static int IMAGE_RULE_FIRST = 0;
    public final static int IMAGE_RULE_RANDOM = 1;
    private int mMultipleImageRule = IMAGE_RULE_FIRST;

    private MediaPlayer mMediaPlayer;
    private Vibrator mVibrator;

    public void perform(BaseActivity activity, NeiooAction action, boolean playSound){
        if(mMediaPlayer==null){
            mMediaPlayer = MediaPlayer.create(activity, R.raw.ui_lbatlow);
        }
        if(mVibrator==null){
            mVibrator = ((Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE));
        }

        if(playSound){
            mMediaPlayer.start();
        }
        mVibrator.vibrate(300);

        if(action.getType().equals(ACTION_SHOW_IMAGE)){
            showImageDialog(activity, action);
        }else if(action.getType().equals(ACTION_SHOW_VIDEO)){
            showVideoDialog(activity,action);
        }else if(action.getType().equals(ACTION_SHOW_WEBVIEW)){
            showWebDialog(activity,action);
        }else if(action.getType().equals(ACTION_LOCAL_PUSH)){
            showLocalPush(activity,action);
        }
    }

    public void perform(BaseActivity activity, NeiooAction action){
        perform(activity, action, true);
    }

    public void setMultipleImagesRule(int rule){
        mMultipleImageRule = rule;
    }


    private void showLocalPush(BaseActivity activity, NeiooAction action){
        JSONObject detail = action.getDetail();
        try {
            String push_text = detail.getString("push_text");
            new MaterialDialog.Builder(activity)
                    .typeface("DroidSansFallback.ttf", "DroidSansFallback.ttf")
                    .title(push_text)
                    .positiveText(R.string.ok)
                    .positiveColorRes(R.color.c1)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();

            if(!activity.isInForeground()){
                int notifyID = 1;
                int requestCode = notifyID;
                Intent intent = activity.getIntent();
                int flags = PendingIntent.FLAG_CANCEL_CURRENT;
                PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), requestCode, intent, flags);
                NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification.Builder(activity.getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(activity.getString(R.string.app_name))
                        .setContentText(push_text).setContentIntent(pendingIntent)
                        .build();
                notificationManager.notify(notifyID, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showWebDialog(Context context, NeiooAction action) {
        JSONObject detail = action.getDetail();
        try {
            String web_url = detail.getString("url");
            final FullScreenDialog dialog = new FullScreenDialog(context);
            dialog.setAnimation(R.style.DialogPushInAnimation);

            RelativeLayout main = new RelativeLayout(context);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(-1, -1);
            rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
            main.setLayoutParams(rlp);

            WebView view = new WebView(context);
            view.loadUrl(web_url);
            main.addView(view, new RelativeLayout.LayoutParams(-1, -1));

            ImageView imgClose = new ImageView(context);
            imgClose.setImageResource(R.drawable.bu_close);
            RippleView rippleView = RippleView.wrap(context,imgClose,context.getResources().getColor(R.color.c6),26);
            rippleView.setRippleRoundedCorners((int) DesignUtils.dp2px(56, context));
            rippleView.setLayoutParams(new RelativeLayout.LayoutParams((int) DesignUtils.dp2px(56, context), (int) DesignUtils.dp2px(56, context)));
            main.addView(rippleView);
            rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.addView(main);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showVideoDialog(Context context, NeiooAction action){
        JSONObject detail = action.getDetail();
        try {
            String video_url = detail.getString("url");
            final FullScreenDialog dialog = new FullScreenDialog(context);
            dialog.setAnimation(R.style.DialogScaleAnimation);
            final VideoView view = new VideoView(context);
            view.setZOrderOnTop(true);
            view.setVideoURI(Uri.parse(video_url));
            view.setMediaController(new MediaController(context));
            view.requestFocus();
            view.start();
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(-1,-2);
            rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
            view.setLayoutParams(rlp);
            dialog.addView(view);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    view.stopPlayback();
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void showImageDialog(Context context, NeiooAction action){
        float screenW = DesignUtils.getScreenWidth(context);
        float screenH = DesignUtils.getScreenHeight(context);
        JSONObject detail = action.getDetail();
        try {
            String image_url = null;
            if(detail.get("url") instanceof String){
                image_url = detail.getString("url");
            }else if (detail.get("url") instanceof JSONArray){
                JSONArray urls = detail.getJSONArray("url");
                if(mMultipleImageRule == IMAGE_RULE_FIRST){
                    image_url = urls.getString(0);
                }else if(mMultipleImageRule == IMAGE_RULE_RANDOM){
                    image_url = urls.getString((int)(Math.random()* 3));
                }
            }

            String h_align = detail.getString("align");
            String v_align = detail.getString("v_align");
            double width = Double.parseDouble(detail.getString("width"));
            double height =  Double.parseDouble(detail.getString("height"));

            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams((int)(screenW*width),(int)(screenH*height));
            if(h_align.equals(ALIGN_LEFT)){
                rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }else if(h_align.equals(ALIGN_RIGHT)){
                rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }else{
                rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            }
            if(v_align.equals(ALIGN_TOP)){
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }else if(v_align.equals(ALIGN_BOTTOM)){
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }else{
                rlp.addRule(RelativeLayout.CENTER_VERTICAL);
            }

            final FullScreenDialog dialog = new FullScreenDialog(context);
            dialog.setAnimation(R.style.DialogScaleAnimation);
            SimpleDraweeView view = new SimpleDraweeView(context);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(rlp);
//            view.setHierarchy(new GenericDraweeHierarchyBuilder(context.getResources())
//                    .setProgressBarImage(new ProgressBarDrawable())
//                    .build());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(image_url))
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(view.getController())
                    .build();
            view.setController(controller);
            dialog.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
