#!/usr/bin/env bash
kubectl delete -f router/controller/mysql-router-server.yaml
kubectl delete -f router/service/mysql-router-service.yaml
