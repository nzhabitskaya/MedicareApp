package com.mobile.android.ebabynotebook.ui.fragments.nodes.weight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mobile.android.database.DataSourceHolder;
import com.mobile.android.database.WithingsDataSource;
import com.mobile.android.database.beans.Measure;
import com.mobile.android.ebabynotebook.R;
import com.mobile.android.ebabynotebook.utils.DialogUtil;
import com.mobile.android.chart.data.DataManager;
import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionManager;
import com.mobile.android.withings.service.connection.OAuth;
import com.mobile.android.withings.service.rest.callback.RestCallback;
import com.mobile.android.withings.ui.CustomUserDialog;
import com.mobile.android.withings.ui.fragment.RequestWeightDataFragment;

import java.util.List;

public class ManuellErfassenFragment extends RequestWeightDataFragment implements RestCallback{
    private EditText gewicht;
    private EditText kommentar;
    private Button btn_save;
    private ImageButton btn_reload;
    private CustomUserDialog dialog;
    private TableLayout table;
    private List<Measure> measures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_weight_manuell, container, false);
        initViews(view);
        ConnectionManager.getInstance().registerResponseListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initTimeStamp();
        reloadTable();
    }

    @Override
    public void onError(final Intent intent) {
        hideProgress();
        DialogUtil.showErrorDialog(getActivity(), com.mobile.android.withings.R.string.message_connection_error);
    }

    @Override
    public void onInternalError(final Intent intent) {
        hideProgress();
        DialogUtil.showErrorDialog(getActivity(), com.mobile.android.withings.R.string.message_connection_error);
    }

    @Override
    public void onSuccess(Intent intent) {
        hideProgress();
        if(intent.getAction() == RestService.Action.REQUEST_MEASURES){
            String status = intent.getStringExtra(RestService.RESPONSE_STATUS);
            if(status.equals("342")){
                dialog.showErrorMessage(R.string.msg_user_is_not_authorized);
            } else {
                reloadTable();
            }
        }
    }

    private void initViews(View view) {
        datum = (TextView) view.findViewById(R.id.datum);
        zeit = (TextView) view.findViewById(R.id.zeit);
        gewicht = (EditText) view.findViewById(R.id.gewicht);
        kommentar = (EditText) view.findViewById(R.id.kommentar);

        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(listener);
        btn_reload = (ImageButton) view.findViewById(R.id.btn_reload);
        btn_reload.setOnClickListener(listener);
        datum.setOnClickListener(listener);
        zeit.setOnClickListener(listener);
        dialog = new CustomUserDialog(getActivity());
        table = (TableLayout) view.findViewById(R.id.table);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.datum:
                    createDatePickerDialog().show();
                    break;
                case R.id.zeit:
                    createTimePickerDialog().show();
                    break;
                case R.id.btn_save:
                    if(isUserAuthorized()) {
                        saveData();
                        reloadTable();
                    } else {
                        dialog.showErrorMessage(R.string.msg_user_is_not_authorized);
                    }
                    break;
                case R.id.btn_reload:
                    if(isUserAuthorized()) {
                        String accessToken = DataManager.getInstance().getAccessToken(getActivity());
                        getMeasures(OAuth.KEY, accessToken);
                    } else {
                        dialog.showErrorMessage(R.string.msg_user_is_not_authorized);
                    }
                    break;
            }
        }
    };

    private boolean isUserAuthorized(){
        if(DataManager.getInstance().getUserId(getActivity()).length() == 0){
            return false;
        } else{
            return true;
        }
    }

    private void saveData() {
        try {
            // Put measures in db
            String userId = DataManager.getInstance().getUserId(getActivity());
            String dateStr = String.valueOf(currentTimestamp / 1000);
            String weightStr = gewicht.getText().toString();
            float weightVal = 0;
            String kommentarStr = kommentar.getText().toString();
            String accountName = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("name", "");

            if(weightStr.length() == 0){
                DialogUtil.showErrorDialog(getActivity(), R.string.weight_message);
            } else {
                weightVal = Float.valueOf(weightStr);
            }
            if (weightVal < 1500 || weightVal > 25000) {
                DialogUtil.showErrorDialog(getActivity(), R.string.weight_message);
            } else {
                WithingsDataSource datasource = DataSourceHolder.getInstance(getActivity()).getWithingsDataSource();
                datasource.open();
                datasource.createMeasure(userId, accountName, dateStr, weightStr, kommentarStr, "M");
                datasource.close();
            }

        } catch (java.sql.SQLException e) {
            Log.e("", e.getMessage());
        }
    }

    private void parseWeightData() {
        try {
            WithingsDataSource datasource = DataSourceHolder.getInstance(getActivity()).getWithingsDataSource();
            datasource.open();

            String accountId = DataManager.getInstance().getUserId(getActivity());
            String accountName = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("name", "");
            measures = datasource.getAllMeasures(accountId, accountName);
            for (Measure measure : measures) {
                String type = measure.getType();
                long measureId = measure.getId();
                long timestamp = Long.parseLong(measure.getTimestamp()) * 1000;
                String datum = android.text.format.DateFormat.format("dd.MM.yyyy", timestamp).toString();
                String zeit = android.text.format.DateFormat.format("hh:mm", timestamp).toString();
                String weight = measure.getWeight();
                String comment = measure.getComment();
                drawTableRow(getActivity(), measureId, type, datum, zeit, weight, comment);
            }

            datasource.close();
        } catch (java.sql.SQLException e) {
            Log.e("", e.getMessage());
        }
    }

    private void deleteMeasureById(long measureId) {
        try {
            WithingsDataSource datasource = DataSourceHolder.getInstance(getActivity()).getWithingsDataSource();
            datasource.open();

            for(Measure measure : measures){
                if(measure.getId() == measureId)
                    datasource.deleteMeasure(measure);
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
        drawTableRow(getActivity(), 0, getString(R.string.table_title_typ), getString(R.string.table_title_datum), getString(R.string.table_title_zeit), getString(R.string.table_title_gewicht), getString(R.string.table_title_kommentar));
        // fill table with data from database
        parseWeightData();
    }

    private void drawTableRow(Context context, long measureId, String str1, String str2, String str3, String str4, String str5){
        TableRow row = new TableRow(context);
        row.setPadding(5,5,5,5);

        TextView cell1 = createCell(context, str1, 60);
        TextView cell2 = createCell(context, str2, 130);
        TextView cell3 = createCell(context, str3, 130);
        TextView cell4 = createCell(context, str4, 60);
        TextView cell5 = createCell(context, str5, 130);
        ImageButton deleteButton = createDeleteButton(context, 50, measureId);

        row.addView(cell1);
        row.addView(cell2);
        row.addView(cell3);
        row.addView(cell4);
        row.addView(cell5);
        if(measureId > 0) // if table row is not table title, then delete table row
            row.addView(deleteButton);

        table.addView(row);
    }

    private TextView createCell(Context context, String text, int maxWidth){
        TextView cell = new TextView(context);
        cell.setBackground(context.getDrawable(R.drawable.cell_shape));
        cell.setText(text);
        cell.setPadding(5,5,5,5);
        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cellParams.setMargins(0, 0, 0, 0);
        cell.setLayoutParams(cellParams);
        cell.setGravity(Gravity.CENTER_HORIZONTAL);
        cell.setMaxWidth(maxWidth);
        cell.setLines(2);
        cell.setTextSize(12);
        return cell;
    }

    private ImageButton createDeleteButton(Context context, int maxWidth, final long measureId){
        ImageButton cell = new ImageButton(context);
        cell.setImageDrawable(context.getDrawable(R.drawable.delete));
        cell.setBackgroundColor(Color.TRANSPARENT);
        cell.setScaleType(ImageView.ScaleType.FIT_CENTER);
        cell.setPadding(5,5,5,5);
        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(maxWidth, maxWidth);
        cellParams.setMargins(0, 0, 0, 0);
        cell.setLayoutParams(cellParams);
        cell.setMaxWidth(maxWidth);
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMeasureById(measureId);
                reloadTable();
            }
        });
        return cell;
    }
}
