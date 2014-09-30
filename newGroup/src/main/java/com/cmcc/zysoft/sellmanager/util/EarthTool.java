/**
 * @文件名：EarthTool.java
 * @author:李三来
 * @mail:li.sanlai@starit.com.cn
 * @date:2012-10-18 上午10:42:01
 */
package com.cmcc.zysoft.sellmanager.util;

import java.math.BigDecimal;

/**
 * @文件名：EarthTool.java
 * @author:李三来
 * @mail:li.sanlai@starit.com.cn
 * @date:2012-10-18 上午10:42:01
 */
public class EarthTool {

	/**
	 * 地球半径
	 */
	private static final double EARTH_RADIUS = 6378137;

	/**
	 * rad
	 * @param d
	 * @return
	 */
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为：千米（公里）
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(double lng1, double lat1, double lng2,
			double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		s = s / 1000;
		s = round(s,3,BigDecimal.ROUND_HALF_UP);
		return s;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为：千米（公里）
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(String lng1Str, String lat1Str, String lng2Str,
			String lat2Str) {
		double lng1 = Double.valueOf(lng1Str);
		double lat1 = Double.valueOf(lat1Str);
		double lng2 = Double.valueOf(lng2Str);
		double lat2 = Double.valueOf(lat2Str);
		
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		s = s / 1000;
		return s;
	}
	

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为：米
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double getDistanceWithMeter(String lng1Str, String lat1Str, String lng2Str,
			String lat2Str) {
		double lng1 = Double.valueOf(lng1Str);
		double lat1 = Double.valueOf(lat1Str);
		double lng2 = Double.valueOf(lng2Str);
		double lat2 = Double.valueOf(lat2Str);
		
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	
	/** 
     * 对double数据进行取精度. 
     * @param value double数据. 
     * @param scale 精度位数(保留的小数位数). 
     * @param roundingMode 精度取值方式. 
     * @return 精度计算后的数据. 
     */ 
    public static double round(double value, int scale, 
             int roundingMode) {   
        BigDecimal bd = new BigDecimal(value);   
        bd = bd.setScale(scale, roundingMode);   
        double d = bd.doubleValue();   
        bd = null;   
        return d;   
    }   

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double distance = getDistance(113.22120, 23.24183, 113.22119,
				23.24183);
		System.out.println("Distance is:" + distance);
	}

}
