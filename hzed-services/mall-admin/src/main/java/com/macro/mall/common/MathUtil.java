package com.macro.mall.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 计算工具类
 * @author yangyiqun
 */
public class MathUtil {
	
	private MathUtil() {
	    throw new IllegalAccessError("Utility class");
	  }

	public static final int DIV_SCALE_ZERO			= 0;
	public static final int DIV_SCALE_TWO			= 2;
	public static final int DEFAULT_SCALE 			= 5;
	public static final int DIV_SCALE				= 10;
	/** 掐尾 */
	public static final int DEFAULT_ROUND_MODE 		= BigDecimal.ROUND_DOWN;
	/** 四舍五入 */
	public static final int ROUND_HALF_UP 			= BigDecimal.ROUND_HALF_UP;
	public static final DecimalFormat DEFAULT_DF 	= new DecimalFormat("0.00000");
	public static final DecimalFormat SHORT_DF 		= new DecimalFormat("0.00");
	
	/**
	 * 提供精确的加法运算
	 * @param v1
	 * @param v2
	 * @return double
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的加法运算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal add(BigDecimal v1,BigDecimal v2){
		return v1.add(v2);
	}

	/**
	 * 提供精确的加法运算
	 * @param values
	 * @return
	 */
	public static double add(double...values){
		if(values.length <= 1){
			return new BigDecimal(Double.toString(values[0])).doubleValue();
		} 
		BigDecimal total = new BigDecimal(Double.toString(values[0]));
		for(int i=1;i<values.length;i++){
			BigDecimal b = new BigDecimal(Double.toString(values[i]));
			total = total.add(b);
		}
		return total.doubleValue();
	}
	
	/**
	 * 提供精确的加法运算
	 * @param v1
	 * @param v2
	 * @return String
	 */
	public static String add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).toString();
	}
	
	/**
	 * 提供精确的减法运算
	 * @param v1
	 * @param v2
	 * @return double
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal sub(BigDecimal v1,BigDecimal v2){
		return v1.subtract(v2);
	}

	/**
	 * 提供精确的减法运算
	 * @param v1
	 * @param v2
	 * @return String
	 */
	public static String sub(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).toString();
	}
	
	/**
	 * 提供精确的乘法运算
	 * @param v1
	 * @param v2
	 * @return double
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal mul(BigDecimal v1,BigDecimal v2){
		return v1.multiply(v2);
	}

	/**
	 * 提供精确的乘法运算,保留两位小数，掐尾
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal mulOfTwo(BigDecimal v1,BigDecimal v2){
		return v1.multiply(v2).setScale(DIV_SCALE_TWO, DEFAULT_ROUND_MODE);
	}

	/**
	 * 提供精确的乘法运算
	 * @param v1
	 * @param v2
	 * @return String
	 */
	public static String mul(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).toString();
	}
	
	/**
	 * 提供精确的除法运算
	 * @param v1
	 * @param v2
	 * @return double
	 */
	public static double div(double v1, double v2) {
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, DIV_SCALE, DEFAULT_ROUND_MODE).doubleValue();
		
	}

	/**
	 * 提供精确的除法运算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal div(BigDecimal v1,BigDecimal v2){
		return v1.divide(v2, DIV_SCALE_ZERO, DEFAULT_ROUND_MODE);
	}

	/**
	 * 除法，默认保留2位小数
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal divOfTwoDecimals(BigDecimal v1,String v2){
		BigDecimal b2 = new BigDecimal(v2);
		return div(v1, b2, 2);
	}

	/**
	 * 除法，保留scale为小数
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return
	 */
	public static BigDecimal div(BigDecimal v1,BigDecimal v2 ,int scale){
		return v1.divide(v2, scale, DEFAULT_ROUND_MODE );
	}

	/**
	 * 提供精确的除法运算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal div10(BigDecimal v1,BigDecimal v2){
		return v1.divide(v2, DIV_SCALE, DEFAULT_ROUND_MODE);
	}

	/**
	 * 提供精确的除法运算
	 * @param v1
	 * @param v2
	 * @return double
	 */
	public static String div(String v1, String v2) {
		
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, DIV_SCALE, DEFAULT_ROUND_MODE).toString();
		
	}


	/**
	 * 提供精确的指数运算
	 * @param v1
	 * @param v2
	 * @return double
	 */
	public static double pow(double v1, double v2) {
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		return b1.pow((int)v2).doubleValue();
	}

	/**
	 * 提供精确的指数运算
	 * @param v1
	 * @param v2
	 * @return String
	 */
	public static String pow(String v1, int v2) {
		
		BigDecimal b1 = new BigDecimal(v1);
		return b1.pow((int)v2).toString();
	}
	
	/**
	 * 格式化，指定scale，指定roundMode
	 * @param v
	 * @param scale
	 * @param roundMode
	 * @return double
	 */
	public static double round(double v, int scale, int roundMode) {
		if(scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero.");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		return b.setScale(scale, roundMode).doubleValue();
	}
	
	/**
	 * 格式化，指定scale，默认roundMode
	 * @param v
	 * @param scale
	 * @return double
	 */
	public static double round(double v, int scale) {
		return round(v, scale, DEFAULT_ROUND_MODE);
	}
	
	/**
	 * 格式化，默认scale，默认roundMode
	 * @param v
	 * @return double
	 */
	public static double round(double v) {
		return round(v, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
	}
	
	/**
	 * 格式化，指定scale，指定roundMode
	 * @param v
	 * @param scale
	 * @param roundMode
	 * @return String
	 */
	public static String round(String v, int scale, int roundMode) {
		if(scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero.");
		}
		BigDecimal b = new BigDecimal(v);
		return b.setScale(scale, roundMode).toString();
	}
	
	/**
	 * 格式化，指定scale，默认roundMode
	 * @param v
	 * @param scale
	 * @return double
	 */
	public static String round(String v, int scale) {
		return round(v, scale, DEFAULT_ROUND_MODE);
	}
	
	/**
	 * 格式化，默认scale，默认roundMode
	 * @param v
	 * @return String
	 */
	public static String round(String v) {
		return round(v, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
	}
	
	/**
	 * 格式化显示
	 * @param v
	 * @return
	 */
	public static String format(double v) {
		return DEFAULT_DF.format(round(v));
	}
	
	/**
	 * 格式化显示
	 * @param v
	 * @param decimalFormat
	 * @return
	 */
	public static String format(double v,DecimalFormat decimalFormat) {
		return decimalFormat.format(round(v));
	}
	
	/**
	 * 比较两个数的大小
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean gt(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.compareTo(b2) > 0 ? true : false;
	}

	/**
	 * 
	 * @param b1
	 * @param v2
	 * @return
	 */
	public static boolean gt(BigDecimal b1, double v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.compareTo(b2) > 0 ? true : false;
	}

	/**
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean gt(BigDecimal b1,BigDecimal b2){
		return b1.compareTo(b2) > 0 ? true : false;
	}
	
	/**
	 * 比较两个数的大小
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean ge(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.compareTo(b2) > -1 ? true : false;
	}

	/**
	 * 
	 * @param b1
	 * @param v2
	 * @return
	 */
	public static boolean ge(BigDecimal b1, double v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.compareTo(b2) > -1 ? true : false;
	}

	/**
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean ge(BigDecimal b1,BigDecimal b2){
		return b1.compareTo(b2) > -1 ? true : false;
	}
	
	/**
	 * 比较两个数的大小
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean eq(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.compareTo(b2) == 0 ? true : false;
	}

	/**
	 * 
	 * @param b1
	 * @param v2
	 * @return
	 */
	public static boolean eq(BigDecimal b1, double v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.compareTo(b2) == 0 ? true : false;
	}

	/**
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean eq(BigDecimal b1,BigDecimal b2){
		return b1.compareTo(b2) == 0 ? true : false;
	}

	/**
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean eqInteger(Integer b1,Integer b2){
		return b1.compareTo(b2) == 0 ? true : false;
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static boolean neq(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.compareTo(b2) == 0 ? false : true;
	}

	/**
	 * 
	 * @param b1
	 * @param v2
	 * @return
	 */
	public static boolean neq(BigDecimal b1, double v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.compareTo(b2) == 0 ? false : true;
	}

	/**
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean neq(BigDecimal b1,BigDecimal b2){
		return b1.compareTo(b2) == 0 ? false : true;
	}
	
	/**
	 * 比较两个数的大小
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean lt(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.compareTo(b2) < 0 ? true : false;
	}

	/**
	 * 
	 * @param b1
	 * @param v2
	 * @return
	 */
	public static boolean lt(BigDecimal b1, double v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.compareTo(b2) < 0 ? true : false;
	}

	/**
	 * 比较两个数的大小
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean lt(BigDecimal b1,BigDecimal b2){
		return b1.compareTo(b2) < 0 ? true : false;
	}
	
	/**
	 * 比较两个数的大小
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean le(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.compareTo(b2) < 1 ? true : false;
	}

	/**
	 * 
	 * @param b1
	 * @param v2
	 * @return
	 */
	public static boolean le(BigDecimal b1, double v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.compareTo(b2) < 1 ? true : false;
	}

	/**
	 * 比较两个数的大小
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean le(BigDecimal b1,BigDecimal b2){
		return b1.compareTo(b2) < 1 ? true : false;
	}
	
	/**
	 * 比较两个数的大小
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean gt(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		
		return b1.compareTo(b2) > 0 ? true : false;
	}
	
	/**
	 * 比较两个数的大小
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean ge(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		
		return b1.compareTo(b2) > -1 ? true : false;
	}
	
	/**
	 * 比较两个数的大小
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean eq(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		
		return b1.compareTo(b2) == 0 ? true : false;
	}
	
	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static boolean neq(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		
		return b1.compareTo(b2) == 0 ? false : true;
	}
	
	/**
	 * 比较两个数的大小
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean lt(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		
		return b1.compareTo(b2) < 0 ? true : false;
	}
	
	/**
	 * 比较两个数的大小
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean le(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		
		return b1.compareTo(b2) < 1 ? true : false;
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	public static double min(double...values) {
		if(values.length <= 1){
			return values[0];
		} 
		double min = values[0];
		for(int i=1;i<values.length;i++){
			if(lt(values[i], min)){
				min = values[i];
			}
		}
		return min;
	}

	/**
	 *
	 * @param values
	 * @return
	 */
	public static BigDecimal min(BigDecimal...values) {
		if(values.length <= 1){
			return values[0];
		}
		BigDecimal min = values[0];
		for(int i=1;i<values.length;i++){
			if(lt(values[i], min)){
				min = values[i];
			}
		}
		return min;
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	public static double max(double...values) {
		if(values.length <= 1){
			return values[0];
		} 
		double max = values[0];
		for(int i=1;i<values.length;i++){
			if(gt(values[i], max)){
				max = values[i];
			}
		}
		return max;
	}

	/**
	 *
	 * @param values
	 * @return
	 */
	public static BigDecimal max(BigDecimal...values) {
		if(values.length <= 1){
			return values[0];
		}
		BigDecimal max = values[0];
		for(int i=1;i<values.length;i++){
			if(gt(values[i], max)){
				max = values[i];
			}
		}
		return max;
	}
}
