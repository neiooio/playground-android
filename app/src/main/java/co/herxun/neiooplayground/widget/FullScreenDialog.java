package co.herxun.neiooplayground.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class FullScreenDialog extends Dialog {
	private RelativeLayout main;

	public FullScreenDialog(Context ct) {
		super(ct);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
	//	enableBackPressed(false);
		
		WindowManager.LayoutParams paramsF = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_DIM_BEHIND,
				PixelFormat.TRANSLUCENT);
		main = new RelativeLayout(ct);
		main.setBackgroundColor(Color.TRANSPARENT);
		main.setLayoutParams(paramsF);

		setContentView(main);
		
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().setDimAmount(0.8f);
		setCanceledOnTouchOutside(true);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(lp);
	}

	public void setAnimation(int resId){
		getWindow().getAttributes().windowAnimations = resId;
	}

	public  void addView(View v){
		main.addView(v);
	}
	@Override
	public void show(){
		super.show();
	}
	
	@Override
	public void dismiss(){
		super.dismiss();
	}
}
