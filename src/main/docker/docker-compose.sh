#! /bin/bash

# redis-slave服务启动两个容器，redis-sentinel启动3个容器，即启动redis启动一主两从三个哨兵
docker-compose up -d --scale redis-slave=2 --scale redis-sentinel=3