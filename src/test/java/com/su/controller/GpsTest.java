package com.su.controller;

import java.text.DecimalFormat;

public class GpsTest {

	public static void main(String[] args) {
		double lat=31.31685623092902;
		/*String x=""+lat;
		System.out.println(x.substring(0,9));	*/
		DecimalFormat df = new DecimalFormat( "0.000000 ");   
		System.out.println(df.format(lat)); 
	}

}
