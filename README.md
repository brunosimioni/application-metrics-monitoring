# Modern application metrics monitoring with JMX

A simple mix and aggregation of JMX, Jolokia, Telegraf, InfluxDB and Grafana tools for Spring Boot and Actuator metrics monitoring.

## Background

Spring Boot Actuator has no out-of-box tooling for gauges and counters monitoring in near realtime monitoring.  The official documentation offers some alternatives to export data in a custom way for different platforms. Check it [here](http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-metrics.html).

On the other hand, Spring Boot [recomends you to use Jolokia] (http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#production-ready-jolokia) as a managed dependency to expose internal's JMX MBeans through HTTP.

Additionally, [Spring Boot Admin] (https://github.com/codecentric/spring-boot-admin) offers JMX MBeans support and you can use it to get some data snapshots about JMX use, but it is individual instance oriented. In a clustered environment, getting individual and isolated data from hosts may not be productive.

## Main goal
Getting persistent monitoting data, by using some time-series operations tool lacks explanations and modern alternatives.

Installing and using StatsD and Graphite grants several hours of work, leaving out some great productivity-friendly alternatives, as Docker and Docker Compose tooling does.

This repository is a proposal to enable Spring Boot monitoring without 3rd-party problems of data retention and transformation, as NewRelic or Datadog does.

## Solution

1. Configured your Spring Boot instaces with Jolokia dependency

2. Configure an instance of Telegraf tool for collecting metric. We do recommend installing one Telegraf instance per application node, making it isolated, since Telegraf requires the IP assigned to application instance. Using a central point of Telegraf, in a dynamic and provisioned environment may cause data interruption (since you have to updates the monitored IPs). Telegraf uses ~10mb of memory per instance, giving a small footprint to monitor applications instances.

3. Configure Telegraf input plugin for Jolokia

4. Configure Telegraf output plugin for InfluxDB.

5. Configure an instance of InfluxDB for data ingestion and storage.

6. Configure an instance of Grafana and a Datasource for data collecting.

7. Create your beautiful dashboards.
