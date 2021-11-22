package com.lin.mydream.util;

import com.google.common.base.Splitter;
import com.lin.mydream.consts.MydreamException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.helpers.MessageFormatter;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    /**
     * format占位替换，{} -> param
     */
    public static String format(String format, Object... params) {
        return MessageFormatter.arrayFormat(format, params).getMessage();
    }

    public static String trimLeading(String str) {
        return org.springframework.util.StringUtils.trimLeadingWhitespace(str);
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
        if (days <= 365L) {
            return days + "天";
        } else {
            if (days % 365 == 0) {
                return (days / 365) + "周年";
            } else {
                return days + "天";
            }
        }
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getDistanceOfTwoDate(DateUtils.parseDate("2021-02-14 00:00:00", "yyyy-MM-dd HH:mm:ss"), new Date()));
        System.out.println(getDistanceOfTwoDate(DateUtils.parseDate("2021-11-01 00:00:00", "yyyy-MM-dd HH:mm:ss"), new Date()));
    }

}
