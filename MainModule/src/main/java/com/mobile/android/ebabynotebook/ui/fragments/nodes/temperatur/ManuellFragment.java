package com.mobile.android.ebabynotebook.ui.fragments.nodes.temperatur;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mobile.android.database.DataSourceHolder;
import com.mobile.android.database.beans.Temperatur;
import com.mobile.android.ebabynotebook.R;
import com.mobile.android.database.TemperaturDataSource;
import com.mobile.android.ebabynotebook.utils.DialogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManuellFragment extends BaseFragment {
    private EditText temperature;
    private EditText kommentar;
    private Button btn_save;
    private TableLayout table;
    private Spinner spinnerKoerperStelle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_temperature_manuell, container, false);
        initViews(view);
        initSpinnerValues(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        restoreFieldsFromSharedPrefs();
        initTimeStamp();
        reloadTable();
    }

    private void initViews(View view) {
        datum = (TextView) view.findViewById(R.id.datum);
        zeit = (TextView) view.findViewById(R.id.zeit);
        temperature = (EditText) view.findViewById(R.id.temperature);
        kommentar = (EditText) view.findViewById(R.id.kommentar);
        spinnerKoerperStelle = (Spinner) view.findViewById(R.id.spinner_koerperStelle);

        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(listener);
        datum.setOnClickListener(listener);
        zeit.setOnClickListener(listener);
        table = (TableLayout) view.findViewById(R.id.table);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View view) {
            saveFieldsToSharedPrefs();

            switch (view.getId()) {
                case R.id.datum:
                    createDatePickerDialog().show();
                    break;
                case R.id.zeit:
                    createTimePickerDialog().show();
                    break;
                case R.id.btn_save:
                    if(temperature.getText().length() > 0)
                        saveDataToDatabase();
                    //((IconTabActivity) getActivity()).replaceFragment(IconTabActivity.TEMPERATURE_FRAGMENT, IconTabActivity.TEMPERATURE_FRAGMENT);
                    reloadTable();
                    break;
            }
        }
    };

    private void saveDataToDatabase() {
        try {
            // Put measures in db
            String dateStr = String.valueOf(currentTimestamp / 1000);
            String unit = getString(R.string.set_tempC);
            String temperaturStr = temperature.getText().toString();
            float temperatur = Float.valueOf(temperaturStr);
            String comment = kommentar.getText().toString();

            String accountName = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("name", "");
            String position = spinnerKoerperStelle.getSelectedItem().toString();

            if(temperatur < 30.00 || temperatur > 42.00 || !validateTemperatur(temperaturStr)){
                DialogUtil.showErrorDialog(getActivity(), R.string.temperatur_message);
            }
            else {
                TemperaturDataSource datasource = DataSourceHolder.getInstance(getActivity()).getTemperaturDataSource();
                datasource.open();
                datasource.createTemperatur(accountName, dateStr, unit, temperaturStr, position, comment, "M");
                datasource.close();
            }

        } catch (java.sql.SQLException e) {
            Log.e("", e.getMessage());
        }
    }

    // Save and restore unit into shared prefs
    private void saveFieldsToSharedPrefs(){
    }

    private void restoreFieldsFromSharedPrefs(){
    }

    private void parseTemperaturData() {
        try {
            TemperaturDataSource datasource = DataSourceHolder.getInstance(getActivity()).getTemperaturDataSource();
            datasource.open();

            String accountName = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("name", "");
            List<Temperatur> temperatures = datasource.getAllTemperatur(accountName);
            for (Temperatur temperatur : temperatures) {
                String unit = "°C";
                long timestamp = Long.parseLong(temperatur.getTimestamp()) * 1000;
                String datum = android.text.format.DateFormat.format("dd.MM.yyyy", timestamp).toString();
                String zeit = android.text.format.DateFormat.format("hh:mm", timestamp).toString();
                String type = temperatur.getType();
                String temperature = temperatur.getTemperatur();
                String position = temperatur.getPosition();
                String comment = temperatur.getComment();
                drawTable(getActivity(), type, datum, zeit, temperature, unit, position, comment);
            }

            datasource.close();
        } catch (java.sql.SQLException e) {
            Log.e("", e.getMessage());
        }
    }

    private void reloadTable() {
        // clear table data
        table.removeAllViews();
        // draw table header
        drawTable(getActivity(), getString(R.string.table_title_typ), getString(R.string.table_title_datum), getString(R.string.table_title_zeit), getString(R.string.table_title_temperatur), getString(R.string.table_title_unit), getString(R.string.title_koerperstelle), getString(R.string.table_title_kommentar));
        // fill table with data from database
        parseTemperaturData();
    }

    private void drawTable(Context context, String str1, String str2, String str3, String str4, String str5, String str6, String str7){
        TableRow row = new TableRow(context);
        row.setPadding(0, 0, 0, 0);

        TextView cell1 = createCell(context, str1, 70);
        TextView cell2 = createCell(context, str2, 180);
        TextView cell3 = createCell(context, str3, 180);
        TextView cell4 = createCell(context, str4, 120);
        TextView cell5 = createCell(context, str5, 60);
        TextView cell6 = createCell(context, str6, 150);
        TextView cell7 = createCell(context, str7, 180);

        row.addView(cell1);
        row.addView(cell2);
        row.addView(cell3);
        row.addView(cell4);
        row.addView(cell5);
        row.addView(cell6);
        row.addView(cell7);

        table.addView(row);
    }

    private TextView createCell(Context context, String text, int maxWidth){
        TextView cell = new TextView(context);
        cell.setBackground(context.getDrawable(R.drawable.cell_shape));
        cell.setText(text);
        cell.setPadding(7, 7, 7, 7);
        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cellParams.setMargins(0, 0, 0, 0);
        cell.setLayoutParams(cellParams);
        cell.setGravity(Gravity.CENTER_HORIZONTAL);
        cell.setMaxWidth(maxWidth);
        cell.setLines(1);
        cell.setTextSize(12);
        return cell;
    }

    private void initSpinnerValues(View view){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getKoerperStelleData());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_koerperStelle);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Title");
        spinner.setSelection(0);
    }

    private String[] getKoerperStelleData() {
        String[] data = {getString(R.string.title_stirn), getString(R.string.title_gehoergang), getString(R.string.title_mundhoehle), getString(R.string.title_rektal), getString(R.string.title_achselhoehle)};
        return data;
    }

    private boolean validateTemperatur(String temperatur){
        Pattern pattern;
        Matcher matcher;
        String PATTERN = "[0-9][0-9].[0-9][0-9]";
        pattern = Pattern.compile(PATTERN);
        matcher = pattern.matcher(temperatur);
        return matcher.matches();
    }
}
