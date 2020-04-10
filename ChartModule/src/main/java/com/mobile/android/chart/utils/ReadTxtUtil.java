package com.mobile.android.chart.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadTxtUtil {
    public static final String DATA = "data";

    public static String readTxt(Context context, String textFile){

        String str = "";
        StringBuffer stringBuffer = new StringBuffer("[[{");
        stringBuffer.append("\"data\":\"");
        try {
            InputStream is = context.getAssets().open(textFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            if (is != null) {
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str.replaceAll("\\s+", "\"}, {\"data\":\""));
                    stringBuffer.append("\"}], [{\"data\":\"");
                }
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stringBuffer.delete(stringBuffer.length() - 12, stringBuffer.length());
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
