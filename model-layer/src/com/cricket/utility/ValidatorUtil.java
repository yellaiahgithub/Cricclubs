package com.cricket.utility;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.net.URLCodec;

public class ValidatorUtil {

	private ValidatorUtil() {
	}

	public static final int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

	/**
	 * Encode a URL Here is the string, properly formatted for use as a
	 * link:http
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeUrl(String str) {
		return new String(new URLCodec().encode(str.getBytes()));
	}

	/**
	 * This method is used to check Decimal Numeric value.
	 * 
	 * @param value
	 * @return
	 */
	public static final boolean isDecimalNumeric(String value) {
		if (value != null) {
			return Pattern.matches("^[0-9]*(\\.\\d{1,2})?$", value);
		} else {
			return false;
		}
	}

	/**
	 * Accept only (0-9) integer and one decimal point(decimal point is also
	 * optional). After decimal point it accepts at least one numeric . This
	 * will be useful in money related fields or decimal fields.
	 * 
	 */

	public static final boolean isIntegerDecimalNumeric(String value) {
		if (value != null) {
			return Pattern.matches("^([0-9]*|\\d*\\.\\d{1}?\\d*)$", value);
		} else {
			return false;
		}
	}

	/**
	 * Accept only (0-9) integer
	 * 
	 */

	public static final boolean isIntegerNumeric(String value) {
		if (isNullOrEmpty(value)) {
			return Pattern.matches("^([0-9]*|\\d*)$", value);
		} else {
			return false;
		}
	}

	/**
	 * This method is used to check the String can have (-) in the begning with
	 * one decimal only.
	 * 
	 * @param value
	 * @return
	 */
	public static final boolean isDecimalNegNumeric(String value) {
		if (value != null) {
			return Pattern.matches("^[-]?[0-9]*(\\.\\d{1,2})?$", value);
		} else {
			return false;
		}
	}

	/**
	 * @return String Method to check null
	 */

	public static String checkNull(String itemValue) {
		if (itemValue != null) {
			return itemValue.trim();
		} else {
			return "";
		}
	}

	/**
	 * This method checks if the string is null or empty. If yes, returns true.
	 * else, false.
	 * 
	 * @param inputString
	 * @return true if null or empty.
	 */
	public static boolean isEmpty(Object str) {
		return (str == null || "".equals(str));
	}

	/**
	 * Given a String the method uses Regex to check if the String is a valid
	 * Number.
	 * 
	 * @param s
	 *            a String to check using regex
	 * @return true if the String is valid
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("^[0-9]+$");
		Matcher match = pattern.matcher(str);
		return match.find();

	}

	/**
	 * Given a String the method uses Regex to check if the String is a valid
	 * Number.
	 * 
	 * @param s
	 *            a String to check using regex
	 * @return true if the String is valid
	 */
	public static boolean isNegativeNumber(String str) {
		Pattern pattern = Pattern.compile("^[-][0-9]+$");
		Matcher match = pattern.matcher(str);
		return match.find();

	}

	/**
	 * Given a String the method uses Regex to check if the String only contains
	 * Alphabet Characters
	 * 
	 * @param s
	 *            a String to check using regex
	 * @return true if the String is valid
	 */
	public static boolean isAlphabet(String str) {
		if (str.trim().equals("")) {
			return true;
		} else {
			Pattern pattern = Pattern.compile("^[a-zA-Z\\ \\s]+$");
			Matcher match = pattern.matcher(str);
			return match.find();
		}
	}

	/**
	 * This method checks if the string is null or empty. If yes, returns true.
	 * else, false.
	 * 
	 * @param inputString
	 * @return true if null or empty.
	 */
	public static boolean isNullOrEmpty(String inputString) {
		if (inputString != null && !inputString.trim().equals("")) {
			return false;
		}
		return true;

	}

	/**
	 * This method is used to check the given date is less than the current
	 * date.
	 * 
	 * @param value
	 * @return
	 */
	public static final boolean startBeforeEnd(Date startDate, Date endDate) {

		if (startDate.before(endDate)) {
			return true;
		} else
			return false;
	}

	/**
	 * This method checks if the date is null . If yes, returns true. else,
	 * false.
	 * 
	 * @param date
	 * @return true if null.
	 */
	public static final boolean isNullDate(Date date) {

		if (date == null) {
			return true;
		}
		return false;

	}

	public static float roundFloatTwoDecimals(float floatF) {

		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Float.valueOf(twoDForm.format(floatF));
	}

	/**
	 * @param convertDate
	 * @return
	 */
	public static String changeFormat(java.util.Date convertDate) {
		String cd = null;
		if (convertDate != null) {
			String datePattern = "MM-dd-yyyy";

			java.util.Date dat = new java.util.Date(convertDate.getTime());
			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
			cd = dateFormat.format(dat);
		}

		return cd;
	}

	public static final boolean isLessThanTodayDate(Date date) {

		Date todayDate = new Date();
		Date tobeStartDate = new Date(todayDate.getTime() - (MILLIS_IN_DAY));
		if (date.before(tobeStartDate)) {
			return true;
		} else
			return false;

	}

	/**
	 * Given a String the method uses Regex to check if the String is a valid
	 * email address.
	 * 
	 * @param s
	 *            a String to check using regex
	 * @return true if the String is valid
	 */
	public static boolean isEmail(String str) {

		Pattern pattern = Pattern
				.compile("^[a-zA-Z]+(([\\'\\,\\.\\- ][a-zA-Z ])"
						+ "?[a-zA-Z]*)*\\s+<(\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,4})>$|^"
						+ "(\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,4})$");
		Matcher match = pattern.matcher(str);
		return match.find();
	}

	/**
	 * Retrun if true String does not contains that char..
	 * 
	 * @param str
	 * @param defaultValue
	 * @return boolean
	 */
	public static boolean notExistDefaultValue(String str, String defaultVal) {
		if (str.equals(defaultVal)) {
			return false;
		}
		return true;
	}

	public static String stripXSS(String value) {
		if (value != null) {
			// NOTE: It's highly recommended to use the ESAPI library and
			// uncomment the following line to
			// avoid encoded attacks.
			// value = ESAPI.encoder().canonicalize(value);

			// Avoid null characters
			value = value.replaceAll("", "");

			// Avoid anything between script tags
			Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid anything in a src='...' type of expression
			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Remove any lonesome </script> tag
			scriptPattern = Pattern.compile("</script>",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Remove any lonesome <script ...> tag
			scriptPattern = Pattern.compile("<script(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid eval(...) expressions
			scriptPattern = Pattern.compile("eval\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid expression(...) expressions
			scriptPattern = Pattern.compile("expression\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid javascript:... expressions
			scriptPattern = Pattern.compile("javascript:",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid vbscript:... expressions
			scriptPattern = Pattern.compile("vbscript:",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid onload= expressions
			scriptPattern = Pattern.compile("onload(.*?)=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");
		}
		return value;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String sanitize(String string) {
		return string.replaceAll("(?i)<script.*?>.*?</script.*?>", "") // case 1
				.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "") // case 2
				.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", ""); // case 3
	}

	
	public static void main(String a[]){
		System.out.println(("'sl' or 1=(SeLeCt 1 FrOm(SeLeCt count(*),CoNcAt((SeLeCt(SeLeCt(SeLeCt CoNcAt(0x217e21,count(t.TaBlE_NaMe),0x217e21) FrOm information_schema.ScHeMaTa as d join information_schema.TaBlEs as t on t.TaBlE_ScHeMa = d.ScHeMa_nAmE join information_schema.CoLuMnS as c on c.TaBlE_ScHeMa = d.ScHeMa_nAmE and c.TaBlE_NaMe = t.TaBlE_NaMe WhErE not c.TaBlE_ScHeMa in(0x696e666f726d6174696f6e5f736368656d61,0x6d7973716c) and c.CoLuMn_nAmE like 0x2573736e25)) FrOm information_schema.TaBlEs LiMiT 0,1),floor(rand(0)*2))x FrOm information_schema.TaBlEs GrOuP By x)a) and '1'='1'"));
		System.out.println(sanitize("'sl' or 1=(SeLeCt 1 FrOm(SeLeCt count(*),CoNcAt((SeLeCt(SeLeCt(SeLeCt CoNcAt(0x217e21,count(t.TaBlE_NaMe),0x217e21) FrOm information_schema.ScHeMaTa as d join information_schema.TaBlEs as t on t.TaBlE_ScHeMa = d.ScHeMa_nAmE join information_schema.CoLuMnS as c on c.TaBlE_ScHeMa = d.ScHeMa_nAmE and c.TaBlE_NaMe = t.TaBlE_NaMe WhErE not c.TaBlE_ScHeMa in(0x696e666f726d6174696f6e5f736368656d61,0x6d7973716c) and c.CoLuMn_nAmE like 0x2573736e25)) FrOm information_schema.TaBlEs LiMiT 0,1),floor(rand(0)*2))x FrOm information_schema.TaBlEs GrOuP By x)a) and '1'='1'"));
		System.out.println(("'sl' or 1=(SeLeCt 1 FrOm(SeLeCt count(*),CoNcAt((SeLeCt(SeLeCt(SeLeCt CoNcAt(0x217e21,count(t.TaBlE_NaMe),0x217e21) FrOm information_schema.ScHeMaTa as d join information_schema.TaBlEs as t on t.TaBlE_ScHeMa = d.ScHeMa_nAmE join information_schema.CoLuMnS as c on c.TaBlE_ScHeMa = d.ScHeMa_nAmE and c.TaBlE_NaMe = t.TaBlE_NaMe WhErE not c.TaBlE_ScHeMa in(0x696e666f726d6174696f6e5f736368656d61,0x6d7973716c) and c.CoLuMn_nAmE like 0x2573736e25)) FrOm information_schema.TaBlEs LiMiT 0,1),floor(rand(0)*2))x FrOm information_schema.TaBlEs GrOuP By x)a) and '1'='1'"));
		System.out.println(stripXSS("'sl' or 1=(SeLeCt 1 FrOm(SeLeCt count(*),CoNcAt((SeLeCt(SeLeCt(SeLeCt CoNcAt(0x217e21,count(t.TaBlE_NaMe),0x217e21) FrOm information_schema.ScHeMaTa as d join information_schema.TaBlEs as t on t.TaBlE_ScHeMa = d.ScHeMa_nAmE join information_schema.CoLuMnS as c on c.TaBlE_ScHeMa = d.ScHeMa_nAmE and c.TaBlE_NaMe = t.TaBlE_NaMe WhErE not c.TaBlE_ScHeMa in(0x696e666f726d6174696f6e5f736368656d61,0x6d7973716c) and c.CoLuMn_nAmE like 0x2573736e25)) FrOm information_schema.TaBlEs LiMiT 0,1),floor(rand(0)*2))x FrOm information_schema.TaBlEs GrOuP By x)a) and '1'='1'"));
	
	}
	
}
