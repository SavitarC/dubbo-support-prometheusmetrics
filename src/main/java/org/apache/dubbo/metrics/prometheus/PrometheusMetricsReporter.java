package org.apache.dubbo.metrics.prometheus;

import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.metrics.report.AbstractMetricsReporter;
import org.apache.dubbo.rpc.model.ApplicationModel;

public class PrometheusMetricsReporter extends AbstractMetricsReporter {

    private final PrometheusMeterRegistry prometheusRegistry;

    public PrometheusMetricsReporter(URL url, ApplicationModel applicationModel) {
        super(url, applicationModel);
        this.prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Override
    protected void doInit() {
        addMeterRegistry(prometheusRegistry);
    }

    @Override
    public String getResponse() {
        return prometheusRegistry.scrape();
    }

    @Override
    protected void doDestroy() {
        // no-op
    }
}
