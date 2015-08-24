package co.herxun.neiooplayground.widget;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.herxun.neiooplayground.R;
import co.herxun.neiooplayground.utils.DesignUtils;

/**
 * Created by chiao on 15/7/23.
 */
public class NaviBubble extends RelativeLayout{
    private final double mScale = 1.0;

    private TextView textTitle;
    private ImageView imgIcon;
    private View contentView;
    private FrameLayout containerView;

    private View.OnClickListener mOnClickListener;

    public NaviBubble(Context context) {
        super(context);
        init();
    }

    public NaviBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NaviBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > (int)((mScale * height) + 0.5)) {
            width = (int)((mScale * height) + 0.5);
        } else {
            height = (int)((width / mScale) + 0.5);
        }

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );
    }

    private void init(){
        removeAllViews();
        inflate(getContext(), R.layout.view_navi_bubble, this);

        textTitle = (TextView) findViewById(R.id.text_title);
        imgIcon = (ImageView) findViewById(R.id.img_icon);
        contentView = findViewById(R.id.view_content);
        containerView = (FrameLayout) findViewById(R.id.view_container);

        ShapeDrawable oval = new ShapeDrawable(new OvalShape());
        oval.getPaint().setColor(getResources().getColor(R.color.c10));
        setBackgroundDrawable(oval);
    }

    public void setTitle(String title){
        textTitle.setText(title);
    }

    public void setIcon(int resId){
        imgIcon.setImageResource(resId);
    }

    public void setColor(int color){
        ShapeDrawable oval = new ShapeDrawable(new OvalShape());
        oval.getPaint().setColor(color);
        setBackgroundDrawable(oval);
    }

    public void setRipple(int color,int alpha){
        containerView.removeView(contentView);
        RippleView ripple = RippleView.wrap(getContext(), contentView, color, alpha);
        ripple.setRippleRoundedCorners((int) DesignUtils.getScreenWidth(getContext()));
        containerView.addView(ripple);
        ripple.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    mOnClickListener.onClick(v);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(View.OnClickListener lsr){
        mOnClickListener = lsr;
    }
}
