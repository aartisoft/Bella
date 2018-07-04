package com.spots.bella.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.spots.bella.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ehab Salah on 6/19/2018.
 */

public class WizaredSliderAdapter extends PagerAdapter {
    private static final String TAG = "WizaredSliderAdapter";
    int[] layouts;
    LayoutInflater layoutInflater;
    Context context;
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

    @BindView(R.id.frist_wizard_slide_iv)
    ImageView iv_1;
    ImageView iv_2, iv_3;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(layouts[position], container, false);

        ButterKnife.bind(this, view);
//        slide1 = view.findViewById(R.id.first_slide_card_container);
//        iv_1 = view.findViewById(R.id.frist_wizard_slide_iv);
        iv_2 = view.findViewById(R.id.second_wizard_slide_iv);
        iv_3 = view.findViewById(R.id.third_wizard_slide_iv);

        if (iv_1 != null) {
            Glide.with(context).load(R.drawable.wizard_image_1).into(iv_1);
//            iv_1.setImageResource(R.drawable.wizard_image_1);
        }
        if (iv_2 != null) {
//            iv_2.setImageResource(R.drawable.wizard_image_2);
            Glide.with(context).load(R.drawable.wizard_image_2).into(iv_2);

        }
        if (iv_3 != null) {
//            iv_3.setImageResource(R.drawable.wizard_image_3);
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
