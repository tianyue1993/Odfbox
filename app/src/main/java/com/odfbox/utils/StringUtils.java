package com.odfbox.utils;


/**
 * @author etc
 * @Description: 字符串处理类
 * @date 2 Apr, 2016 10:56:09 AM
 */
public class StringUtils {

    /**
     * is null or its length is 0
     * <p>
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    public static boolean isNotEmptyOrNull(String str) {
        if (str.length() == 0) {
            return false;
        } else {
            return true;
        }

    }
}
