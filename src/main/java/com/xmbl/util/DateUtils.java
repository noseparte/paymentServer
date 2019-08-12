package com.xmbl.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DateUtils {

	private static final Log logger = LogFactory.getLog(DateUtils.class);
	
	public static final String DEFAULT = "yyyy-MM-dd HH:mm:ss";

	public static final String YYYYMMDD = "yyyy-MM-dd";

	public static final String YYYYMMDDS = "yyyyMMdd";

	public static final String YYYYMMDD_ZH = "yyyy年MM月dd日";

	public static final String YYYYMMDD_DOT = "yyyy.MM.dd";

	public static final String YYYYMM = "yyyyMM";

	public static int FIRST_DAY_OF_WEEK = Calendar.MONDAY; //中国周一是一周的第一天

	/**
	 *
	 * @param strDate
	 * @return
	 */
	public static Date parseDate(String strDate) {
		return parseDate(strDate, null);
	}

	/**
	 * parseDate
	 *
	 * @param strDate
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String strDate, String pattern) {
		if(StringUtils.isEmpty(strDate))
			return null;
		Date date = null;
		try {
			if(pattern == null) {
				pattern = DEFAULT;
			}
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			date = format.parse(strDate);
		} catch (Exception e) {
			logger.error(e);
			logger.error("oops, got an exception: ", e);
		}
		return date;
	}

	/**
	 * format date
	 *
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return formatDate(date, DEFAULT);
	}

	/**
	 * format date
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if(date == null)
			return null;
		String strDate = null;
		try {
			if(pattern == null) {
				pattern = DEFAULT;
			}
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			strDate = format.format(date);
		} catch (Exception e) {
			logger.error(e);
			logger.error("oops, got an exception: ", e);
		}
		return strDate;
	}

	/**
	 * 取得一年的第几周
	 *
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date){
		Calendar c = getCalendar();
		c.setTime(date);
		c.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
		int week_of_year = c.get(Calendar.WEEK_OF_YEAR);
		return week_of_year - 1;
	}

	/**
	 * getWeekBeginAndEndDate
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getWeekBeginAndEndDate(Date date, String pattern){
		Date monday = getMondayOfWeek(date);
		Date sunday = getSundayOfWeek(date);
		return formatDate(monday, pattern) + "-" + formatDate(sunday, pattern) ;
	}

	/**
	 * getWeekBeginAndEndDate
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getWeekBeginAndEndDate(int year, int weekNo, String pattern){
		Date monday = getMondayOfWeek(year, weekNo);
		Date sunday = getSundayOfWeek(year, weekNo);
		return formatDate(monday, pattern) + "-" + formatDate(sunday, pattern) ;
	}

	/**
	 * 根据日期取得对应周周一日期
	 *
	 * @param date
	 * @return
	 */
	public static Date getMondayOfWeek(Date date) {
		Calendar monday = getCalendar();
		monday.setTime(date);
		monday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
		monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return monday.getTime();
	}

	/**
	 * 根据日期取得对应周周一日期
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastMondayOfWeek(Date date) {
		Date thismonday = getMondayOfWeek(date);
		Calendar monday = getCalendar();
		monday.setTime(thismonday);
		monday.add(Calendar.DAY_OF_YEAR, -7);
		return monday.getTime();
	}


	/**
	 * 根据周的周一日期
	 *
	 * @param date
	 * @return
	 */
	public static Date getMondayOfWeek(int year, int weekOfYear) {
		Calendar monday = getCalendar();
		monday.set(Calendar.YEAR, year);
		monday.set(Calendar.WEEK_OF_YEAR, weekOfYear + 1);
		monday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
		monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return monday.getTime();
	}

	/**
	 * 根据日期取得对应周周日日期
	 *
	 * @param date
	 * @return
	 */
	public static Date getSundayOfWeek(Date date) {
		Calendar sunday = getCalendar();
		sunday.setTime(date);
		sunday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
		sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return sunday.getTime();
	}

	/**
	 * 根据周的周日日期
	 *
	 * @param date
	 * @return
	 */
	public static Date getSundayOfWeek(int year, int weekOfYear) {
		Calendar sunday = getCalendar();
		sunday.set(Calendar.YEAR, year);
		sunday.set(Calendar.WEEK_OF_YEAR, weekOfYear + 1);
		sunday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
		sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return sunday.getTime();
	}

	/**
	 * 取得月的剩余天数
	 *
	 * @param date
	 * @return
	 */
	public static int getRemainDayForMonth(Date date) {
		int dayOfMonth = getDayOfMonth(date);
		int day = getPassDayOfMonth(date);
		return dayOfMonth - day;
	}

	/**
	 * 取得月已经过的天数
	 *
	 * @param date
	 * @return
	 */
	public static int getPassDayOfMonth(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得月天数
	 *
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得月第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 取得月最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 取得季度第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfSeason(Date date) {
		return getFirstDateOfMonth(getSeasonDate(date)[0]);
	}

	/**
	 * 取得季度最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfSeason(Date date) {
		return getLastDateOfMonth(getSeasonDate(date)[2]);
	}

	/**
	 * 取得季度天数
	 * @param date
	 * @return
	 */
	public static int getDayOfSeason(Date date) {
		int day = 0;
		Date[] seasonDates  = getSeasonDate(date);
		for (Date date2 : seasonDates) {
			day += getDayOfMonth(date2);
		}
		return day;
	}

	/**
	 * 取得季度剩余天数
	 *
	 * @param date
	 * @return
	 */
	public static int getRemainDayOfSeason(Date date) {
		return getDayOfSeason(date) - getPassDayOfSeason(date);
	}

	/**
	 * 取得季度已过天数
	 *
	 * @param date
	 * @return
	 */
	public static int getPassDayOfSeason(Date date) {
		int day = 0;

		Date[] seasonDates  = getSeasonDate(date);

		Calendar c = getCalendar();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);

		if(month == Calendar.JANUARY || month == Calendar.APRIL || month == Calendar.JULY || month == Calendar.OCTOBER) {//季度第一个月
			day = getPassDayOfMonth(seasonDates[0]);
		} else if(month == Calendar.FEBRUARY || month == Calendar.MAY || month == Calendar.AUGUST || month == Calendar.NOVEMBER) {//季度第二个月
			day = getDayOfMonth(seasonDates[0]) + getPassDayOfMonth(seasonDates[1]);
		} else if(month == Calendar.MARCH || month == Calendar.JUNE || month == Calendar.SEPTEMBER || month == Calendar.DECEMBER) {//季度第三个月
			day = getDayOfMonth(seasonDates[0]) + getDayOfMonth(seasonDates[1]) + getPassDayOfMonth(seasonDates[2]);
		}
		return day;
	}

	/**
	 * 取得季度月
	 *
	 * @param date
	 * @return
	 */
	public static Date[] getSeasonDate(Date date) {
		Date[] season = new Date[3];

		Calendar c = getCalendar();
		c.setTime(date);

		int nSeason = getSeason(date);
		if(nSeason == 1) {//第一季度
			c.set(Calendar.MONTH, Calendar.JANUARY);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.FEBRUARY);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.MARCH);
			season[2] = c.getTime();
		} else if(nSeason == 2) {//第二季度
			c.set(Calendar.MONTH, Calendar.APRIL);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.MAY);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.JUNE);
			season[2] = c.getTime();
		} else if(nSeason == 3) {//第三季度
			c.set(Calendar.MONTH, Calendar.JULY);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.AUGUST);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.SEPTEMBER);
			season[2] = c.getTime();
		} else if(nSeason == 4) {//第四季度
			c.set(Calendar.MONTH, Calendar.OCTOBER);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.NOVEMBER);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.DECEMBER);
			season[2] = c.getTime();
		}
		return season;
	}

	/**
	 *
	 * 1 第一季度  2 第二季度 3 第三季度 4 第四季度
	 *
	 * @param date
	 * @return
	 */
	public static int getSeason(Date date) {

		int season = 0;

		Calendar c = getCalendar();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				season =  1;
				break;
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
				season =  2;
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				season =  3;
				break;
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
				season =  4;
				break;
			default:
				break;
		}
		return season;
	}

	/**
	 * 取得周对应年度
	 * @param date
	 * @return
	 */
	public static Integer getYearOfWeek(Date date) {
		Date sunday = getSundayOfWeek(date);
		return getYear(sunday);
	}

	/**
	 * 取得当前日期年度
	 * @param date
	 * @return
	 */
	public static Integer getYear(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	public static Date getLastWeekDate(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		c.add(Calendar.WEEK_OF_YEAR, -1);
		return c.getTime();
	}

	public static Date getNextWeekDate(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		c.add(Calendar.WEEK_OF_YEAR, 1);
		return c.getTime();
	}

	private static Calendar getCalendar() {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
		return c;
	}

	public static Date getLastWeekMonday(int year, int weekOfYear) {
		Date date = getMondayOfWeek(year, weekOfYear);
		return getLastWeekDate(date);
	}

	public static Date getLastWeekSunday(int year, int weekOfYear) {
		Date date = getSundayOfWeek(year, weekOfYear);
		return getLastWeekDate(date);
	}

	public static Date getNextWeekMonday(int year, int weekOfYear) {
		Date date = getMondayOfWeek(year, weekOfYear);
		return getNextWeekDate(date);
	}

	public static Date getDateTime(long time) {
		return new Date(time);
	}

	public static String getDateAdd(String pattern,int value) {

        if (null == pattern || "".equals(pattern)){
            pattern = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dt = "";
        Date date = null;
        if(value!=0){
            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.DAY_OF_MONTH,value);
            date = cal.getTime();
        }else{
            date = new Date();
        }
        dt = sdf.format(date);
        return dt;

 }

	 /**
     * 计算出date day天之前或之后的日期
     *
     * @param date 日期
     * @param date 天数，正数为向后几天，负数为向前几天
     * @return 返回Date日期类型
     */
    public static Date getDateBeforeOrAfterDays(Date date, int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
        return now.getTime();
    }

    /**
     * 计算出date monthes月之前或之后的日期
     *
     * @param date    日期
     * @param monthes 月数，正数为向后几天，负数为向前几天
     * @return 返回Date日期类型
     */
    public static Date getDateBeforeOrAfterMonthes(Date date, int monthes) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + monthes);
        return now.getTime();
    }

    /**
     * 计算出date years年之前或之后的日期
     *
     * @param date  日期
     * @param years 年数，正数为向后几天，负数为向前几天
     * @return 返回Date日期类型
     */
    public static Date getDateBeforeOrAfterYears(Date date, int years) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.YEAR, now.get(Calendar.YEAR) + years);
        return now.getTime();
    }

    /**
     * 计算两个日期之间的天数
     *
     * @param beginDate
     * @param endDate
     * @return 如果beginDate 在 endDate之后返回负数 ，反之返回正数
     */
    public static int daysOfTwoDate(Date beginDate, Date endDate) {

        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        beginCalendar.setTime(beginDate);
        endCalendar.setTime(endDate);

        return getDaysBetween(beginCalendar, endCalendar);

    }

    /**
     * 计算两个日期之间的天数
     *
     * @param d1
     * @param d2
     * @return 如果d1 在 d2 之后返回负数 ，反之返回正数
     */
    public static int getDaysBetween(Calendar d1, Calendar d2) {

        int days = 0;
        int years = d1.get(Calendar.YEAR) - d2.get(Calendar.YEAR);
        if (years == 0) {
            days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
            return days;
        } else if (years > 0) {
            for (int i = 0; i < years; i++) {
                d2.add(Calendar.YEAR, 1);
                days += -d2.getActualMaximum(Calendar.DAY_OF_YEAR);
                if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
                    days += d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
                    return days;
                }
            }
        } else {

            for (int i = 0; i < -years; i++) {
                d1.add(Calendar.YEAR, 1);
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                if (d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
                    days += d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
                    return days;
                }
            }

        }
        return days;


    }

	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(long s){
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

    /**
     * 获得当前时间当天的开始时间，即当前给出的时间那一天的0时0分0秒的时间
     *
     * @param date 当前给出的时间
     * @return
     * @throws ParseException
     */
    public static Date getDateBegin(Date date) {
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            try {
                return DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.CHINA).parse(ymdFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Date getDateEnd(Date date) {
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            try {
                date = getDateBeforeOrAfterDays(date, 1);
                date = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.CHINA).parse(ymdFormat.format(date));
                Date endDate = new Date();
                endDate.setTime(date.getTime() - 1000);

                return endDate;
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return null;
    }

	public static List<String> getDateList(String begin, String end) {
		Date startDate = parseDate(begin);
		Date endDate = parseDate(end);
		Date date = startDate;
		List<String> list = new ArrayList<String>();
		while(date.compareTo(endDate) <= 0) {
			list.add(formatDate(date));
			date = getDateBeforeOrAfterDays(date, 1);
		}
		return list;
	}

    /**
     *
     * @param year
     * @param weekNo
     * @param i  0周一 1 周二 2 周三 以次类推
     * @return
     */
	public static Date getDateOfWeek(int year, int weekOfYear, int i) {
		Calendar day = getCalendar();
		day.set(Calendar.YEAR, year);
		day.set(Calendar.WEEK_OF_YEAR, weekOfYear + 1);
		day.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
		switch (i) {
		case 0:
			day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			break;
		case 1:
			day.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			break;
		case 2:
			day.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
			break;
		case 3:
			day.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
			break;
		case 4:
			day.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			break;
		case 5:
			day.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			break;
		case 6:
			day.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			break;
		}

		return day.getTime();
	}

	public static String getWeekNameByWeekNum(int i) {
		String weekName = null;
		switch (i) {
		case 0:
			weekName = "星期一";
			break;
		case 1:
			weekName = "星期二";
			break;
		case 2:
			weekName = "星期三";
			break;
		case 3:
			weekName = "星期四";
			break;
		case 4:
			weekName = "星期五";
			break;
		case 5:
			weekName = "星期六";
			break;
		case 6:
			weekName = "星期日";
			break;
		}
		return weekName;
	}



	public static int getYearOfPreYearWeek(int year, int week) {
		return week == 1 ? year -1 : year;
	}

	public static int getWeekOfPreYearWeek(int year, int week) {
		return week == 1 ? getMaxWeekNumOfYear(year) : week - 1;
	}


	/**
	* 得到某一年周的总数
	*
	* @param year
	* @return
	*/
	public static int getMaxWeekNumOfYear(int year) {
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
		return getWeekNumber(c.getTime());
	}

	/**
	* 取得当前日期是多少周
	*
	* @param date
	* @return
	*/
	public static int getWeekNumber(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime (date);

		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 取得当前周前后几周
	 *
	 * @param date
	 * @param weeks
	 * @return
	 */
	public static Date getDateBeforeOrAfterWeeks(Date date, int weeks) {
		Calendar day = getCalendar();
		day.setTime(date);
		day.set(Calendar.WEEK_OF_YEAR, day.get(Calendar.WEEK_OF_YEAR) + weeks);
		return day.getTime();
	}

	public static String getWeekOfMonthString(Date date) {
		Calendar day = getCalendar();
		day.setTime(date);
		int month = day.get(Calendar.MONTH);
		int week = day.get(Calendar.WEEK_OF_MONTH);
		return (month + 1) + "月第" + week + "周";
	}

	public static String getStringTime(long time) {
		String hour = String.valueOf(time/3600);
		if(hour.length() < 1) {
			hour = "0" + hour;
		}
		String mintus = String.valueOf(time%3600/60);
		if(mintus.length() < 1) {
			mintus = "0" + mintus;
		}
		String second = String.valueOf(time%3600%60);
		if(second.length() < 1) {
			second = "0" + second;
		}
		return hour + ":" + mintus + ":" + second;
	}

	public static long getSecondTime(String time) {
		long rt = 0;
		if(time != null) {
			String[] times = time.split(":");
			if(times.length == 3) {
				rt = Long.parseLong(times[0]) * 3600 + Long.parseLong(times[1]) * 60 + Long.parseLong(times[2]);
			}
		}
		return rt;
	}
	
	/**
	 * 通过c#当前时间毫秒数获取java当前时间毫秒
	 * @return
	 */
	public static long getJavaCurrentTimeMillisByCCurrentTimeMillis(long cCurrentTimeMillis) {
		long dateDifference = cCurrentTimeMillis - 621355968000000000l;
		if (dateDifference > 0) {
			return dateDifference / 10000l;
		}
		return 0;
	}
	
	/**
	 * 根据java 当前时间毫秒数获取c#当前时间毫秒数
	 * @param javaCurrentTimeMillis
	 * @return
	 */
	public static Long getCCurrentTimeMillisByJavaCurrentTimeMillis(long javaCurrentTimeMillis) {
		try {
			long cCurrentTimeMillis = javaCurrentTimeMillis * 10000l + 621355968000000000l;
			return cCurrentTimeMillis;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * 根据当前时间毫秒数获取当前时间日期
	 * @param millis 当前时间毫秒数
	 * @return
	 */
	public static Date getDateByDateTimeMillis(long millis) {
		try {
			Timestamp ts = new Timestamp(millis);
//			System.out.println(ts.getTime());
	        Date date = new Date(ts.getTime());
            return date;
		}catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 两个时间之间的比较
	 * <br></br>
	 * 判断时间1是否小于等于时间2
	 * @return 满足情况则返回true, 不满足情况则返回false
	 */
	public static boolean Date1LeqDate2 (Date date1,Date date2) {
		try {
			Long time1 = date1.getTime();
			Long time2 = date2.getTime();
			return  (time1 - time2 <= 0);
		} catch (Exception e){
			return false;
		}
	}
	
	/**
	 * 两个时间之间的比较
	 * <br></br>
	 * 判断时间1是否大于等于时间2
	 * @return 满足情况则返回true, 不满足情况则返回false
	 */
	public static boolean Date1GeqDate2 (Date date1,Date date2) {
		try {
			Long time1 = date1.getTime();
			Long time2 = date2.getTime();
			return  (time1 - time2 >= 0);
		} catch (Exception e){
			return false;
		}
	}

	/**
	 * 根据开始时间 和 结束时间 获取开始时间和结束时间之间的时间 
	 * @param javaStartCurrentDate 开始日期
	 * @param javaEndCurrentDate 结束日期
	 * @param hasStartDate            是否包含开始时间
	 * @param hasEndDate 			  是否包含结束时间
	 * @return
	 */
	public static List<Date> getStartDateAndEndDateLst(
			Date startDate, Date endDate,boolean hasStartDate,boolean hasEndDate) {
		List<Date> dateLst = new ArrayList<Date>();
		// 计算两个日期之间相差多少天
	    int nDay = daysOfTwoDate(startDate, endDate);
		if (hasStartDate) {
			dateLst.add(startDate);
		}
		Date tempDate = null;
		for (int i = 1;i<nDay;i++) {
			tempDate = getDateBeforeOrAfterDays(startDate, i);
			if (daysOfTwoDate(tempDate, endDate)>0){
				dateLst.add(tempDate);
			} else { // 最后一天
				if (hasEndDate) {
					dateLst.add(endDate);
				}
			}
		}
		return dateLst;
	}
	
	public static void main(String[] args) {
		 Long long1 =  getCCurrentTimeMillisByJavaCurrentTimeMillis(1510934406l);
		 System.out.println(long1);
		 long1 =  getCCurrentTimeMillisByJavaCurrentTimeMillis(1511020806l);
		 System.out.println(long1);
		 long1 =  getCCurrentTimeMillisByJavaCurrentTimeMillis(1511107206l);
		 System.out.println(long1);
	}
}
