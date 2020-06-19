#!/usr/bin/env bash
kubectl delete -f storage/controller/fastdfs-storage.yaml
kubectl delete -f storage/service/fastdfs-storage-service.yaml
kubectl delete -f storage/volume/fastdfs-storage-claim.yaml