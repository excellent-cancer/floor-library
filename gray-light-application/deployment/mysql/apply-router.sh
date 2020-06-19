#!/usr/bin/env bash
kubectl apply -f router/service/mysql-router-service.yaml
kubectl apply -f router/controller/mysql-router-server.yaml