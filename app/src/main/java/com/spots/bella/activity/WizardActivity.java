package com.spots.bella.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.spots.bella.PreferenceManager;
import com.spots.bella.R;
import com.spots.bella.activity.main_activity.MainActivity;
import com.spots.bella.adapters.WizaredSliderAdapter;
import com.spots.bella.di.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WizardActivity extends BaseActivity {
    private static final String TAG = "WizardActivity";
    @BindView(R.id.slider_view_pager)
    ViewPager mPager;
    private int[] layouts = {R.layout.frist_slide, R.layout.second_slide, R.layout.third_slide};
    private WizaredSliderAdapter mWizaredSliderAdapter;

    @BindView(R.id.dots_layout)
    LinearLayout dots_layout;
    private ImageView[] dots;
    @BindView(R.id.next_btn)
    Button next_btn;
    @BindView(R.id.skip_btn)
    Button skip_btn;

    private boolean isWizard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAppOpenedBefore();
        setContentView(R.layout.activity_wizard);
        ButterKnife.bind(this);
        initWizardViews();
    }

    private void checkAppOpenedBefore() {
        Log.d(TAG, "checkAppOpenedBefore: " + string);

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
//            mPager.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });


            mWizaredSliderAdapter = new WizaredSliderAdapter(layouts, this);
            mPager.setAdapter(mWizaredSliderAdapter);

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


    @OnClick(R.id.next_btn)
    public void next() {
        load_next_slide_click();
    }

    @OnClick(R.id.skip_btn)
    public void skip() {
        loadLoginRegister();
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
