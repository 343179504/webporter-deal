package com.suarez.webporter.util;

public class PointUtil {
    public static String dealPoint(String point) {
        String realPoint = point;
        if (point.contains(".25") || point.contains(".75")) {
            double pointDouble = Double.parseDouble(point);
            String sm_point = getDoubleString(pointDouble - 0.25);
            String big_point = getDoubleString(pointDouble + 0.25);
            realPoint = sm_point + "/" + big_point;
        }
        return realPoint;
    }

    public static String changePoint(String point) {
        String realPoint = point;
        if (point.contains("/")) {
            String partTwo = point.split("/")[1];
            realPoint = getDoubleString(Double.parseDouble(partTwo) - 0.25);
        }
        return realPoint;
    }

    /**
     * 获取下区间点
     *
     * @param point 当前区间点
     * @return 下区间点
     */
    public static String getPointLower(String point) {
        return getDoubleString(Double.parseDouble(point) - 0.25);
    }

    /**
     * 获取上区间点
     *
     * @param point 当前区间点
     * @return 下区间点
     */
    public static String getPointUpper(String point) {
        return getDoubleString(Double.parseDouble(point) + 0.25);
    }

    /*
     * 如果是小数，保留两位，非小数，保留整数
     * @param number
     */
    public static String getDoubleString(double number) {
        String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            //如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            numberStr = String.valueOf(number);
        }
        return numberStr;
    }
}
