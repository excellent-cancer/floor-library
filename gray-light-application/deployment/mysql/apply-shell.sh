#!/usr/bin/env bash
kubectl create configmap mysql-shell-start-script --from-file=shell/mysqlshrc.js
kubectl apply -f shell/mysql-shell.yaml