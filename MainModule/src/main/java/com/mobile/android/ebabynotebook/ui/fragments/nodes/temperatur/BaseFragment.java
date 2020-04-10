package com.mobile.android.ebabynotebook.ui.fragments.nodes.temperatur;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BaseFragment extends Fragment {
    protected TextView datum;
    protected TextView zeit;

    protected int pYear;
    protected int pMonth;
    protected int pDay;
    protected int pHour;
    protected int pMinute;
    protected boolean is24HourView = false;
    protected long currentTimestamp;

    protected Dialog createDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), myDateCallBack, pYear, pMonth, pDay);
        return dialog;
    }

    protected Dialog createTimePickerDialog() {
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), myTimeCallBack, pHour, pMinute, is24HourView);
        return dialog;
    }

    TimePickerDialog.OnTimeSetListener myTimeCallBack = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hour, int minute) {
            pHour =hour;
            pMinute = minute;
            createTimestamp();
        }
    };

    DatePickerDialog.OnDateSetListener myDateCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            pYear = year;
            pMonth = monthOfYear + 1;
            pDay = dayOfMonth;
            createTimestamp();
        }
    };

    private void createTimestamp(){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy, HH:mm");
            Date date = simpleDateFormat.parse(pDay + "/" + pMonth + "/" + pYear + ", " + pHour + ":" + pMinute);
            fillDateAndTime(date);
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void fillDateAndTime(Date date){
        currentTimestamp = date.getTime();
        fillDate(date);
        fillTime(date);
    }

    private void fillDate(Date date) {
        CharSequence dateStr = android.text.format.DateFormat.format("dd.MM.yyyy", date);
        datum.setText(dateStr);
    }

    private void fillTime(Date date) {
        CharSequence timeStr = android.text.format.DateFormat.format("hh:mm", date);
        zeit.setText(timeStr);
    }

    protected void initTimeStamp(){
        final Calendar calendar = Calendar.getInstance();
        pYear = calendar.get(Calendar.YEAR);
        pMonth = calendar.get(Calendar.MONTH);
        pDay = calendar.get(Calendar.DAY_OF_MONTH);
        pHour = calendar.get(Calendar.HOUR_OF_DAY);
        pMinute = calendar.get(Calendar.MINUTE);
        fillDateAndTime(calendar.getTime());
    }
}
