package co.herxun.neiooplayground.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import co.herxun.neiooplayground.R;
import co.herxun.neiooplayground.utils.DesignUtils;


public class RadarView extends FrameLayout {
	private ImageView imgIcon,imgSignal;
	private View radar1,radar2;
	private ValueAnimator scaleEngine1,scaleEngine2,rotatEengine;

	private boolean needRotate = true;

	public RadarView(Context ct) {
		super(ct);
		init();
	}
	public RadarView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}

	public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init(){
		radar1 = new View(getContext());
		radar1.setAlpha(0);
		addView(radar1, new FrameLayout.LayoutParams(-1, -1, Gravity.CENTER));
		radar2 = new View(getContext());
		radar2.setAlpha(0);
		addView(radar2, new FrameLayout.LayoutParams(-1, -1, Gravity.CENTER));
		setColor(0xff999999);

		imgSignal = new ImageView(getContext());
		imgSignal.setImageResource(R.drawable.signal);
		addView(imgSignal, new FrameLayout.LayoutParams(-1, -1, Gravity.CENTER));

		imgIcon = new ImageView(getContext());
		ShapeDrawable oval = new ShapeDrawable(new OvalShape());
		oval.getPaint().setColor(0xffaaaaaa);
		imgIcon.setImageDrawable(oval);
		addView(imgIcon, new FrameLayout.LayoutParams((int) DesignUtils.dp2px(64, getContext()), (int) DesignUtils.dp2px(64, getContext()), Gravity.CENTER));

		scaleEngine1 = ValueAnimator.ofFloat(0, 1);
		scaleEngine2 = ValueAnimator.ofFloat(0, 1);
		rotatEengine = ValueAnimator.ofFloat(0, 360);
	}
	
	
	
	public void setIcon(int resId){
		imgIcon.setImageResource(resId);
	}

	public void setColor(int color){
		ShapeDrawable oval = new ShapeDrawable(new OvalShape());
		oval.getPaint().setColor(color);
		radar1.setBackground(oval);
		radar2.setBackground(oval);
	}
	
	public void disableRotateAnimation(){
		needRotate = false;
		imgSignal.setVisibility(View.GONE);
	}
	
	public void startAnimation(){
		if(needRotate){
			startRotate(0);
		}
		startScale(radar1,0,scaleEngine1);
		startScale(radar2,2000,scaleEngine2);
	}
	
	public void startRotate(int delay){
		if(rotatEengine.isRunning()){
			return;
		}
		rotatEengine.setDuration(4000);
		rotatEengine.setStartDelay(delay);
		rotatEengine.setInterpolator(new LinearInterpolator());
		rotatEengine.setRepeatCount(ValueAnimator.INFINITE);
		rotatEengine.addUpdateListener(new AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				imgSignal.setRotation((Float) animation.getAnimatedValue());
			}
		});
		rotatEengine.start();
	}
	
	public void startScale(final View v,int delay,ValueAnimator engine){
		if(engine.isRunning()){
			return;
		}
		engine.setDuration(3000);
		engine.setStartDelay(delay);
		engine.setInterpolator(new DecelerateInterpolator());
		engine.setRepeatCount(ValueAnimator.INFINITE);
		engine.addUpdateListener(new AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				v.setScaleX((Float) animation.getAnimatedValue());
				v.setScaleY((Float) animation.getAnimatedValue());
				v.setAlpha(1-(Float) animation.getAnimatedValue());
			}
		});
		engine.start();
	}
	
	public void stopScale(){
		scaleEngine1.end();
		scaleEngine2.end();
	}
	
}