package com.lin.mydream.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtil {

    public static void info(String msg, Object... format) {
        if (log.isInfoEnabled()) {
            log.info(msg, format);
        }
    }

    public static void warn(String msg, Object... format) {
        if (log.isWarnEnabled()) {
            log.warn(msg, format);
        }
    }

    public static void error(String msg, Object... format) {
        if (log.isErrorEnabled()) {
            log.error(msg, format);
        }
    }

}
