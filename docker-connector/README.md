# Scraping `/metrics` actuator endpoints, without JMX-HTTP consumption.

Telegraf already have a generic plugin for HTTP scrapping, and, since Actuator deliver the internal MBean by `/metrics` endpoint, you can scrap that information directly, without Jolokia.

This Maven project is an effort to connect to a Docker Swarm cluster, grabbing forwarded applications, and monitoring then, without touching internal containers applications.

This approach, differently from setting a single instance of Telegraf for each application node, stands a single point of failure, since it will generate all configuration needed to a node of Telegraf connect to each individual application node. If Telegraf instance goes down, every metric will stop being collected.
