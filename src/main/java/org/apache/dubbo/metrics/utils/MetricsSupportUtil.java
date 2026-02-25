package org.apache.dubbo.metrics.utils;

import org.apache.dubbo.common.utils.ClassUtils;

public class MetricsSupportUtil {

    private MetricsSupportUtil() {
    }

    public static boolean isSupportMetrics() {
        return isClassPresent("io.micrometer.core.instrument.MeterRegistry");
    }

    public static boolean isSupportPrometheus() {
        return isClassPresent("io.micrometer.prometheus.PrometheusConfig")
                || isClassPresent("io.micrometer.prometheusmetrics.PrometheusConfig");
    }

    private static boolean isClassPresent(String className) {
        return ClassUtils.isPresent(className);
    }
}
