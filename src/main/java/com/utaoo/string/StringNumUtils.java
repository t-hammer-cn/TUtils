package com.utaoo.string;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * string and number utils
 */
public class StringNumUtils {
    /**
     * get all char of number  from string
     *
     * @param str any string
     * @return number in string
     */
    public static String getNumber(String str) {
        String res = null;
        if (StringUtils.isNotBlank(str)) {
            str = str.trim();
            Pattern p = Pattern.compile("\\d{1}");
            Matcher m = p.matcher(str);
            while (m.find()) {
                res += m.group();
            }
        }
        return res;
    }
}
