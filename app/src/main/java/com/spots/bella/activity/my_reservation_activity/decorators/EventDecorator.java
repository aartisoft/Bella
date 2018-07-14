package com.spots.bella.activity.my_reservation_activity.decorators;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.spots.bella.R;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {
    private final Drawable drawable;

    private int color;
    private HashSet<CalendarDay> dates;

    public EventDecorator(/*int color,*/ Context context, Collection<CalendarDay> dates) {
        drawable = context.getResources().getDrawable(R.drawable.unit2);
//        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
//        view.addSpan(new DotSpan(5, color));
    }
}
