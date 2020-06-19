#!/usr/bin/env bash
kubectl apply -f storage/service/fastdfs-storage-service.yaml
kubectl apply -f storage/volume/fastdfs-storage-claim.yaml
kubectl apply -f storage/controller/fastdfs-storage.yaml