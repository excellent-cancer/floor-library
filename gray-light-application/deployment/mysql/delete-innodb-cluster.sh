#!/usr/bin/env bash
kubectl delete -f innodb-cluster/controller/mysql-server.yaml
kubectl delete -f innodb-cluster/service/mysql-web-service.yaml
kubectl delete -f innodb-cluster/service/mysql-local-service.yaml
kubectl delete -f innodb-cluster/configmap/mysql-configmap.yaml
kubectl delete -f innodb-cluster/volume/mysql-claim.yaml