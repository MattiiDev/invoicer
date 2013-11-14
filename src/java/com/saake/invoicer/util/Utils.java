/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.naming.InitialContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author jaizen
 */
public class Utils {

    public static String getPageId(String key){

        return ResourceBundle.getBundle("/com/incon/core/app/properties/viewIds").getString(key);
    }

    private static final Log log = LogFactory.getLog(Utils.class);

    private static NumberFormat decimal8Format = new DecimalFormat("0.00000000");

    /**
     * Helper method to check if a String is blank or null (Copied from Java commons)
     *
     * @param str String to check
     * @return true if the String is blank or null
     */
    public static boolean isBlank(String str) {
        return (str == null || (str.trim().length() < 1 || str.trim().toUpperCase().equals("NULL")));
    }

    /**
     * @param str String to check
     * @return boolean true is String is not blank or null
     */
    public static boolean notBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isNullOrZero(Long value) {
        return (value == null || value == 0);
    }

    public static boolean isNotNullOrZero(Long value) {
        return !isNullOrZero(value);
    }

    /**
     * This method will convert 'Y', 'y', 'N', or 'n' to boolean true or false.
     *
     * @param yOrN String 'Y', 'y', 'N', or 'n', null will be treated as "NO" value
     * @return boolean
     */
    public static boolean convertYorNToBoolean(String yOrN) {
        return "Y".equalsIgnoreCase(yOrN);
    }

    /**
     * This method converts true value to "Y" and false value to "N"
     *
     * @param trueOrFalse boolean
     * @return String
     */
    public static String convertBooleanToYorN(boolean trueOrFalse) {
        return trueOrFalse ? "Y" : "N";
    }

    /**
     * @param str String
     * @return int
     */
    public static int getIntValue(String str) {
        int value = 0;

        try {
            value = Integer.parseInt(str);
        } catch (Exception e) {
            // Do nothing, return default value
        }

        return value;
    }

    /**
     * @param str String
     * @return long
     */
    public static long getLongValue(String str) {
        long value = 0L;

        try {
            value = Long.parseLong(str);
        } catch (Exception e) {
            // Do nothing, return default value
        }

        return value;
    }

    public static String getNotnullString(String str) {
        return str == null ? "" : str;
    }

    /**
     * @param str String
     * @return long
     */
    public static Double getDoubleValue(String str) {
        Double value = null;

        try {
            value = Double.parseDouble(str);
        } catch (Exception e) {
            // Do nothing, return default value
        }

        return value;
    }


    /**
     * @param str String
     * @return boolean
     */
    public static boolean getBooleanValue(String str) {
        boolean value = false;

        try {
            value = Boolean.parseBoolean(str);
        } catch (Exception e) {
            // Do nothing, return default value
        }

        return value;
    }

    /**
     * Helper method to check if a List is null or empty
     *
     * @param aList List to check
     * @return true if list is not null or empty
     */
    public static boolean containsEntries(List aList) {
        return ((aList != null) && (!aList.isEmpty()));
    }

    /**
     * Adds the given number of days and returns new date.
     *
     * @param d    Date
     * @param days int
     * @return Date
     */
    public static Date addDays(Date d, int days) {
        if (d == null) {
            return d;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * Adds the given number of days and returns new date.
     *
     * @param d    Date
     * @param days int
     * @return Date
     */
    public static Date subtractDays(Date d, int days) {
        if (d == null) {
            return d;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }

    /**
     * get next date.
     *
     * @param d Date
     * @return Date
     */
    public static Date getNextDate(Date d) {
        if (d == null) {
            return d;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    /**
     * Formats double number up to 8 decimal digits
     *
     * @param d Date
     * @return String
     */
    public static String formatDouble(Double d) {
        if (d == null) return "";
        NumberFormat f = new DecimalFormat("#0.00######");
        return f.format(d);
    }

    /**
     * Formates Date to MM/dd/yyyy pattern
     *
     * @param d Date
     * @return String
     */
    public static String formatDate(Date d) {
        if (d == null) return "";
        SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
        return f.format(d);
    }

    /**
     * Formates Date to MM/dd/yyyy pattern
     *
     * @param d Date
     * @return String
     */
    public static String formatDateTime(Date d) {
        if (d == null) return "";
        SimpleDateFormat f = new SimpleDateFormat("MM/dd/yy");
        return f.format(d);
    }

    public static String formatCurrency(String currencyAsStr) {
        String formattedCurrency = currencyAsStr;

        if (Utils.notBlank(currencyAsStr)) {
            double currency = getDoubleValue(currencyAsStr);
            formattedCurrency = formatCurrency(currency);
        }

        return formattedCurrency;
    }

    public static String formatCurrency(Double currencyToFormat) {
        if (currencyToFormat == null) return "";

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(currencyToFormat);
    }

    /**
     * Add a month to the given date
     *
     * @param cal Calendar
     * @return Calendar
     */
    public static Calendar addMonth(Calendar cal) {
        cal.add(Calendar.MONTH, 1);
        return cal;
    }

    /**
     * Add a given no. month to the given date
     *
     * @param date   Date
     * @param months int
     * @return Date
     */
    public static Date addMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * Add a given no. years to the given date
     *
     * @param date  Date
     * @param years int
     * @return Date
     */
    public static Date addYear(Date date, int years) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }

    /**
     * Add a given no. Hours to the given date
     *
     * @param date  Date
     * @param hours int
     * @return Date
     */
    public static Date addHour(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }

    /**
     * Add a given no. Hours to the given Calendar date
     *
     * @param cal   Calendar
     * @param hours int
     * @return Date
     */
    public static Calendar addHour(Calendar cal, int hours) {
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal;
    }

    /**
     * Set hour to the given Date
     *
     * @param date Date
     * @param hour int
     */
    public static void setHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
    }

    /**
     * Set Hour to the given Calendar date
     *
     * @param cal  Calendar
     * @param hour int
     */
    public static void setHour(Calendar cal, int hour) {
        cal.set(Calendar.HOUR_OF_DAY, hour);
    }


    /**
     * Gives the Day of Hour from the given date
     *
     * @param date Date
     * @return int
     */
    public static int getHour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Gives the Day of Hour from the given Calendar date
     *
     * @param cal Calendar
     * @return int
     */
    public static int getHour(Calendar cal) {
        return cal.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * Get Calendar from Date
     *
     * @param d Date
     * @return Calendar
     */
    public static Calendar getCalendar(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal;
    }

    /**
     * Get The month Name
     *
     * @param cal Calendar
     * @return String
     */
    public static String getMonthName(Calendar cal) {
        String[] monthName = {"January", "February",
                "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        return monthName[cal.get(Calendar.MONTH)];
    }

    /**
     * Get No. Of days in Month
     *
     * @param cal Calendar
     * @return int
     */
    public static int getNoOfDaysInMonth(Calendar cal) {
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get year Nomber
     *
     * @param cal Calendar
     * @return int
     */
    public static int getYear(Calendar cal) {
        return cal.get(Calendar.YEAR);
    }

    /**
     * Give the Diffrence in Dates
     *
     * @param d1 Date
     * @param d2 Date
     * @return int
     */
    public static int getDiffernceInDays(Date d1, Date d2) {
        int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
        long diffInMillis = d1.getTime() - d2.getTime();
        return (int) Math.ceil((double) diffInMillis / (double) MILLIS_IN_DAY);
    }

    /**
     * Give the Diffrence in Hours
     *
     * @param d1 Date
     * @param d2 Date
     * @return int
     */
    public static int getDiffernceInHours(Date d1, Date d2) {
        long diffInMillis = Math.abs(d1.getTime() - d2.getTime());
        return (int) Math.floor((double) diffInMillis / (1000 * 60 * 60));
    }

    /**
        * Give the Diffrence in minutes
        *
        * @param d1 Date
        * @param d2 Date
        * @return int
        */
       public static int getDiffInMinutes(Date d1, Date d2) {

        int diff = 0;

        if ( d1 != null && d2 != null){
           long diffInMillis = Math.abs(d1.getTime() - d2.getTime());
           diff =  (int) Math.floor((double) diffInMillis / (1000 * 60));
        }

        return diff;
    }


    /**
     * Give the Diffrence in Hours
     *
     * @param cal1 Calendar
     * @param cal2 Calendar
     * @return int
     */
    public static int getDiffernceInDays(Calendar cal1, Calendar cal2) {
        return getDiffernceInDays(cal1.getTime(), cal2.getTime());
    }

    /**
     * Converts the given string date in format '"MM/dd/yyyy"' to util Date
     *
     * @param s String
     * @return Date
     */
    public static Date getDate(String s) {
        Date d = null;

        try {
            DateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
            d = sf.parse(s);

        } catch (Exception e) {
            //do nothing
        }
        return d;
    }

    /**
     * Set the Calendar day month to 1
     *
     * @param cal Calendar
     */
    public static void setToFirstDayOfMonth(Calendar cal) {
        cal.set(Calendar.DATE, 1);
    }

    /**
     * Set the Calendar day of month to last day of the month
     *
     * @param cal Calendar
     */
    public static void setToLastDayOfMonth(Calendar cal) {
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DATE, lastDay);
    }

    /**
     * Returns Current Date with out time.
     *
     * @return Date
     */
    public static Date getCurrentDateOnly() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Returns Date with out time.
     *
     * @param d Date
     * @return Date
     */
    public static Date getDateOnly(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getEndOfDay(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static String trim(String s) {
        if (s == null) {
            return "";
        } else {
            return s.trim();
        }
    }

    /**
     * this function will return a Date object that represents the current date minus the months that you would like it
     * reduced by. Pass in the number of months that you would like the date to be less than the current date.
     *
     * @param months int
     * @return Date
     */
    public static Date getDateLess(int months) {
        Date currDate = new Date(System.currentTimeMillis());
        long time = currDate.getTime();
        long newTime;
        long diff = (60 * 60 * 1000);
        long diff2 = (30 * 24 * months);
        newTime = time - (diff * diff2);
        return new Date(newTime);
    }

    /**
     * Remove the Leading 0's from the long value and returns string
     *
     * @param number String
     * @return String
     */
    public static String removeLeadingZeros(String number) {
        if (number == null) {
            return null;
        }
        try {
            long l = Long.parseLong(number);
            return String.valueOf(l);
        } catch (Exception e) {
            return number;
        }
    }

    /**
     * Remove the Leading 0's from the long value and returns string
     *
     * @param number String
     * @return String
     */
    public static String removeLeadingZerosFromString(String number) {
        if (number == null) {
            return null;
        }
        try {
            int counter = 0;
            for (int i = 0; i < number.length(); i++) {
                if (number.charAt(i) == '0') {
                    counter++;
                } else {
                    break;
                }
            }
            return number.substring(counter);
        } catch (Exception e) {
            return number;
        }
    }

    public static String formatDecimal8(double dob) {
        return decimal8Format.format(dob);
    }

    public static String formatDecimal8(Double dob) {
        String s = "";
        if (dob != null) {
            return formatDecimal8(dob.doubleValue());
        }
        return s;
    }

    public static boolean isDataEqual(Integer value1, Integer value2) {
        return ((value1 == null || value1 == 0) && (value2 == null || value2 == 0)) || (value1 != null && value1.compareTo(value2) == 0);
    }

    /**
     * Returns current date in  MMddyyyy_HHmm format
     *
     * @return String
     */
    public static String getDateTimeStr() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MMddyyyy_HHmmss");
        return format.format(cal.getTime());
    }

    public static String convertCurrencyToString(Double amount) {
        StringBuffer buffer = new StringBuffer();

        if (amount != null && amount != 0) {
            if (amount < 1000) {
                buffer.append(amount);
                buffer.append("$");
            } else if (amount >= 1000 && amount < (1000 * 1000)) {
                buffer.append(Utils.roundDouble(amount / 1000));
                buffer.append("K");
            } else if (amount >= (1000 * 1000) && amount < Double.MAX_VALUE) {
                buffer.append(Utils.roundDouble(amount / (1000 * 1000)));
                buffer.append("MM");
            }
        }

        return buffer.toString();
    }

    public static String getNotNullString(String str) {
        if (str == null) {
            return "";
        }
        return str.trim();
    }


    public static Double getDoubleValue(BigDecimal value) {
        return value == null ? 0 : value.doubleValue();
    }

    public static Long getLongValue(BigDecimal value) {
        return value == null ? 0 : value.longValue();
    }

    public static BigDecimal getBigDecimalValue(Double value) {
        return value == null ? null : new BigDecimal(value);
    }

    public static Double getNonZeroDoubleValue(BigDecimal value) {
        return (value == null || value.doubleValue() == 0) ? null : value.doubleValue();
    }

    /**
     * @param milliSec long
     * @return String
     */
    public static String convertTimeToStr(long milliSec) {
        StringBuffer strValue = new StringBuffer();

        if (milliSec >= 0 && milliSec < 1000) {
            strValue.append(milliSec);
            strValue.append(" ms");
        } else if (milliSec >= 1000 && milliSec < (1000 * 60)) {
            double value = roundDouble((double) milliSec / 1000);
            strValue.append(value);
            strValue.append(" sec");
        } else if ((milliSec >= (1000 * 60))) {
            double value = roundDouble((double) milliSec / (1000 * 60));
            strValue.append(value);
            strValue.append(" min");
        }

        return strValue.toString();
    }

    /**
     * @param valueToRound double
     * @return double
     */
    public static double roundDouble(double valueToRound) {
        return (roundDouble(valueToRound, 2));
    }

    /**
     * @param valueToRound double
     * @param scale        int
     * @return double
     */
    public static double roundDouble(Double valueToRound, int scale) {
        BigDecimal bd = new BigDecimal(valueToRound);

        bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);

        return bd.doubleValue();
    }

    /**
     * @return Map<String, String>
     */
    public static Map<String, String> getSapStatusMap() {
        Map<String, String> statusMap = new HashMap<String, String>();
        statusMap.put("A", "Approved");
        statusMap.put("H", "On Hold");
        statusMap.put("L", "Loaded");
        statusMap.put("S", "Submitted");
        statusMap.put("P", "Processed");
        statusMap.put("R", "Reviewed By Planning");
        statusMap.put("X", "Deleted/Rejected");
        statusMap.put("I", "Initial Request Preparation");
        return statusMap;
    }

    /**
     * @param code String
     * @return String
     */
    public static String getSapStatus(String code) {
        return getSapStatusMap().get(code);
    }

    /**
     * Join a Array of string to a delimited String value
     *
     * @param s
     * @param delimiter
     * @return
     */
    public static String join(String[] s, String delimiter) {
        return join(Arrays.asList(s), delimiter);
    }

    /**
     * Join a list of String to a delimited String
     *
     * @param coll
     * @param delimiter
     * @return
     */
    public static String join(List<String> coll, String delimiter) {
        if (coll.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();

        for (String x : coll) {
            sb.append(x.trim()).append(delimiter);
        }

        sb.delete(sb.length() - delimiter.length(), sb.length());

        return sb.toString();
    }

    /**
     * Join a list of String to a delimited String
     *
     * @param coll
     * @param delimiter
     * @return
     */
    public static String joinLongValues(List<Long> coll, String delimiter) {
        if (coll.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();

        for (Long x : coll) {
            sb.append(x).append(delimiter);
        }

        sb.delete(sb.length() - delimiter.length(), sb.length());

        return sb.toString();
    }

    /**
     * Join a list of String to a delimited String
     *
     * @param s         String[]
     * @param delimiter String
     * @return String
     */
    public static String joinForInQuery(String[] s, String delimiter) {
        return joinForInQuery(Arrays.asList(s), delimiter);
    }

    /**
     * Join a list of String to a delimited String
     *
     * @param coll      List<String>
     * @param delimiter String
     * @return String
     */
    public static String joinForInQuery(List<String> coll, String delimiter) {
        if (coll.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();

        for (String x : coll) {
            sb.append("'").append(x.trim()).append("'").append(delimiter);
        }

        sb.delete(sb.length() - delimiter.length(), sb.length());

        return sb.toString();
    }

    public static <T> String concatForQuery(List<T> coll, String delimiter) {
        if (coll.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();

        for (T x : coll) {
            sb.append("'").append(x.toString().trim()).append("'").append(delimiter);
        }

        sb.delete(sb.length() - delimiter.length(), sb.length());

        return sb.toString();
    }

    /**
     * Breaks the given text into
     *
     * @param text      String
     * @param noOfChars int
     * @return List<String>
     */

    public static List<String> breakString(String text, int noOfChars) {
        List<String> stringList = new ArrayList<String>();
        if (text == null) {
            return null;
        }
        if (text.length() <= noOfChars) {
            stringList.add(text);
        } else {
            while (text.length() > noOfChars) {
                String chunk = text.substring(0, noOfChars);

                int lastSpace = chunk.lastIndexOf(' ');
                if (text.length() > lastSpace) {
                    chunk = text.substring(0, lastSpace + 1);
                    text = text.substring(lastSpace + 1);
                } else {
                    chunk = text.substring(0, lastSpace);
                    text = text.substring(lastSpace);
                }
                stringList.add(chunk);

            }
            if (text.length() > 0) {
                stringList.add(text);
            }
        }
        return stringList;
    }

    /**
     * @param delimitedStr String
     * @param delimiter    String
     * @return Set<String>
     */
    public static Set<String> parseString(String delimitedStr, String delimiter) {
        Set<String> parsedSet = new LinkedHashSet<String>();

        if (delimitedStr != null) {
            StringTokenizer tokens = new StringTokenizer(delimitedStr, delimiter);
            while (tokens.hasMoreTokens()) {
                String extn = tokens.nextToken();
                parsedSet.add(extn.trim());
            }
        }

        return parsedSet;
    }

    /**
     * @param strToParse String
     * @return Map<String, String>
     */
    public static Map<String, String> getMapFromString(String strToParse) {
        Map<String, String> map = new HashMap<String, String>();

        if (notBlank(strToParse)) {
            Set<String> parsedString = parseString(strToParse.trim(), ",");
            for (String str : parsedString) {
                if (notBlank(str)) {
                    String key = str.substring(0, str.indexOf("="));
                    String value = str.substring(str.indexOf("=") + 1);
                    map.put(key.trim(), value);
                }
            }
        }

        return map;
    }

    /**
     * @param attributeMap Map<String, String>
     * @return String
     */
    public static String getStringFromMap(Map<String, String> attributeMap) {
        StringBuilder parsedString = new StringBuilder();

        if (attributeMap != null && !attributeMap.isEmpty()) {
            Set<String> keys = attributeMap.keySet();

            for (Iterator iter = keys.iterator(); iter.hasNext();) {
                String key = (String) iter.next();
                String value = attributeMap.get(key);
                if (notBlank(value) && notBlank(key)) { // Add it to the map if the value is not NULL or not empty string.  In essense, remove from the map.
                    parsedString.append(key);
                    parsedString.append("=");
                    parsedString.append(value);
                    if (iter.hasNext()) {
                        parsedString.append(",");
                    }
                }
            }
        }

        return parsedString.toString();
    }

    public static void removeDuplicate(List arlList) {
        HashSet h = new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
    }

    /**
     *
     *
     */
    public static <T> Collection<T> filterOutSoftDeletedEntities(Collection<T> set) {

        List<T> deletedEntities = null;

        if (deletedEntities == null) {
            deletedEntities = new ArrayList<T>();
        }

        Method decMeth = null;

        for (T t : set) {
            try {
                if (decMeth == null) {
                    decMeth = t.getClass().getDeclaredMethod("getDeleteInd");
                }

                if ("Y".equalsIgnoreCase((String) decMeth.invoke(t))) {
                    deletedEntities.add(t);
                }
            } catch (IllegalAccessException e) {
                log.error(e);
            } catch (NoSuchMethodException e) {
                log.error(e);
            } catch (InvocationTargetException e) {
                log.error(e);
            }
        }

        for (T d : deletedEntities) {
            set.remove(d);
        }

        deletedEntities.clear();

        return set;
    }

    public static boolean isCollectionEmpty(Collection<?> col) {
        return col == null || col.size() <= 0;
    }

    public static boolean isEmpty(Collection<?> list) {

        return list == null || list.size() <= 0;

    }

    public static boolean notEmpty(Collection<?> list) {

        return !isEmpty(list);

    }

    public static String formatAmountToNumber(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c >= '0' && c <= '9') {
                sb.append(c);
            }
        }
        return sb.toString();
    }    
     
//    public static String writePdfFile(byte[] data, String fileName) throws JfwException {
//        String filePath = ElmDBConfig.getProperty("file.temp.dir");
//        if (data == null || data.length <= 0) {
//            throw new JfwException("No data passed in for writing file!");
//        }
//        else
//        if(Utils.isBlank(filePath)){
//            throw new JfwException("No path to write file to!");
//        }
//        else
//        if(Utils.isBlank(fileName)){
//            throw new JfwException("No file name specified!");
//        }
//
//        fileName += "_" + System.currentTimeMillis() + ".pdf";
//
//        File file = new File(fileName);
//        while (file.exists()) {
//            fileName += "_" + System.currentTimeMillis() + ".pdf";
//            file = new File(fileName);
//        }
//
//        boolean dirMade = true;
//        File fileDir = new File(filePath);
//        if (!fileDir.exists()) {
//            dirMade = fileDir.mkdir();
//        }
//
////        if(dirMade){
//            try {
//                FileOutputStream outStream = new FileOutputStream(fileName);
//                outStream.write(data);
//                outStream.flush();
//                outStream.close();
//            } catch (IOException e) {
//                log.error("io exception:" + e.getMessage(), e);
//            } catch (Exception e) {
//                log.error("exception:" + e.getMessage(), e);
//            }
////        }
//
//        deleteFiles(filePath);
//
//        return fileName;
//    }
     
         public static void closeInputStream(InputStream in) {
        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {

        }
    }

    /**
     * Close OutputStream
     *
     * @param out
     */
    public static void closeOutputStream(OutputStream out) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {

        }
    }
    
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void removeCookie(HttpServletResponse response, String name) {
        addCookie(response, name, null, 0);
    }
}