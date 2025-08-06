#!/bin/bash

# Exit on error
set -e

# Get short Git commit hash
GIT_TAG=$(git rev-parse --short HEAD)

# Build Docker image
docker build -t project-service:$GIT_TAG .

# Tag Docker image for Docker Hub
docker tag project-service:$GIT_TAG ujjwalraghuvanshi1212/project-service:$GIT_TAG

# Push to Docker Hub
docker push ujjwalraghuvanshi1212/project-service:$GIT_TAG

echo ""
read -p "Script execution complete. Press Enter to exit..."