package com.example.pickup.models;

import android.view.View;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;

public class MyCalendar {

    public static HorizontalCalendar makeHorizontalCalendar(View rootView, int viewId) {
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        return new HorizontalCalendar.Builder(rootView, viewId).range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();
    };
}
