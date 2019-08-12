package com.xmbl.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Jdate2CshapeTicksUtil {
	 private static final long TICKS_AT_EPOCH        = 621355968000000000L;
     
	    private static final long TICKS_PER_MILLISECOND = 10000; 
	     
	    private static final SimpleDateFormat sdf       = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	 
	    private static TimeZone timeZone = TimeZone.getDefault();
	 
	    /**
	     * 将C#的ticks值转换成Java的Date对象
	     * @param ticks
	     * @return
	     */
	    public static Date fromDnetTicksToJdate(long ticks){
	        Calendar calendar = Calendar.getInstance(timeZone);
	        calendar.setTimeInMillis((ticks - TICKS_AT_EPOCH) / TICKS_PER_MILLISECOND);
	        calendar.setTimeInMillis(calendar.getTimeInMillis() - calendar.getTimeZone().getRawOffset());
	        return calendar.getTime();
	    }
	 
	    /**
	     * 将日期的字符串值转换成C#的ticks值
	     * @param time
	     * @return
	     */
	    public static long getCShapeTicks(String time){
	        long result = -1;
	        Date date = null;
	        try {
	            date = sdf.parse(time);
	            Calendar calendar = Calendar.getInstance(timeZone);
	            calendar.setTime(date);
	            return (calendar.getTimeInMillis() + calendar.getTimeZone().getRawOffset()) * TICKS_PER_MILLISECOND + TICKS_AT_EPOCH;
	        } catch (Exception e) {
	 
	        }
	        return result;
	    }
	 
	    /**
	     * 将Java日期对象转换成C#的ticks值
	     * @param jDate
	     * @return
	     */
	    public static long getCShapeTicks(Date jDate){
	        long result = -1;
	        try {
	            Calendar calendar = Calendar.getInstance(timeZone);
	            calendar.setTime(jDate);
	            return (calendar.getTimeInMillis() + calendar.getTimeZone().getRawOffset()) * TICKS_PER_MILLISECOND + TICKS_AT_EPOCH;
	        } catch (Exception e) {
	 
	        }
	        return result;
	    }
}
