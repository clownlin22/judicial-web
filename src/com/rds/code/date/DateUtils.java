package com.rds.code.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static SimpleDateFormat lineformat = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static SimpleDateFormat zhformat = new SimpleDateFormat(
			"yyyy年MM月dd日");
	
	public static SimpleDateFormat formatforzx = new SimpleDateFormat(
			"yyyy.MM.dd");

	public static SimpleDateFormat intformat = new SimpleDateFormat("yyyyMMdd");
	
	public static SimpleDateFormat formatzh2 = new SimpleDateFormat("yyyy年M月d日");
	
	public static SimpleDateFormat formatTime=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	/**
	 * 把yyyy-MM-dd格式日期转为yyyy年MM月dd日格式日期
	 * 
	 * @param dateString
	 * @return date
	 */
	public static String DateString2DateString(String dateString) {
		String date = "";
		try {
			date = zhformat.format(lineformat.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 日期转yyyyMMdd格式字符串
	 * @param date
	 * @return
	 */
	public static String Date2String(Date date){
		return intformat.format(date);
	}
	
	public static String ZhDate2LineDate(String dateString){
		if (dateString==null||"".equals(dateString)){
			return lineformat.format(new Date());
		}
		String date = "";
		try {
			 date = lineformat.format(zhformat.parse(dateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
}
