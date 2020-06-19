#!/usr/bin/env bash
kubectl delete -f shell/mysql-shell.yaml
kubectl delete configmap mysql-shell-start-script