package com.spots.bella.activity.my_reservation_activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.spots.bella.R;
import com.spots.bella.activity.messages_activity.MessagesActivity;
import com.spots.bella.activity.messages_activity.fragments.MainMessagesFragment;
import com.spots.bella.activity.my_reservation_activity.decorators.EventDecorator;
import com.spots.bella.activity.my_reservation_activity.decorators.HighlightWeekendsDecorator;
import com.spots.bella.activity.my_reservation_activity.decorators.MySelectorDecorator;
import com.spots.bella.activity.my_reservation_activity.decorators.OneDayDecorator;
import com.spots.bella.activity.my_reservation_activity.fragments.MainReservationsFragment;
import com.spots.bella.activity.my_reservation_activity.fragments.MainReservationsFragment.OnFragmentMainReservationsInteractionListener;
import com.spots.bella.adapters.MainMessagesRecyclerViewAdapter;
import com.spots.bella.adapters.MyReservationRecyclerViewAdapter;
import com.spots.bella.di.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.spots.bella.constants.Common.getStatusBarHeight;
import static com.spots.bella.constants.Common.setTranslucentStatusBar;
import static com.spots.bella.constants.Common.syncToolbar;

public class MyReservationActivity extends BaseActivity implements OnDateSelectedListener /*implements OnFragmentMainReservationsInteractionListener */ {
    private static final String TAG = "MyReservationActivity";
    @BindView(R.id.reservation_activity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.reservation_toolbar_container)
    LinearLayout toolbar_container;

    @BindView(R.id.calendarView)
    MaterialCalendarView widget;

    @BindView(R.id.iv_my_reservation_bg)
    ImageView iv_my_reservation_bg;

    @BindView(R.id.rv_my_reservation)
    RecyclerView rv_my_reservation;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatusBar(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(context.getString(R.string.app_font_type))
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_my_reservation);
        ButterKnife.bind(this);
        initViews();
//        showMainReservationsFragment();
    }

  /*  private void showMainReservationsFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.my_reservation_activity_container, new MainReservationsFragment(), getResources().getString(R.string.main_reservations_fragment_tag));
        fragmentTransaction.commit();
    }*/

    private void initViews() {
        Glide.with(context).load(R.drawable.my_reservation_bg).into(iv_my_reservation_bg);
        setupToolbar();
        setUpCalendar();
        setupRecyclerview();
    }

    private void setupRecyclerview() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv_my_reservation.setLayoutManager(layoutManager);
        rv_my_reservation.setHasFixedSize(true);
        MyReservationRecyclerViewAdapter mAdapter = new MyReservationRecyclerViewAdapter(context);
        rv_my_reservation.setNestedScrollingEnabled(false);
        rv_my_reservation.setAdapter(mAdapter);
    }

    private void setUpCalendar() {
        widget.setOnDateChangedListener(MyReservationActivity.this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        widget.setTopbarVisible(false);
        widget.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Date date1 = date.getDate();
                Log.d(TAG, "onMonthChanged: " + date.getDate().toString());
            }
        });
        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        widget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        widget.addDecorators(
                new MySelectorDecorator(this),
                /* new HighlightWeekendsDecorator(),*/
                oneDayDecorator
        );

        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
    }

    private void setupToolbar() {
        setupToolbarContainer();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncToolbar(toolbar_title, Gravity.START, R.string.my_reservation);
    }

    private void setupToolbarContainer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                int height = (int) TypedValue.complexToDimension(tv
                        .data, getResources().getDisplayMetrics());
                Log.d(TAG, "initViews: action height = " + height);
                Log.d(TAG, "initViews: status height = " + getStatusBarHeight(context));
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbar_container.getLayoutParams();
                params.height = height + getStatusBarHeight(context);
                toolbar_container.setLayoutParams(params);
            }

        }
    }

  /*  @Override
    public void onFragmentMainReservationFragmentOpened() {
        Log.d(TAG, "onFragmentMainReservationFragmentOpened: ");
    }*/

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
                calendar.add(Calendar.DATE, 5);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            widget.addDecorator(new EventDecorator(context, calendarDays));
        }
    }

}
