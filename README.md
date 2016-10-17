# Modern application metrics monitoring with JMX

A simple mix and aggregation of JMX, Jolokia, Telegraf, InfluxDB and Grafana tools, for Spring Boot and Actuator metrics monitoring.

## Background

Spring Boot Actuator has no out-of-box tooling for near real-time monitoring charts of gauges and counters metrics at `/metrics` HTTP endpoint. The official documentation current (1.4.1) offers some alternatives to export data in a [custom way for different platforms](http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-metrics.html), but one have to implement by their own the data exportation and parsing.

On the other hand, Spring Boot [recommends you to use Jolokia] (http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#production-ready-jolokia) as a managed dependency, to expose internal's JMX MBeans through HTTP. This gives us the required implementation to explore and inspect performance metrics.

Additionally, [Spring Boot Admin] (https://github.com/codecentric/spring-boot-admin) offers JMX MBeans inspection support and you can use it to get some data snapshots about JMX use, but it is individual instance oriented. In a clustered Spring Boot environment, getting individual and isolated data from hosts may not be productive neither conclusive.

NewRelic and other APM tools gives the same result as we are trying to find out here, by instrumenting code with JVM layers, but data retention and long time-window may be a problem (if you need to inspect performance in terms of seconds or if you have to host old date data.

Others initiatives with the same goal, and custom code:

- https://github.com/sensu-plugins/sensu-plugins-springboot
- https://github.com/jimmidyson/spring-boot-prometheus

Just for consideration, installing and using StatsD and Graphite grants several hours of work, leaving out some great productivity-friendly alternatives, as Docker and Docker Compose tooling does.

## Main goal
Getting Spring Boot Actuator monitoring for self-service dashboards and chats, with modern and on-premise operations tooling, by using only out-of-box Spring Boot features and integrations, without any custom code.

This repository is a proposal to enable Spring Boot monitoring without 3rd-party problems of data retention and transformation, as NewRelic or Datadog does.

The result is achieved by the the flow: 

- Spring Boot get exposed by Actuator
- Jolokia exposes MBeans through HTTP endpoint
- Telegraf pull Joloklia endpoints, fetching data and send it to InfluxDB
- InfluxDB stores time-series data
- Grafana connects to InfluxDB instance
- Grafana draws beautiful monitoring dashboards.

## Solution

1. Configured your Spring Boot instances with [Jolokia](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#production-ready-jolokia) dependency

2. Configure an instance of [Telegraf](https://github.com/influxdata/telegraf) tool for collecting metric along side with you application module instance. We do recommend installing one Telegraf instance per application node, making it isolated, since Telegraf requires the IP assigned to application instance. Using a central point of Telegraf, in a dynamic and provisioned environment may cause data interruption (since you have to updates the monitored IPs). Telegraf uses ~10mb of memory per instance, giving a small footprint to monitor applications instances.

3. Configure [Telegraf input](https://github.com/influxdata/telegraf/tree/master/plugins/inputs/jolokia) plugin for Jolokia

4. Configure [Telegraf output](https://github.com/influxdata/telegraf/tree/master/plugins/outputs/influxdb) plugin for InfluxDB.

5. Configure an instance of InfluxDB for data ingestion and storage.

6. Configure an instance of Grafana and a Datasource for data collecting.

7. Create your beautiful dashboards.

## This repo
This repository was organized as an example of a configuration with all tools for just monitoring, based on docker-compose structure.

`orderfy-config/telegraf.conf`: is an example of monitoring applications modules from [Orderfy](https://github.com/brunosimioni/orderfy) repository, but can be used with any Spring Boot Jolokia-powered repository.
