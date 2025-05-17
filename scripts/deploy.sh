#!/bin/bash

# Get the directory of this script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SERVICE_ROOT="$(dirname "$SCRIPT_DIR")"

# Run docker build and push
"$SCRIPT_DIR/docker.sh"

# Deploy to kubernetes
kubectl apply -f "$SERVICE_ROOT/kubernetes-deployment.yaml"

# Rollout restart to ensure changes are picked up
kubectl rollout restart deployment api-public -n launchblock-services
