# Wiki-nematics 

Data processing pipeline that analyze wikipedia stream in real time 

![dashboard](image.png)

## Install

```
./launcher
```

## Docker
enter container
```
docker exec --workdir /opt/kafka/bin/ -it broker sh
```
consult logs 
```
docker logs flink-taskmanager
```
more info : [Docker Kakfa Quick Start](https://hub.docker.com/r/apache/kafka#quick-start)

Connect to Job Dashboard : [localhost:8081](http://localhost:8081) 
Connect to Grafana : [localhost:3000](http://localhost:3000) 

