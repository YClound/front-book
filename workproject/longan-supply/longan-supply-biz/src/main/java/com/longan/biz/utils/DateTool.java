package com.longan.biz.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTool {
	// 默认转换出为2005-09-08类型的当前日
	public static String getToday() {
		Date dt = new java.util.Date();
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(dt);
	}

	public static String parseDates(Date dt) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(dt);
	}

	public static String parseDates2(Date dt) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(dt);
	}

	public static String parseDates3(Date dt) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
		return sdf.format(dt);
	}

	public static String parseDate(Date dt) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(dt);
	}

	public static String parseDate8(Date dt) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
		return sdf.format(dt);
	}

	public static Date strintToDate(String ds) throws ParseException {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Date d = sdf.parse(ds);
		return d;
	}

	public static Date strintToDate8(String ds) throws ParseException {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
		Date d = sdf.parse(ds);
		return d;
	}

	public static Date strintTotime() throws ParseException {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
		Date d = null;
		d = sdf.parse(DateTool.getTime("HH:mm:ss"));
		return d;
	}

	public static Date strintToDatetime(String ds) throws ParseException {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = sdf.parse(ds);
		return d;
	}

	public static Date strintToDatetime2(String ds) throws ParseException {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		Date d = sdf.parse(ds);
		return d;
	}

	/**
	 * 日期字符串重新格式化
	 * 
	 * @param ds
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static String strSwapFormat(String ds, String oldFormat, String newFormat)
			throws ParseException {
		SimpleDateFormat osdf = new java.text.SimpleDateFormat(oldFormat);
		Date d = osdf.parse(ds);
		SimpleDateFormat nsdf = new java.text.SimpleDateFormat(newFormat);
		return nsdf.format(d);
	}

	// 根据传入的type进行转换日期，type必须遵循Date转换的规则
	public static String getTime(String type) {
		Date dt = new java.util.Date();
		SimpleDateFormat sdf = new java.text.SimpleDateFormat(type);
		return sdf.format(dt);
	}

	public static int getDay(String d) {
		int day = 1;
		if (d.length() >= 10) {
			day = Integer.parseInt(d.substring(8, 10));
		}
		return day;
	}

	public static int getMonth(String d) {
		int m = 1;
		if (d.length() >= 10) {
			m = Integer.parseInt(d.substring(5, 7));
		}
		return m;
	}

	public static int getYear(String d) {
		int y = 1;
		if (d.length() >= 10) {
			y = Integer.parseInt(d.substring(0, 4));
		}
		return y;
	}

	// 根据传入的两个日期计算相差天数
	public static int getDayBetween(Calendar c1, Calendar c2) {
		int iReturn = 0;
		if (c1 != null && c2 != null) {
			iReturn = (int) ((c2.getTimeInMillis() - c1.getTimeInMillis()) / (24 * 60 * 60 * 1000));
		}
		return iReturn;
	}

	// 根据传入的两个日期计算相差年数
	public static int getYearBetween(Calendar c1, Calendar c2) {
		int iReturn = 0;
		if (c1 != null && c2 != null) {
			iReturn = (int) (getDayBetween(c1, c2) / 365);
		}
		return iReturn;
	}

	/**
	 * 把2005-02-5这样的日期格式转化为Calendar类型
	 * 
	 * @param stringTo
	 * @return
	 * @throws ParseException
	 */
	public static Calendar stringToCalendar(String stringTo) throws ParseException {
		Calendar c = Calendar.getInstance();
		if (stringTo != null) {
			SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(stringTo);
			c.setTime(d);
		}
		return c;
	}

	public static String getWeekOfYear(String s) throws ParseException {
		Calendar c = DateTool.stringToCalendar(s);
		return String.valueOf(c.get(Calendar.WEEK_OF_YEAR));

	}

	public static String getNow() {
		return DateTool.getTime("yyyy-MM-dd HH:mm:ss");
	}

	public static String getNow14() {
		return DateTool.getTime("yyyyMMdd HH:mm:ss");
	}

	public static String getNowTime() {
		return DateTool.getTime("HH:mm:ss");
	}

	public static Date afterDay(Date date, Integer day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, +day);
		return c.getTime();

	}

	public static Date beforeDay(Date date, Integer day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -day);
		return c.getTime();

	}

	private static final long CRITICAL_MIN = 60;
	private static final long CRITICAL_HOUR = 60 * 60;
	private static final long CRITICAL_DAY = 60 * 60 * 24;

	public static String secondToDate(Integer second) {
		String result = null;
		Integer min = second / 60;
		Integer sec = second % 60;
		if (second >= CRITICAL_DAY) {
			Integer hour = min / 60;
			Integer day = hour / 24;
			hour = hour % 24;
			result = day + "天" + hour + "小时";
		} else if (second >= CRITICAL_HOUR) {
			Integer hour = min / 60;
			min = min % 60;
			result = hour + "小时" + min + "分";
		} else if (second >= CRITICAL_MIN) {
			result = min + "分" + sec + "秒";
		} else {
			result = sec + "秒";
		}

		return result;
	}
}