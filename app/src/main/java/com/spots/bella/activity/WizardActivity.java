package com.spots.bella.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.spots.bella.PreferenceManager;
import com.spots.bella.R;
import com.spots.bella.adapters.WizaredSliderAdapter;

public class WizardActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mPager;
    private int[] layouts = {R.layout.frist_slide, R.layout.second_slide, R.layout.third_slide};
    private WizaredSliderAdapter mWizaredSliderAdapter;
    private LinearLayout dots_layout;
    private ImageView[] dots;
    private Button next_btn, skip_btn;
    private PreferenceManager pM;
    private boolean isWizard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAppOpenedBefore();
        setContentView(R.layout.activity_wizard);
        initWizardViews();
    }

    private void checkAppOpenedBefore() {

        pM = new PreferenceManager(this);
        pM.clearPrefrences();

        if (pM.readSharedPrefrenceBoolean(getString(R.string.wizard_shown_key))) {
            loadLoginRegister();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            isWizard = true;
        }
    }

    private void initWizardViews() {
        if (isWizard) {
            findViewById(R.id.root_wizard_layout).setVisibility(View.VISIBLE);
            mPager = findViewById(R.id.slider_view_pager);
//            mPager.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });
            mWizaredSliderAdapter = new WizaredSliderAdapter(layouts, this);
            mPager.setAdapter(mWizaredSliderAdapter);
            dots_layout = findViewById(R.id.dots_layout);
            next_btn = findViewById(R.id.next_btn);
            skip_btn = findViewById(R.id.skip_btn);
            next_btn.setOnClickListener(this);
            skip_btn.setOnClickListener(this);
            createDots(mPager.getCurrentItem());
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    createDots(position);
                    if (position == layouts.length - 1) {
                        next_btn.setText("Start");
                        skip_btn.setVisibility(View.INVISIBLE);
                    } else {
                        next_btn.setText("Next");
                        skip_btn.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void createDots(int current_position) {
        if (dots_layout != null) {
            dots_layout.removeAllViews();
        }
        dots = new ImageView[layouts.length];

        for (int i = 0; i < layouts.length; i++) {
            dots[i] = new ImageView(this);
            if (i == current_position) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            dots_layout.addView(dots[i], params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                load_next_slide_click();
                break;
            case R.id.skip_btn:
                loadLoginRegister();
                break;
        }
    }

    private void load_next_slide_click() {

        int next_slide = mPager.getCurrentItem() + 1;
        if (next_slide < layouts.length) {
            mPager.setCurrentItem(next_slide);
        } else {
            loadLoginRegister();
        }
    }

    private void loadLoginRegister() {
        pM.writeSharedPrefrenceBoolean(getString(R.string.wizard_shown_key), true);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
