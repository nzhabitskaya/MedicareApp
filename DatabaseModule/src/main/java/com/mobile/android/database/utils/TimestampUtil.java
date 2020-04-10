package com.mobile.android.database.utils;

import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TimestampUtil {
    public static final long DATE_OF_BIRTH = 1419984000; //2012-12-31 date of birth of Antonona Hathaway

	public static String getCurrentTimestamp(){
		return String.valueOf(System.currentTimeMillis()/1000);
	}
	
	public static String generateNonce(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }

    public static String parseDate(long milliSeconds) {
        return DateFormat.format("dd MMM yyyy", new Date(milliSeconds * 1000)).toString();
    }

    public static int findDifferenceInDays(long currentDate, long startDate) {
        Date date2 = new Date(currentDate * 1000);
        Date date1 = new Date(startDate * 1000);

        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }

        int differenceValue = Integer.parseInt(result.values().toArray()[0].toString());
        return differenceValue;
    }
}
