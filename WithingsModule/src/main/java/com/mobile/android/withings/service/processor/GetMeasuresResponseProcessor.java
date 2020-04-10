package com.mobile.android.withings.service.processor;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mobile.android.chart.data.DataManager;
import com.mobile.android.database.DataSourceHolder;
import com.mobile.android.database.WithingsDataSource;
import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionResponse;
import com.mobile.android.withings.service.connection.OAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetMeasuresResponseProcessor extends ResponseProcessor<ConnectionResponse> {
    private Context mContext;

    public GetMeasuresResponseProcessor(Context context, Intent requestIntent) {
        super(context, requestIntent);
        mContext = context;
    }

    @Override
    void processSuccess(ConnectionResponse connectionResponse) {
        Log.e(OAuth.TAG, connectionResponse.getResponse());
        String responseStr = connectionResponse.getResponse();

        parseMeasures(responseStr);
    }

    private void parseMeasures(String jsonStr){
        try {
            WithingsDataSource datasource = DataSourceHolder.getInstance(mContext).getWithingsDataSource();
            datasource.open();

            JSONObject jsonObj = new JSONObject(jsonStr);
            String status = jsonObj.getString("status");
            mRequestIntent.putExtra(RestService.RESPONSE_STATUS, status);

            JSONArray groups = jsonObj.getJSONObject("body").getJSONArray("measuregrps");

            for(int i = 0; i < groups.length(); i++) {
                JSONObject group = (JSONObject) groups.get(i);
                String date = group.getString("date");

                String comment = "";
                if(group.has("comment"))
                    comment = group.getString("comment");

                JSONObject measures = (JSONObject) group.getJSONArray("measures").get(0);
                String weightValue = measures.getString("value");
                String userId = DataManager.getInstance().getUserId(mContext);

                // Check if entry for this day does nit exist
                // Put measures in db
                String accountName = PreferenceManager.getDefaultSharedPreferences(mContext).getString("name", "");
                datasource.findOrCreateMeasure(userId, accountName, date, weightValue, comment, "A");
            }

            datasource.close();

        } catch (JSONException e){
            Log.e("", e.getMessage());
        } catch (java.sql.SQLException e){
            Log.e("", e.getMessage());
        }
    }

    @Override
    void processError(ConnectionResponse connectionResponse) {
    }
}
