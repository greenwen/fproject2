package com.icia.fproject.vrp.util;

public class IfUtil {
    public static <T> T nvl(T target, T defaultValue) {
        if (target == null) {
            return defaultValue;
        } else {
            return target;
        }
    }

    public static String evl(String target, String defaultValue) {
        if (target == null || target.trim().isEmpty()) {
            return defaultValue;
        } else {
            return target;
        }
    }
}

