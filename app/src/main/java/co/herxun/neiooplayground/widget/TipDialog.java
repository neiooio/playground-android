package co.herxun.neiooplayground.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import co.herxun.neiooplayground.R;
import co.herxun.neiooplayground.utils.DesignUtils;

/**
 * Created by chiao on 15/7/27.
 */
public class TipDialog extends FullScreenDialog{

    public TipDialog(Context ct, String imgUrl, String title, String description) {
        super(ct);
        RelativeLayout view = new RelativeLayout(ct);
        View.inflate(ct, R.layout.view_beacon_dialog, view);
        SimpleDraweeView img = (SimpleDraweeView) view.findViewById(R.id.view_beacon_dialog_img);
        TextView textTitle = (TextView) view.findViewById(R.id.view_beacon_dialog_title);
        TextView textContent = (TextView) view.findViewById(R.id.view_beacon_dialog_content);
        RippleView ripple = (RippleView) view.findViewById(R.id.view_beacon_dialog_ripple);
        LinearLayout bg = (LinearLayout) view.findViewById(R.id.view_beacon_dialog_bg);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams((int) DesignUtils.dp2px(288, ct),-2);
        rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
        view.setLayoutParams(rlp);
        addView(view);

        ripple.setRippleOverlay(false);
        ripple.setDefaultRippleAlpha(255);
        ripple.setRippleColor(ct.getResources().getColor(R.color.c7));
        ripple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        img.setImageURI(Uri.parse(imgUrl));
        textTitle.setText(title);
        textContent.setText(description);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(ct.getResources().getColor(R.color.c6));
        float corner = DesignUtils.dp2px(2,ct);
        gd.setCornerRadii(new float[]{0, 0, 0, 0, corner, corner, corner, corner});
        bg.setBackgroundDrawable(gd);

        setCanceledOnTouchOutside(false);
    }
}
