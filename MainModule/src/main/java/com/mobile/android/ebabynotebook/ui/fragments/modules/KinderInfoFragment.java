package com.mobile.android.ebabynotebook.ui.fragments.modules;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.mobile.android.ebabynotebook.R;
import com.mobile.android.withings.ui.CustomUserDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class KinderInfoFragment extends Fragment {
    private EditText name;
    private EditText geburtsdatum;
    private EditText geburtsgewicht;
    private EditText geburt;
    private EditText ssw;
    private RadioGroup geschlechtRadioGroup;
    private Button buttonSave;

    private int pYear;
    private int pMonth;
    private int pDay;

    private Date geburtsDatum;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_kinder_info, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        restoreFieldsFromSharedPrefs();
    }

    private void initViews(View view){
        name = (EditText) view.findViewById(R.id.name);
        geschlechtRadioGroup = (RadioGroup) view.findViewById(R.id.radiogroup_geschlecht);
        geburtsdatum = (EditText) view.findViewById(R.id.geburtsdatum);
        geburtsdatum.setOnClickListener(listener);
        geburtsgewicht = (EditText) view.findViewById(R.id.geburtsgewicht);
        geburt = (EditText) view.findViewById(R.id.geburt);
        ssw = (EditText) view.findViewById(R.id.ssw);
        buttonSave = (Button) view.findViewById(R.id.btn_save);
        buttonSave.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener(){
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.geburtsdatum:
                    createDatePickerDialog().show();
                    break;
                case R.id.btn_save:
                    saveFieldsToSharedPrefs();
                    break;
            }
        }
    };

    protected Dialog createDatePickerDialog() {
        /** Get the current date */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), myCallBack, pYear, pMonth, pDay);
        return dialog;
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int myYear = year;
            int myMonth = monthOfYear + 1;
            int myDay = dayOfMonth;
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
                geburtsDatum = simpleDateFormat.parse(myDay + "/" + myMonth + "/" + myYear);
                fillDateAndTime(geburtsDatum);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    private void fillDateAndTime(Date date) {
        long currentTimestamp = date.getTime();
        CharSequence dateStr = android.text.format.DateFormat.format("dd.MM.yyyy", date);
        CharSequence timeStr = android.text.format.DateFormat.format("hh:mm", date);
        geburtsdatum.setText(dateStr);
    }

    private void saveFieldsToSharedPrefs(){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString("name", name.getText().toString());
        editor.putInt("geschlecht", getGeschlecht());
        editor.putString("geburtsdatum", geburtsdatum.getText().toString());
        editor.putLong("geburtsdate", geburtsDatum.getTime());
        editor.putString("geburtsgewicht", geburtsgewicht.getText().toString());
        editor.putString("geburt", geburt.getText().toString());
        editor.putString("ssw", ssw.getText().toString());
        editor.apply();
    }

    private void restoreFieldsFromSharedPrefs(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        name.setText(prefs.getString("name", ""));
        selectGeschlecht(prefs.getInt("geschlecht", 0));
        geburtsdatum.setText(prefs.getString("geburtsdatum", ""));
        geburtsgewicht.setText(prefs.getString("geburtsgewicht", ""));
        geburt.setText(prefs.getString("geburt", ""));
        ssw.setText(prefs.getString("ssw", ""));
    }

    private int getGeschlecht() {
        int checkedId = geschlechtRadioGroup.getCheckedRadioButtonId();
        switch (checkedId){
            case R.id.maleCheckBox:
                return 0;
            case R.id.femaleCheckBox:
                return 1;
            default:
                return 0;
        }
    }

    private void selectGeschlecht(int value) {
        switch (value){
            case 0:
                geschlechtRadioGroup.check(R.id.maleCheckBox);
                break;
            case 1:
                geschlechtRadioGroup.check(R.id.femaleCheckBox);
                break;
            default:
                break;
        }
    }
}