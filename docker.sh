#!/bin/bash

docker build -t private-docker-registry.launchblock.dev/api-public:latest .

docker push private-docker-registry.launchblock.dev/api-public