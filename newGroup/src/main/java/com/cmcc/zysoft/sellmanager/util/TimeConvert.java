package com.cmcc.zysoft.sellmanager.util;

import java.util.Date;


public class TimeConvert {
	
    public static String convertToTime(int value)
    {
//    	int ms = value %1000;
    	int ss = 0;
    	int mu = 0;
    	int ho = 0;
    	String str;

    	if(value > 1000)
    	{
    		ss = value/1000;
    	}
    	
    	if(ss > 60){
    		mu = ss/60;
    		ss = ss%60;
    	}

    	if(mu > 60){
    		ho = mu/60;
    		mu = mu%60;
    	}

    	if(ho > 10)
    	{
    		str = ho+"";
    	}else{
    		str = "0"+ho+"";
    	}

    	if(mu<10){
    		str = str + ":0"+mu;
    	}else{
    		str = str + ":"+mu;
    	}
    	
    	if(ss<10){
    		str =str + ":0"+ss;
    	}else{
    		str =str + ":"+ss;
    	}
    	
//    	if(ms < 10){
//    		str = str +":0"+ms;
//    	}else{
//    		str = str +":"+ms;
//    	}
    	    	
    	return str;
    }
    
    /**
     * 根据Long类型返回分钟数
     * @param d
     * @return
     */
    public static String LongToMinutes(Long d)
    {
    	Date date = new Date(d);
    	int hour = date.getHours();
    	int minute = date.getMinutes() + hour*60;
    	return String.valueOf(minute);
    }
    
    
    public static String LongToDate(Long d, int type)
    {
    	Date date = new Date(d);
    	String str = "";
    	
    	if(type == 0){
	    	int month = date.getMonth()+1;
	    	int day = date.getDate();
	    	if(month < 10){
	    		str += "0";
	    	}
	    	
	    	str += (month+"月");
	    	
	    	if(day< 10){
	    		str += "0";
	    	}
	    	
	    	str += (day+"日");
    	}else if (type == 1){
	    	int hour = date.getHours();
	    	int minute = date.getMinutes();
	    	if(hour < 10){
	    		str += "0";
	    	}
	    	
	    	str += (hour+":");
	    	
	    	if(minute< 10){
	    		str += "0";
	    	}
	    	
	    	str += (minute+"");
    	}else if(type == 2){
	    	int month = date.getMonth()+1;
	    	int day = date.getDate();
	    	if(month < 10){
	    		str += "0";
	    	}
	    	
	    	str += (month+"-");
	    	
	    	if(day< 10){
	    		str += "0";
	    	}
	    	
	    	str += (day+"");
    	}
    	
    	return str;
    }

    
}
