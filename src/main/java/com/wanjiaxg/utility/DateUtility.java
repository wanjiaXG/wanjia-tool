package com.wanjiaxg.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtility {

    public static long dateToLong(Date date){
        long result = 0;
        if(date != null){
            result = date.getTime();
        }
        return result;
    }

    public static Date longToDate(long time){
        return new Date(time);
    }

    public static Date stringToDate(String time){
        Date date = null;
        if(time != null){
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = simpleDateFormat.parse(time);
            }catch (Exception e){

            }
        }
        return date;
    }

    public static long stringToLong(String time){
        return dateToLong(stringToDate(time));
    }

}
