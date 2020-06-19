#!/usr/bin/env bash
kubectl apply -f tracker/service/fastdfs-tracker-service.yaml
kubectl apply -f tracker/service/fastdfs-web-service.yaml
kubectl apply -f tracker/volume/fastdfs-tracker-claim.yaml
kubectl apply -f tracker/controller/fastdfs-tracker.yaml