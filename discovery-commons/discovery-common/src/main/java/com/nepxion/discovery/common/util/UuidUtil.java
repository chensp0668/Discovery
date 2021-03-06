package com.nepxion.discovery.common.util;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

public class UuidUtil {
    public static final Map<Integer, Integer> RANDOM_MAP = new HashMap<Integer, Integer>() {
        private static final long serialVersionUID = 9074559847984842939L;
        {
            put(999, 3);
            put(9999, 4);
            put(99999, 5);
        }
    };

    public static final int[] RANDOM_ARRAY = { 9999, 999, 999 };

    public static String getTimeUUID() {
        return getTimeUUID(null);
    }

    public static String getTimeUUID(String prefix) {
        return getTimeUUID(prefix, RANDOM_ARRAY);
    }

    public static String getTimeUUID(String prefix, int[] randomArray) {
        return getTimeUUID(prefix, "yyyyMMdd-HHmmss-SSS", randomArray);
    }

    public static String getTimeUUID(String prefix, String dataFormat) {
        return getTimeUUID(prefix, dataFormat, RANDOM_ARRAY);
    }

    public static String getTimeUUID(String prefix, String dataFormat, int[] randomArray) {
        if (dataFormat == null) {
            throw new IllegalArgumentException("dataFormat can't be null");
        }

        if (randomArray == null) {
            throw new IllegalArgumentException("randomArray can't be null");
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(prefix)) {
            stringBuilder.append(prefix).append("-");
        }

        stringBuilder.append(new SimpleDateFormat(dataFormat).format(new Date())).append("-");

        for (int random : randomArray) {
            String result = random(random, String.valueOf(random).length());

            stringBuilder.append(result).append("-");
        }

        if (stringBuilder.length() > 0) {
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    public static String random(int random, int padSize) {
        return StringUtils.leftPad(String.valueOf(RandomUtils.nextInt(0, random)), padSize, "0");
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
}