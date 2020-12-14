package com.carelee.util;

/**
 * Package: com.carelee.util
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 15:43
 * Copyright: Copyright (c) 2040
 */
public class StringUtils {
    public static boolean notEmpty(String str) {
        return null != str && str.length() >= 0;
    }

    public static String getStr(Object o){
        return String.valueOf(o);
    }
}
