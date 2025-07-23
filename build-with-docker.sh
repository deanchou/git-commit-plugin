#!/bin/bash

# Ensure script stops execution on error
set -e

# Color definitions
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Starting to build using Docker...${NC}"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Error: Docker not found. Please install Docker first: https://docs.docker.com/get-docker/${NC}"
    exit 1
fi

# Check if docker-compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}Error: docker-compose not found. Please install docker-compose first: https://docs.docker.com/compose/install/${NC}"
    exit 1
fi

# Check if Docker service is running
docker info > /dev/null 2>&1 || { echo -e "${RED}Error: Docker service is not running. Please start Docker service and try again.${NC}"; exit 1; }

# Create build directory (if it doesn't exist)
mkdir -p build

# Clean previous build (if exists)
if [ -f "build/libs/git-commit-helper-1.0-SNAPSHOT.jar" ]; then
    echo -e "${YELLOW}Cleaning previous build...${NC}"
    rm -f build/libs/*
    rm -f build/distributions/*
fi

# Build plugin using docker-compose
echo -e "${YELLOW}Building...${NC}"
echo -e "${YELLOW}This may take a few minutes, depending on your network speed and computer performance.${NC}"
echo -e "${YELLOW}First build requires downloading Docker images and Gradle dependencies, which may be slow.${NC}"

# Build plugin using docker-compose
docker-compose up --build

# Check if build was successful
if [ -f "build/libs/git-commit-helper-1.0-SNAPSHOT.jar" ]; then
    echo -e "${GREEN}Build successful!${NC}"
    
    # Display file size
    FILE_SIZE=$(du -h "build/libs/git-commit-helper-1.0-SNAPSHOT.jar" | cut -f1)
    echo -e "${GREEN}Plugin file size: ${FILE_SIZE}${NC}"
    
    # On macOS, provide option to show file in Finder
    if [[ "$(uname)" == "Darwin" ]]; then
        echo -e "${YELLOW}Do you want to show the plugin file in Finder? (y/n)${NC}"
        read -r response
        if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
            open -R "build/libs/git-commit-helper-1.0-SNAPSHOT.jar"
        fi
    fi
else
    echo -e "${RED}Build failed. Please check the error messages above.${NC}"
    exit 1
fi