package com.mobile.android.database;

import android.content.Context;

public class DataSourceHolder {
    private static DataSourceHolder holder;
    private WithingsDataSource measuresDataSource;
    private TemperaturDataSource temperaturDataSource;

    DataSourceHolder(Context context){
        measuresDataSource = new WithingsDataSource(context);
        temperaturDataSource = new TemperaturDataSource(context);
    }

    public static DataSourceHolder getInstance(Context context){
        if(holder == null)
            holder = new DataSourceHolder(context);
        return holder;
    }

    public WithingsDataSource getWithingsDataSource() {
        return measuresDataSource;
    }

    public TemperaturDataSource getTemperaturDataSource() {
        return temperaturDataSource;
    }
}
