#!/usr/bin/env bash
kubectl apply -f innodb-cluster/volume/mysql-claim.yaml
kubectl apply -f innodb-cluster/configmap/mysql-configmap.yaml
kubectl apply -f innodb-cluster/service/mysql-local-service.yaml
kubectl apply -f innodb-cluster/service/mysql-web-service.yaml
kubectl apply -f innodb-cluster/controller/mysql-server.yaml