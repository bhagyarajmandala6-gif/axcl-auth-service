package com.innocito.axcl.util;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

public class MdcAware {

    public static Runnable wrap(Runnable task) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            if (contextMap != null) MDC.setContextMap(contextMap);
            try {
                task.run();
            } finally {
                MDC.clear();
            }
        };
    }

    public static <V> Callable<V> wrap(Callable<V> task) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            if (contextMap != null) MDC.setContextMap(contextMap);
            try {
                return task.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static <T> Consumer<T> wrap(Consumer<T> consumer) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return t -> {
            if (contextMap != null) MDC.setContextMap(contextMap);
            try {
                consumer.accept(t);
            } finally {
                MDC.clear();
            }
        };
    }

    public static <T, R> Function<T, R> wrapFunction(Function<T, R> function) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return t -> {
            if (contextMap != null) MDC.setContextMap(contextMap);
            try {
                return function.apply(t);
            } finally {
                MDC.clear();
            }
        };
    }
}
