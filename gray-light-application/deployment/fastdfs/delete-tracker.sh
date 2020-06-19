#!/usr/bin/env bash
kubectl delete -f tracker/controller/fastdfs-tracker.yaml
kubectl delete -f tracker/volume/fastdfs-tracker-claim.yaml
kubectl delete -f tracker/service/fastdfs-tracker-service.yaml
kubectl delete -f tracker/service/fastdfs-web-service.yaml
