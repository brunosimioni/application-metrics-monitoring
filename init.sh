echo "initializing docker images"
docker-compose up -d
echo "configuring grafana datasource"
docker-compose exec grafana sh /etc/grafana/scripts/grafana-conf.cmd
