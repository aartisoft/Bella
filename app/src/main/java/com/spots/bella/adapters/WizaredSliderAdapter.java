package com.spots.bella.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.spots.bella.R;
import com.spots.bella.constants.Common;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ehab Salah on 6/19/2018.
 */

public class WizaredSliderAdapter extends PagerAdapter {
    private static final String TAG = "WizaredSliderAdapter";
    private int[] layouts;
    private LayoutInflater layoutInflater;
    private Context context;
    private View slide1;

    public WizaredSliderAdapter(int[] layouts, Context context) {
        this.layouts = layouts;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(layouts[position], container, false);

        ImageView iv_1 = view.findViewById(R.id.frist_wizard_slide_iv);
        ImageView iv_2 = view.findViewById(R.id.second_wizard_slide_iv);
        ImageView iv_3 = view.findViewById(R.id.third_wizard_slide_iv);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View first_card = view.findViewById(R.id.first_slide_card_container);
            View second_card = view.findViewById(R.id.second_slide_card_container);
            View third_card = view.findViewById(R.id.third_slide_card_container);

            if (first_card != null) {
                RelativeLayout.MarginLayoutParams lp = (RelativeLayout.MarginLayoutParams) first_card.getLayoutParams();
                lp.setMargins(Common.convertDpToPixel(40, context), Common.convertDpToPixel(60, context), Common.convertDpToPixel(40, context), Common.convertDpToPixel(100, context));
                first_card.requestLayout();

                RelativeLayout.MarginLayoutParams params = (RelativeLayout.MarginLayoutParams) iv_1.getLayoutParams();
                params.setMargins(0, 0, 0, Common.convertDpToPixel(context.getResources().getInteger(R.integer.wizard_image_margin_buttom), context));
                params.setMarginEnd(0);
                params.setMarginStart(0);
                iv_1.requestLayout();

            }
            if (second_card != null) {
                RelativeLayout.MarginLayoutParams lp = (RelativeLayout.MarginLayoutParams) second_card.getLayoutParams();
                lp.setMargins(Common.convertDpToPixel(40, context), Common.convertDpToPixel(60, context), Common.convertDpToPixel(40, context), Common.convertDpToPixel(100, context));
                second_card.requestLayout();

                RelativeLayout.MarginLayoutParams params = (RelativeLayout.MarginLayoutParams) iv_2.getLayoutParams();
                params.setMargins(0, 0, 0, Common.convertDpToPixel(context.getResources().getInteger(R.integer.wizard_image_margin_buttom), context));
                params.setMarginEnd(0);
                params.setMarginStart(0);
                iv_2.requestLayout();

            }
            if (third_card != null) {
                RelativeLayout.MarginLayoutParams lp = (RelativeLayout.MarginLayoutParams) third_card.getLayoutParams();
                lp.setMargins(Common.convertDpToPixel(40, context), Common.convertDpToPixel(60, context), Common.convertDpToPixel(40, context), Common.convertDpToPixel(100, context));
                third_card.requestLayout();

                RelativeLayout.MarginLayoutParams params = (RelativeLayout.MarginLayoutParams) iv_3.getLayoutParams();
                params.setMargins(0, 0, 0, Common.convertDpToPixel(context.getResources().getInteger(R.integer.wizard_image_margin_buttom), context));
                params.setMarginEnd(0);
                params.setMarginStart(0);
                iv_3.requestLayout();

            }
        }


        if (iv_1 != null) {
            Glide.with(context).load(R.drawable.wizard_image_1).into(iv_1);
        }
        if (iv_2 != null) {
            Glide.with(context).load(R.drawable.wizard_image_2).into(iv_2);

        }
        if (iv_3 != null) {
            Glide.with(context).load(R.drawable.wizard_image_3).into(iv_3);

        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
