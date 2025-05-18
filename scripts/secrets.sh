#!/bin/bash

# Check if kubeseal is installed
if ! command -v kubeseal &> /dev/null; then
    echo "Error: kubeseal is not installed. Please install it first."
    exit 1
fi

# Check if kubectl is installed and can access the cluster
if ! kubectl cluster-info &> /dev/null; then
    echo "Error: kubectl is not installed or cannot access the cluster."
    exit 1
fi

API_TOKEN_SECRET_KEY=$(LC_ALL=C tr -dc 'A-Za-z0-9!@#$%^&*()_+[]{}|;:,.<>?/' < /dev/urandom | head -c 32)

# Create Registry Secrets
cat > temp-public-secrets.yaml << EOF
apiVersion: v1
kind: Secret
metadata:
  name: api-public-secrets
  namespace: launchblock-services
type: Opaque
stringData:
  API_TOKEN_SECRET_KEY: $API_TOKEN_SECRET_KEY
EOF

# Create sealed secrets
kubeseal --format yaml < temp-public-secrets.yaml > sealed-public-secrets.yaml

# Apply the sealed secrets
kubectl apply -f sealed-public-secrets.yaml

# Clean up temporary files
rm temp-public-secrets.yaml

echo "API public secrets have been created and applied successfully!"