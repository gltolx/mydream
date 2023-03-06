package com.lin.mydream.util;

import com.google.common.base.Splitter;
import com.lin.mydream.consts.MydreamException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/11.
 * @desc 通用工具
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtil {

    public static void asserts(boolean b, Object param) {
        if (!b) {
            throw MydreamException.of("target parameter invalid: {}", param);
        }
    }

    public static <T> void asserts(T t, Predicate<T> predicate) {
        asserts(predicate.test(t), t);
    }

    /**
     * 断言
     */
    public static <T> void asserts(T t, Predicate<T> predicate, String message, Object... formats) {
        if (!predicate.test(t)) {
            throw MydreamException.of(message, formats);
        }
    }

    /**
     * 否定
     */
    public static <T> void negative(T t, Predicate<T> predicate, String message, Object... formats) {
        if (predicate.test(t)) {
            throw MydreamException.of(message, formats);
        }
    }


    public static <T> List<T> orEmpty(Supplier<List<T>> supplier) {
        return Optional.ofNullable(supplier)
                .map(Supplier::get)
                .orElse(Collections.emptyList());
    }

    public static <T> String orEmptyString(Supplier<String> supplier) {
        return Optional.ofNullable(supplier)
                .map(Supplier::get)
                .orElse(StringUtils.EMPTY);
    }

    public static <T> T orNull(Supplier<T> supplier) {
        return Optional.ofNullable(supplier)
                .map(Supplier::get)
                .orElse(null);
    }

    public static <T> T or(Supplier<T> supplier, T defaultVal) {
        return Optional.ofNullable(supplier)
                .map(Supplier::get)
                .orElse(defaultVal);
    }

    /**
     * format占位替换，{} -> param
     */
    public static String format(String format, Object... params) {
        return MessageFormatter.arrayFormat(format, params).getMessage();
    }

    public static String trimLeading(String str) {
        return org.springframework.util.StringUtils.trimLeadingWhitespace(str);
    }

    public static String unicodeToString2(String unicodeStr) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader("key=" + unicodeStr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("key");
    }

    /**
     * unicode码转为可读字符串
     */
    public static String unicodeToString(String unicodeStr) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < unicodeStr.length()) {
            char c = unicodeStr.charAt(i);
            if (c == '\\' && i + 1 < unicodeStr.length() && unicodeStr.charAt(i + 1) == 'u') {
                String unicode = unicodeStr.substring(i + 2, i + 6);
                char ch = (char) Integer.parseInt(unicode, 16);
                sb.append(ch);
                i += 6;
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }

    /**
     * 字符串转为unicode编码
     */
    public static String stringToUnicode(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int codePoint = str.codePointAt(i);
            String hex = Integer.toHexString(codePoint);
            sb.append("\\u").append(hex);
            if (Character.isSurrogate((char) codePoint)) {
                i++;
            }
        }
        return sb.toString();
    }


    /**
     * 命令规则是 ... - ...
     *
     * @param input 输入的命令
     * @return key: 命令头 value: 命令内容
     */
    public static Pair<String, String> parseCommand(String input) {
        if (input.contains(" - ")) {
            List<String> list = Splitter.on(" - ").omitEmptyStrings().splitToList(input);
            if (list.size() == 0) {
                throw MydreamException.of("invalid command input:{}", input);
            } else if (list.size() == 1) {
                return Pair.of(trimLeading(list.get(0)), StringUtils.EMPTY);
            } else {
                return Pair.of(trimLeading(list.get(0)), trimLeading(list.get(1)));
            }

        } else {
            return Pair.of(trimLeading(input), StringUtils.EMPTY);
        }
    }


    public static long getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    public static String transferDays(long days) {
        long d = Math.abs(days);
        if (d <= 365L) {
            return d + "天";
        } else {
            if (d % 365 == 0) {
                return (d / 365) + "周年";
            } else {
                return d + "天";
            }
        }
    }

    public static Integer tryParseInteger(String str) {
        return tryParseInteger(str, NumberUtils.INTEGER_ZERO);
    }

    public static Integer tryParseInteger(String str, Integer def) {
        if (StringUtils.isEmpty(str)) {
            return def;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ignore) {
        }
        return def;
    }

    public static Long tryParseLong(String str) {
        return tryParseLong(str, NumberUtils.LONG_ZERO);
    }

    /**
     * 尝试将给定字符串转为Long型, 成功则返回该long值, 失败则返回默认值
     *
     * @param str          给定字符串
     * @param defaultValue 默认值
     */
    public static Long tryParseLong(String str, Long defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException ignore) {
        }
        return defaultValue;
    }

    public static void main(String[] args) throws ParseException {
//        Pair<String, String> pair = parseCommand("create remember - 'feY测试' '2022-02-14' '17826833386,13639853155'");
    }

}
