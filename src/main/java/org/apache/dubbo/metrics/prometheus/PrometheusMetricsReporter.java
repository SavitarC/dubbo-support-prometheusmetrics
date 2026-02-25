package org.apache.dubbo.metrics.prometheus;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.metrics.report.AbstractMetricsReporter;
import org.apache.dubbo.rpc.model.ApplicationModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PrometheusMetricsReporter extends AbstractMetricsReporter {

    private static final String NEW_PROMETHEUS_CONFIG = "io.micrometer.prometheusmetrics.PrometheusConfig";
    private static final String NEW_PROMETHEUS_REGISTRY = "io.micrometer.prometheusmetrics.PrometheusMeterRegistry";
    private static final String LEGACY_PROMETHEUS_CONFIG = "io.micrometer.prometheus.PrometheusConfig";
    private static final String LEGACY_PROMETHEUS_REGISTRY = "io.micrometer.prometheus.PrometheusMeterRegistry";

    private final MeterRegistry prometheusRegistry;
    private final Method scrapeMethod;

    public PrometheusMetricsReporter(URL url, ApplicationModel applicationModel) {
        super(url, applicationModel);

        RegistryHandle registryHandle = createRegistry();
        this.prometheusRegistry = registryHandle.registry;
        this.scrapeMethod = registryHandle.scrapeMethod;
    }

    @Override
    protected void doInit() {
        addMeterRegistry(prometheusRegistry);
    }

    @Override
    public String getResponse() {
        try {
            return (String) scrapeMethod.invoke(prometheusRegistry);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to scrape prometheus metrics", e);
        }
    }

    @Override
    protected void doDestroy() {
        // no-op
    }

    private static RegistryHandle createRegistry() {
        RegistryHandle registryHandle = tryCreateRegistry(NEW_PROMETHEUS_CONFIG, NEW_PROMETHEUS_REGISTRY);
        if (registryHandle != null) {
            return registryHandle;
        }

        registryHandle = tryCreateRegistry(LEGACY_PROMETHEUS_CONFIG, LEGACY_PROMETHEUS_REGISTRY);
        if (registryHandle != null) {
            return registryHandle;
        }

        throw new IllegalStateException("No compatible Micrometer Prometheus implementation found");
    }

    private static RegistryHandle tryCreateRegistry(String configClassName, String registryClassName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> configClass = Class.forName(configClassName, true, classLoader);
            Class<?> registryClass = Class.forName(registryClassName, true, classLoader);

            Field defaultConfigField = configClass.getField("DEFAULT");
            Object defaultConfig = defaultConfigField.get(null);

            Object registry = registryClass.getConstructor(configClass).newInstance(defaultConfig);
            Method scrapeMethod = registryClass.getMethod("scrape");
            return new RegistryHandle((MeterRegistry) registry, scrapeMethod);
        } catch (ClassNotFoundException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize PrometheusMeterRegistry for class " + registryClassName, e);
        }
    }

    private static class RegistryHandle {
        private final MeterRegistry registry;
        private final Method scrapeMethod;

        private RegistryHandle(MeterRegistry registry, Method scrapeMethod) {
            this.registry = registry;
            this.scrapeMethod = scrapeMethod;
        }
    }
}
