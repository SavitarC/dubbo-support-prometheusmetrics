# Dubbo + Spring Boot + Prometheus Metrics Demo

这是一个基于以下依赖的最小可运行示例：

- Dubbo `3.3.6`
- Spring Boot `3.5.10`
- `dubbo-observability-spring-boot-starter`
- `spring-boot-starter-actuator`
- `micrometer-registry-prometheus`

## 运行

```bash
mvn spring-boot:run
```

应用启动后监听端口 `18180`。

## 验证 Prometheus 指标输出

```bash
curl -s http://127.0.0.1:18180/metrics | grep "^dubbo_" | sort
```

如果启动成功，这个命令会输出 Dubbo 暴露的 Prometheus 指标（以 `dubbo_` 前缀开头）。

## 其他接口

```bash
curl "http://127.0.0.1:8080/ping?name=world"
```

返回示例：

```text
Hello, world
```


## 兼容 Prometheus 新客户端

当前示例内置了对 Micrometer Prometheus 1.x（`io.micrometer.prometheusmetrics.*`）包名的兼容处理，避免 `dubbo.metrics.protocol=prometheus` 时因旧类名探测失败而降级为 NOP reporter。
