package com.github.ctaras.util;

import org.slf4j.Logger;

import java.util.function.Supplier;

public class Log {

    public static void d(Logger logger, Supplier<String> s) {
        if (logger.isDebugEnabled()) {
            logger.debug(s.get());
        }
    }

    public static void i(Logger logger, Supplier<String> s) {
        if (logger.isInfoEnabled()) {
            logger.info(s.get());
        }
    }

    public static void w(Logger logger, Supplier<String> s) {
        if (logger.isWarnEnabled()) {
            logger.warn(s.get());
        }
    }

    public static void e(Logger logger, Supplier<String> s) {
        if (logger.isErrorEnabled()) {
            logger.error(s.get());
        }
    }

    public static void e(Logger logger, Supplier<String> s, Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error(s.get(), t);
        }
    }
}
