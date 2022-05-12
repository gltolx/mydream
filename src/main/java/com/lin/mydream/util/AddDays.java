package com.lin.mydream.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2022/4/12.
 * @desc TODO 详细描述一下这个方法怎么调
 */
@Slf4j
public class AddDays { // TODO AddDays
    /**
     * 日期转换函数，按天加减
     *
     * @param date    输入日期
     * @param days    日期加减天数，负数代表减
     * @param pattern 输入日期的格式，TODO 日期格式需要举例：yyyyMMdd、yyyy-MM-dd HH:mm:ss
     * @return 输出日期
     */
    public String eval(String date, int days, String pattern) {
        String outputDate = "";
        try {
            if (StringUtils.isBlank(date)) {
                // TODO 使用RuntimeException抛异常
                throw new RuntimeException(String.format("The field 'date' [%s] you entered invalid", date));
            }
            if (StringUtils.isBlank(pattern)) {
                throw new RuntimeException(String.format("The field 'pattern' [%s] you entered invalid", pattern));
            }
            // 对date按pattern形式转成JavaDate
            Date _date = DateUtils.parseDate(date, pattern);
            // 执行日期加减，出参也是JavaDate
            Date _outputDate = DateUtils.addDays(_date, days);
            outputDate = DateFormatUtils.format(_outputDate, pattern);
        } catch (Exception e) {
            // TODO 打印异常日志
            log.error("AddDays error", e);
        }
        return outputDate;
    }

    public static void main(String[] args) {
        new AddDays().eval("2020-04=29", 1, "yyyy-MM=dd HH:mm;ss");
    }

}
